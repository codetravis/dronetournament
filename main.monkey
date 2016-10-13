Import dronetournament
Import user
Import server
Import brl.json
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
	Field win_button:Image
	Field lose_button:Image
	Field join_button_image:Image
	Field join_button:Button

	Field t_fighter_img:Image
	Field eye_fighter_img:Image
	Field single_turret_img:Image
	Field tournament_server_url:String = "http://localhost:4567/dronetournament" '"https://evolvinggames.herokuapp.com/dronetournament"
	Field game_id:Int
	Field multiplayer_service:MultiplayerService
	Field game_list:JsonArray
	Field unit_types:JsonObject
	Field timer_begin:Float
	Field game_select:List<GameSelect>

	Method OnCreate()
		Print "Creating Game"
		game_state = "setup"
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
		
		t_fighter_img = LoadImage("images/t_fighter.png", 1, Image.MidHandle)
		eye_fighter_img = LoadImage("images/eye_fighter.png", 1, Image.MidHandle)
		single_turret_img = LoadImage("images/single_turret.png", 1, Image.MidHandle)
	End
	
	Method CreateUIElements()
		Self.play_tutorial_button = New Button(10, -100, 110, 60, 440, 170, Self.play_tutorial_button_image)
		Self.play_multiplayer_button = New Button(10, 100, 60, 260, 540, 170, Self.play_multiplayer_button_image)
		Self.join_button = New Button(10, 100, 60, 260, 540, 170, Self.join_button_image)
	End
	
	Method OnUpdate()
		If (game_state = "setup")
			GetUsername()
		Else If (game_state = "get_password")
			GetPassword()
		Else If (game_state = "menu")
			If (TouchDown(0))
				If (Self.play_tutorial_button.Selected())
					SetupTutorial()
				Else If (Self.play_multiplayer_button.Selected())
					SignIn() 					
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
				Self.game_state = "menu"
			End
		Else If (Self.game_state = "winner")
			If (TouchDown(0))
				Self.game_state = "menu"
			End
		End
		UpdateAsyncEvents
	End

	Method GetUsername:Void()
		EnableKeyboard()
		Local char = GetChar()
		If (char = CHAR_ENTER)
			DisableKeyboard()
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
	End
	
	Method GetPassword:Void()
		EnableKeyboard()
		Local char = GetChar()
		If (char = CHAR_ENTER)
			DisableKeyboard()
			Self.game_state = "menu"
		Else If (char = CHAR_BACKSPACE Or char = CHAR_DELETE)
			If (Self.user.password.Length() <= 1)
				Self.user.password = ""
			Else
				Self.user.password = Self.user.password[..-1]
			End
		Else If (char > 0 And Self.user.password.Length() < 13)
			Self.user.password += String.FromChar(char)
		End
	End

	Method SignIn:Void()
		Self.multiplayer_service.PostJsonRequest("/sign_in", "{ ~qusername~q: ~q" + user.username + "~q, ~qpassword~q: ~q" + user.password + "~q }")
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
			For Local key:Int = Eachin Self.game.units.Keys
				Local current_unit:Unit = Self.game.units.Get(key)
				If (current_unit.armor > 0)
					current_unit.DrawStatic(Self.user.player_id, Self.game_state)
				End
			End
			For Local part:Particle = Eachin Self.game.particles
				part.Draw()
			End
		Else If (Self.game_state = "loser")
			DrawImage(lose_button, 10, 100)
		Else If (Self.game_state = "winner")
			DrawImage(win_button, 10, 100)
		Else If (Self.game_state = "tutorial")
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
		Local t_type:UnitType = New UnitType(JsonObject("{~qname~q: ~qT-Fighter~q, ~qspeed~q: ~q120~q, ~qturn~q: ~q4~q, ~qarmor~q: ~q5~q, ~qfull_energy~q: ~q100~q, ~qcharge_energy~q: ~q5~q, ~qimage~q: ~qt_fighter.png~q}"))
		Self.game.opponents = New List<Unit>()
		Self.tutorial_unit = New Unit(1, 150.0, 150.0, -30, t_type, 1, 1)
		
		Local eye_type:UnitType = New UnitType(JsonObject("{~qname~q: ~qEye-Fighter~q, ~qspeed~q: ~q100~q, ~qturn~q: ~q3~q, ~qarmor~q: ~q2~q, ~qfull_energy~q: ~q100~q, ~qcharge_energy~q: ~q5~q, ~qimage~q: ~qeye_fighter.png~q}"))
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
		Self.multiplayer_service.PostRequest("/new_game/" + user.player_id)
		Self.game_state = "server"
	End	
	
	Method RunTutorial:Void()
		If (moves < 1)
			If (Self.tutorial_unit.armor < 1)
				Self.game_state = "loser"
			Else If (LiveOpponentCount(Self.game.opponents) = 0)
				Self.game_state = "winner"
			Else If (TouchDown(0))
				If (Self.tutorial_unit.ControlSelected(TouchX(0), TouchY(0)))
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
						'Print("Client created particle on step: " + Self.moves + " at:" + current_unit.position.x + ", " + current_unit.position.y + " with heading: " + current_unit.heading)
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
			For Local unit_id:Int = Eachin Self.game.units.Keys
				Local unit:Unit = Self.game.units.Get(unit_id)
				If (unit.player_id = Self.user.player_id  And Self.game.units.Get(unit_id).ControlSelected(TouchX(0), TouchY(0)))
					Self.game.units.Get(unit_id).SetControl(TouchX(0), TouchY(0), SCREEN_WIDTH, SCREEN_HEIGHT)
					Exit
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
		Local move_json:String = BuildMoveJson()
		Self.multiplayer_service.PostJsonRequest("/move_points/" + Self.game.id + "/" + Self.user.player_id + "/30", move_json)
		Self.game_state = "multiplayer_server"
	End

	Method EndTurn:Void()
		Local move_json:String = BuildMoveJson()
		Self.multiplayer_service.PostJsonRequest("/end_turn/" + Self.game.id, move_json)
		Self.game_state = "multiplayer_server"
	End
	
	Method BuildMoveJson:String()
		Print "Prepping move json"
		Local first:Int = 1
		Local move_json:String = "{ ~qdata~q : { ~qplayer_id~q: " + user.player_id + ", "
		move_json += "~qmoves~q : [ "
	
		For Local unit_id:Int = Eachin Self.game.units.Keys
			
			If (Self.game.units.Get(unit_id).player_id = Self.user.player_id)
				If first = 1
					first = 0
				Else
					move_json += ", "
				End
				move_json += "{ ~qunit_id~q: " + Self.game.units.Get(unit_id).unit_id + ", "
				move_json += "~qx~q: " + Self.game.units.Get(unit_id).position.x + ", "
				move_json += "~qy~q: " + Self.game.units.Get(unit_id).position.y + ", "
				move_json += "~qcontrol-x~q: " + Self.game.units.Get(unit_id).control.position.x + ", "
				move_json += "~qcontrol-y~q: " + Self.game.units.Get(unit_id).control.position.y + ", "
				move_json += "~qcontrol-heading~q: " + Self.game.units.Get(unit_id).control.position.heading + ", " 
				move_json += "~qheading~q: " + Self.game.units.Get(unit_id).heading 
				move_json += " }"
			End
		End
		move_json += " ] } }"
		
		Return move_json		
	End
	
	
	Method GetNextTurn:Void()
		Self.multiplayer_service.GetRequest("/next_turn/" + Self.game.id + "/" + Self.user.player_id)
		Self.game_state = "multiplayer_server"
	End
	
	Method CheckIfAllPlayersUpdated:Void()
		Self.multiplayer_service.GetRequest("/update_state/" + Self.game.id + "/" + Self.user.player_id)
		Self.game_state = "multiplayer_server"
	End
	
End



Function CounterClockwise:Bool(pointOne:Vec2D, pointTwo:Vec2D, pointThree:Vec2D)
	Return ((pointThree.y - pointOne.y) * (pointTwo.x - pointOne.x) > (pointTwo.y - pointOne.y) * (pointThree.x - pointOne.x)) 
End

Function LinesIntersect:Bool(pointA:Vec2D, pointB:Vec2D, pointC:Vec2D, pointD:Vec2D)
	' To determine if 2 lines intersect there is this sneaky way of doing it
	' where you look at the counterclockwiseness of 3 points at a time
	Local abc:Bool = CounterClockwise(pointA, pointB, pointC)
	Local abd:Bool = CounterClockwise(pointA, pointB, pointD)
	Local cda:Bool = CounterClockwise(pointC, pointD, pointA)
	Local cdb:Bool = CounterClockwise(pointC, pointD, pointB)

	Return(( abc <> abd) And (cda <> cdb))
End

Function Collided(particle:Particle, unit:Unit)

	If (unit.armor <= 0)
		Return False
	Else If (particle.team = unit.team)
		Return False
	End

	Local top_left:Vec2D = New Vec2D(unit.position.x - 10, unit.position.y - 10)
	Local top_right:Vec2D = New Vec2D(unit.position.x + 10, unit.position.y - 10)
	Local bottom_left:Vec2D = New Vec2D(unit.position.x - 10, unit.position.y + 10)
	Local bottom_right:Vec2D = New Vec2D(unit.position.x + 10, unit.position.y + 10)
	If (LinesIntersect(particle.past_position, particle.position, top_left, top_right ) Or
		LinesIntersect(particle.past_position, particle.position, top_left, bottom_left) Or
		LinesIntersect(particle.past_position, particle.position, top_right, bottom_right ) Or
		LinesIntersect(particle.past_position, particle.position, bottom_right, bottom_left))

		Return True
	Else
		Return False
	End
End

Function Main()
	New DroneTournamentGame()
End