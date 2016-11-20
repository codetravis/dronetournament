Import brl.json
Import dronetournament
Import game
Import unit
Import particle
Import user
Import server
Import user_interface

Const SCREEN_WIDTH:Int = 640
Const SCREEN_HEIGHT:Int = 480

Class DroneTournamentGame Extends App

	Field user:User
	Field tutorial_unit:Unit
	Field moves:Int
	Field game:Game
	Field game_state:String
	Field play_tutorial_button_image:Image
	Field play_tutorial_button:Button
	Field play_multiplayer_button_image:Image
	Field play_multiplayer_button:Button
	Field end_turn_button_image:Image
	Field end_turn_button:Button
	Field win_button:Image
	Field lose_button:Image
	Field join_button_image:Image
	Field join_button:Button
	Field keyboard_enabled:Bool

	Field t_fighter_img:Image
	Field eye_fighter_img:Image
	Field single_turret_img:Image
	Field tournament_server_url:String = "https://evolvinggames.herokuapp.com/dronetournament"
	'Field tournament_server_url:String = "http://localhost:3000/dronetournament"
	Field game_id:Int
	Field multiplayer_service:MultiplayerService
	Field game_list:JsonArray
	Field unit_types:JsonObject
	Field timer_begin:Float
	Field game_select:List<GameSelect>

	Method OnCreate()
		Print "Creating Game"
		game_state = "menu"
		Self.keyboard_enabled = False
		Self.timer_begin = Millisecs()
		SetUpdateRate(30)
		user = New User("")
		
		multiplayer_service = New MultiplayerService(tournament_server_url)
		game = New Game()
		
		LoadImages()
		CreateUIElements()
		
	End	
	
	Method LoadImages()
		play_tutorial_button_image = LoadImage("images/play_tutorial_button.png")
		play_multiplayer_button_image = LoadImage("images/play_multiplayer_button.png")
		join_button_image = LoadImage("images/join_game.png")
		win_button = LoadImage("images/win_button.png")
		lose_button = LoadImage("images/lose_button.png")
		end_turn_button_image = LoadImage("images/end_turn_button.png")
		
		t_fighter_img = LoadImage("images/t_fighter.png", 1, Image.MidHandle)
		eye_fighter_img = LoadImage("images/eye_fighter.png", 1, Image.MidHandle)
		single_turret_img = LoadImage("images/single_turret.png", 1, Image.MidHandle)
	End
	
	Method CreateUIElements()
		Self.play_tutorial_button = New Button(10, -100, 110, 60, 440, 170, Self.play_tutorial_button_image)
		Self.play_multiplayer_button = New Button(10, 100, 60, 260, 540, 170, Self.play_multiplayer_button_image)
		Self.join_button = New Button(10, 100, 60, 260, 540, 170, Self.join_button_image)
		Self.end_turn_button = New Button(200, 500, 250, 650, 540, 170, Self.end_turn_button_image)  
	End
	
	Method OnUpdate()
		If (game_state = "setup")
			GetUsername()
		Else If (game_state = "get_password")
			GetPassword()
		Else If (game_state = "menu")
			If (TouchDown(0) And (Millisecs() - Self.timer_begin > 1000))
				If (Self.play_tutorial_button.Selected())
					SetupTutorial()
				Else If (Self.play_multiplayer_button.Selected())
					If (Self.user.player_id = "0")
						Self.game_state = "setup"
					Else
						SignIn() 
					End			
				End
			End
		Else If (Self.game_state = "server" Or Self.game_state = "multiplayer_server")
			DetermineGameState()
		Else If (game_state = "get_games")
			GetListOfActiveGames()
		Else If (game_state = "list_games")
			If (TouchDown(0))
				For Local game_ui:GameSelect = Eachin Self.game_select
					If (game_ui.Selected())
						GetGameInfoFromServer(game_ui.game_id)
						Exit
					End
				End
				
				If (join_button.Selected())
					JoinGame()
				End
			End
		Else If (game_state = "tutorial")
			RunTutorial()
		Else If (Self.game_state = "multiplayer")
			UserPlanMoves()
		Else If (Self.game_state = "end_turn")
			If (Millisecs() - Self.timer_begin > 3000)
				Self.timer_begin = Millisecs()
				GetNextTurn()
			End
		Else If (Self.game_state = "multiplayer_ready")
			RunMultiplayer()
		Else If (Self.game_state = "updated")
			If (Millisecs() - Self.timer_begin > 3000)
				Self.timer_begin = Millisecs()
				CheckIfAllPlayersUpdated()
			End
		Else If (Self.game_state = "loser")
			If (TouchDown(0))
				Self.timer_begin = Millisecs()
				Self.game_state = "menu"
			End
		Else If (Self.game_state = "winner")
			If (TouchDown(0))
				Self.timer_begin = Millisecs()
				Self.game_state = "menu"
			End
		End
		UpdateAsyncEvents
	End

	Method GetUsername:Void()
		If (Self.keyboard_enabled)
			Local char = GetChar()
			If (char = CHAR_ENTER)
				DisableKeyboard()
				Self.keyboard_enabled = False
				Self.game_state = "get_password"
			Else If (char = CHAR_BACKSPACE Or char = CHAR_DELETE)
				If (Self.user.username.Length() <= 1)
					Self.user.username = ""
				Else
					Self.user.username = Self.user.username[..-1]
				End
			Else If (char > 0 And Self.user.username.Length() < 13)
				Self.user.username += String.FromChar(char)
			End
		Else
			EnableKeyboard()
			Self.keyboard_enabled = True
		End
	End
	
	Method GetPassword:Void()
		If (Self.keyboard_enabled)
			Local char = GetChar()
			If (char = CHAR_ENTER)
				DisableKeyboard()
				Self.keyboard_enabled = False
				SignIn()
			Else If (char = CHAR_BACKSPACE Or char = CHAR_DELETE)
				If (Self.user.password.Length() <= 1)
					Self.user.password = ""
				Else
					Self.user.password = Self.user.password[..-1]
				End
			Else If (char > 0 And Self.user.password.Length() < 13)
				Self.user.password += String.FromChar(char)
			End
		Else
			EnableKeyboard()
			Self.keyboard_enabled = True
		End
	End

	Method SignIn:Void()
		Local json_body:String = "{ ~qusername~q: ~q" + Self.user.username + "~q, ~qpassword~q: ~q" + Self.user.password + "~q }"
		Self.multiplayer_service.PostJsonRequest("/sign_in", json_body)
		Self.game_state = "server"
	End

	Method DetermineGameState:Void()
		If (Self.multiplayer_service.HasRequestFinished)
			Local action:String = Self.multiplayer_service.response.GetString("action")
			If (action = "Bad Server Response")
				Self.game_state = "menu"
			Else If (action = "Sign In")
				Self.user.player_id = Self.multiplayer_service.response.GetString("player_id")
				Self.game_state = "get_games"
			Else If (action = "Invalid Sign In")
				Self.user = New User("")
				Self.game_state = "setup"
			Else If (action = "List Games")
				Self.game_list = JsonArray(multiplayer_service.response.Get("games"))
				BuildGameListUI()
				Self.game_state = "list_games"
			Else If (action = "Load Game")
				Self.game.LoadFromJson(Self.multiplayer_service.response, Self.user.player_id)
				UsePlayerStateToSetGameState()
			Else If (action = "Turn Stop")
				Self.game_state = "multiplayer"
			Else If (action = "Waiting" Or action = "Turn Ended")
				Self.timer_begin = Millisecs()
				game_state = "end_turn"
			Else If (action = "Ready")
				Self.moves = 30
				Self.game.LoadServerMoves(Self.multiplayer_service.response)
				WinLoseOrContinue()
			Else If (action = "Update Waiting")
				Self.timer_begin = Millisecs()
				game_state = "updated"
			Else If (action = "Update Ready")
				GetGameInfoFromServer(Self.game.id)
			Else If (action = "Server Move Points")
				Self.game.SetUnitPathsToServerSimulation(Self.multiplayer_service.response, Self.user.player_id)
				EndTurn()
			End
		End	
	End

	Method WinLoseOrContinue:Void()
		Local player_unit_count:Int = 0
		Local opponent_unit_count:Int = 0

		For Local key:Int = Eachin Self.game.units.Keys
			Local current_unit:Unit = Self.game.units.Get(key)
			If (current_unit.armor > 0 And current_unit.player_id = Self.user.player_id)
				player_unit_count += 1
			Else If (current_unit.armor > 0)
				opponent_unit_count += 1
			End
		End
		
		If (player_unit_count = 0)	
			Self.game_state = "loser"
		Else If (opponent_unit_count = 0)
			Self.game_state = "winner"
		Else
			Self.game_state = "multiplayer_ready"
		End
	End

	Method GetListOfActiveGames:Void()
		Self.multiplayer_service.GetRequest("/games/" + Self.user.player_id)
		Self.game_state = "server"
	End

	Method GetGameInfoFromServer:Void(game_id:String)
		Self.multiplayer_service.GetRequest("/game/" + game_id)
		Self.game_state = "server"
	End
	
	Method JoinGame:Void()
		Self.multiplayer_service.PostRequest("/join_game/" + user.player_id)
		Self.game_state = "server"
	End
	
	Method BuildGameListUI()
		Self.game_select = New List<GameSelect>()
		Local x = 10
		Local y = 10
		For Local i:Int = 0 Until Self.game_list.Length
			Local game_object:JsonObject = JsonObject(Self.game_list.Get(i))
			Local game_ui:GameSelect = New GameSelect(x, y, 400, 50, String(game_object.GetInt("game_id")))
			Self.game_select.AddLast(game_ui)
			y += 55
		End 
	End
	
	Method UsePlayerStateToSetGameState()
		If (Self.game.player_state = "plan")
			Self.game_state = "multiplayer"
		Else If (Self.game.player_state = "finished")
			Self.game_state = "end_turn"
		Else If (Self.game.player_state = "updated")
			Self.game_state = "updated"
		End
	End
	
	Method OnRender()
		Cls(100, 100, 100)		
		
		If (Self.game_state = "setup")	
			DrawText("Enter a username: " + user.username, 50, 200)
		Else If (Self.game_state = "get_password")
			DrawText("Enter a password: " + user.password, 50, 200)
		Else If (game_state = "menu")
			Self.play_tutorial_button.Draw()
			Self.play_multiplayer_button.Draw()
		Else If (game_state = "list_games")
			For Local game_ui:GameSelect = Eachin Self.game_select
				game_ui.Draw()
			End
			Self.join_button.Draw()
		Else If (Self.game_state = "multiplayer" Or
		 		Self.game_state = "multiplayer_ready" Or
		  		Self.game_state = "end_turn" Or
		   		Self.game_state = "updated" Or
		   		Self.game_state = "multiplayer_server")

		   	Self.end_turn_button.Draw()
			Self.game.Draw(Self.user.player_id, Self.game_state)
		Else If (Self.game_state = "loser")
			DrawImage(lose_button, 10, 100)
		Else If (Self.game_state = "winner")
			DrawImage(win_button, 10, 100)
		Else If (Self.game_state = "tutorial")
			Self.end_turn_button.Draw()
			
			If (Self.tutorial_unit.armor > 0)
				Self.tutorial_unit.DrawStatic(1, Self.game_state)
			End
			
			For Local enemy:Unit = Eachin Self.game.opponents
				If (enemy.armor > 0)
					enemy.DrawStatic(2, Self.game_state)
				End
			End
			
			For Local part:Particle = Eachin Self.game.particles
				part.Draw()
			End
		End
	End
	
	Method LiveOpponentCount:Int(enemies:List<Unit>)
		Local live_opponents:Int = 0
		For Local enemy:Unit = Eachin enemies
			If (enemy.armor > 0)
				live_opponents = live_opponents + 1
			End
		End
		Return live_opponents
	End
	
	Method SetupTutorial:Void() 
		Local t_type:UnitType = New UnitType(JsonObject("{~qid~q: 1, ~qname~q: ~qT-Fighter~q, ~qspeed~q: 100, ~qturn~q: 5, ~qarmor~q: 6, ~qfull_energy~q: 100, ~qcharge_energy~q: 6, ~qimage_name~q: ~qt_fighter.png~q}"))
		Self.game.opponents = New List<Unit>()
		Self.tutorial_unit = New Unit(1, 150.0, 150.0, -30, t_type, 1, 1)
		
		Local eye_type:UnitType = New UnitType(JsonObject("{~qid~q: 2, ~qname~q: ~qEye-Fighter~q, ~qspeed~q: 120, ~qturn~q: 4, ~qarmor~q: 2, ~qfull_energy~q: 100, ~qcharge_energy~q: 4, ~qimage_name~q: ~qeye_fighter.png~q}"))
		For Local i:Int = 0 To 3
			Local xrand:Float = Rnd(200, 580)
			Local yrand:Float = Rnd(200, 420)
			Local opponent:Unit = New Unit(i + 2, xrand, yrand, 30, eye_type, 0, 2)
			Self.game.opponents.AddLast(opponent)
		End
		Self.moves = 0
		Self.game.particles = New List<Particle>()
		Self.game_state = "tutorial"
	End
	
	Method SetupMultiplayer:Void()
		Self.moves = 0
		Self.game.particles = New List<Particle>()
	End

	Method CreateNewMultiplayerGame:Void()
		Self.multiplayer_service.PostRequest("/new_game/" + Self.user.player_id)
		Self.game_state = "server"
	End	
	
	Method RunTutorial:Void()
		If (moves < 1)
			If (Self.tutorial_unit.armor < 1)
				Self.game_state = "loser"
			Else If (LiveOpponentCount(Self.game.opponents) = 0)
				Self.game_state = "winner"
			Else If (TouchDown(0))
				If (Self.end_turn_button.Selected())
					For Local enemy:Unit = Eachin Self.game.opponents
						Local xrand = Rnd(-15.0, 15.0)
						Local yrand = Rnd(-15.0, 15.0)
						enemy.SetControl(Self.tutorial_unit.position.x + xrand, 
										 Self.tutorial_unit.position.y + yrand,
										 SCREEN_WIDTH, SCREEN_HEIGHT)
					End
					moves = 30
				Else If (Self.tutorial_unit.ControlSelected(TouchX(0), TouchY(0)))
					Self.tutorial_unit.SetControl(TouchX(0), TouchY(0), SCREEN_WIDTH, SCREEN_HEIGHT)
				End
			Else
				Self.tutorial_unit.ControlReleased()
			End
				
			If KeyHit(KEY_ENTER)
				For Local enemy:Unit = Eachin Self.game.opponents
					Local xrand = Rnd(-15.0, 15.0)
					Local yrand = Rnd(-15.0, 15.0)
					enemy.SetControl(Self.tutorial_unit.position.x + xrand, Self.tutorial_unit.position.y + yrand, SCREEN_WIDTH, SCREEN_HEIGHT)
				End
				moves = 30
			End
		Else
			For Local enemy:Unit = Eachin Self.game.opponents
				If (enemy.armor > 0)
					enemy.Update()
					If (enemy.currentEnergy = 100)
						game.particles.AddLast(New Particle(enemy.position, 2.5, 1, enemy.heading, 20, enemy.team))
						enemy.FireWeapon()
					End
				End
			End
			
			If (Self.tutorial_unit.armor > 0)
				Self.tutorial_unit.Update()
				If (Self.tutorial_unit.currentEnergy = 100)
					Self.game.particles.AddLast(New Particle(Self.tutorial_unit.position, 2.5, 1, Self.tutorial_unit.heading, 20, Self.tutorial_unit.team))
					Self.tutorial_unit.FireWeapon()
				End
			End
			
			For Local particle:Particle = Eachin Self.game.particles
				particle.Update()
				If Collided(particle, Self.tutorial_unit)
					Self.tutorial_unit.TakeDamage()
					Self.game.particles.Remove(particle)
				Else
					For Local opponent:Unit = Eachin Self.game.opponents
						If Collided(particle, opponent)
							opponent.TakeDamage()
							Self.game.particles.Remove(particle)
							Exit
						End
					End
				End

				If particle.lifetime <= 0
					Self.game.particles.Remove(particle)
				End
			End
			moves -= 1
			If (moves < 1)
				Self.tutorial_unit.SetControl(Self.tutorial_unit.position.x + Self.tutorial_unit.velocity.x, Self.tutorial_unit.position.y + Self.tutorial_unit.velocity.y, SCREEN_WIDTH, SCREEN_HEIGHT)
			End
		End
	End

	Method RunMultiplayer:Void()
		If (Self.moves > 0)

			For Local unit_id:Int = Eachin Self.game.units.Keys
				Local current_unit:Unit = Self.game.units.Get(unit_id)
				If (current_unit.armor > 0)
					Self.game.units.Get(unit_id).Update()
					current_unit = Self.game.units.Get(unit_id)
					If (current_unit.currentEnergy >= current_unit.unit_type.maxEnergy)
						game.particles.AddLast(New Particle(current_unit.position, 2.5, 1, current_unit.heading, 20, current_unit.team))
						Self.game.units.Get(unit_id).FireWeapon()
					End
					If (moves = 1)
						Self.game.units.Get(unit_id).SetControl(current_unit.position.x + current_unit.velocity.x, current_unit.position.y + current_unit.velocity.y, SCREEN_WIDTH, SCREEN_HEIGHT)
					End
				End
			End

			For Local particle:Particle = Eachin Self.game.particles
				particle.Update()
				For Local unit_id:Int = Eachin Self.game.units.Keys
					Local current_unit:Unit = Self.game.units.Get(unit_id)
					If Collided(particle, current_unit)
						Self.game.units.Get(unit_id).TakeDamage()
						Self.game.particles.Remove(particle)
						Exit
					End
				End

				If particle.lifetime <= 0
					Self.game.particles.Remove(particle)
				End
			End
			Self.moves -= 1
		Else
			Self.game_state = "updated"
		End
	End

	Method UserPlanMoves:Void()
		If (TouchDown(0))
			If (Self.end_turn_button.Selected())
				GetServerMoves()
			Else
				For Local unit_id:Int = Eachin Self.game.units.Keys
					Local unit:Unit = Self.game.units.Get(unit_id)
					If (unit.player_id = Self.user.player_id  And Self.game.units.Get(unit_id).ControlSelected(TouchX(0), TouchY(0)))
						Self.game.units.Get(unit_id).SetControl(TouchX(0), TouchY(0), SCREEN_WIDTH, SCREEN_HEIGHT)
						Exit
					End
				End
			End
		Else
			For Local unit_id:Int = Eachin Self.game.units.Keys
				Self.game.units.Get(unit_id).ControlReleased()
			End
		End
		
		If KeyHit(KEY_ENTER)
			GetServerMoves()
		End
	
	End
	
	Method GetServerMoves:Void()
		Local move_json:String = Self.game.BuildMoveJson(Self.user.player_id)
		Self.multiplayer_service.PostJsonRequest("/move_points/" + Self.game.id + "/" + Self.user.player_id + "/30", move_json)
		Self.game_state = "multiplayer_server"
	End

	Method EndTurn:Void()
		Local move_json:String = Self.game.BuildMoveJson(Self.user.player_id)
		Self.multiplayer_service.PostJsonRequest("/end_turn/" + Self.game.id, move_json)
		Self.game_state = "multiplayer_server"
	End
	
	
	Method GetNextTurn:Void()
		Self.multiplayer_service.GetRequest("/next_turn/" + Self.game.id + "/" + Self.user.player_id)
		Self.game_state = "multiplayer_server"
	End
	
	Method CheckIfAllPlayersUpdated:Void()
		Self.multiplayer_service.GetRequest("/update_state/" + Self.game.id + "/" + Self.user.player_id)
		Self.game_state = "multiplayer_server"
	End
	
	Method OnPause()
	
	End
	
	Method OnResume()
	
	End
	
End

Function Main()
	New DroneTournamentGame()
End