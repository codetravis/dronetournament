Import dronetournament
Import user
Import server
Import brl.json

Class DroneTournamentGame Extends App

	Field user:User
	Field unit:Unit
	Field moves:Int
	Field game:Game
	Field game_state:String
	Field play_button:Image
	Field win_button:Image
	Field lose_button:Image
	Field t_fighter_img:Image
	Field tournament_server_url:String = "http://localhost:4567/dronetournament" '"https://evolvinggames.herokuapp.com/dronetournament"
	Field game_id:Int
	Field multiplayer_service:MultiplayerService
	Field game_list:JsonArray
	Field unit_types:JsonObject

	Method OnCreate()
		Print "Creating Game"
		game_state = "setup"
		SetUpdateRate(30)
		user = New User("")
		
		multiplayer_service = New MultiplayerService(tournament_server_url)
		game = New Game()
		
		LoadImages()
		
	End	
	
	Method LoadImages()
		play_button = LoadImage("images/play_button.png")
		win_button = LoadImage("images/win_button.png")
		lose_button = LoadImage("images/lose_button.png")
		t_fighter_img = LoadImage("images/t_fighter.png", 1, Image.MidHandle)
	End
	
	Method OnUpdate()
		If (game_state = "setup")
			GetUsername()
		Else If (game_state = "menu")
			If (TouchDown(0))
				If (TouchY(0) < 240)
					SetupTutorial()
				Else
					SignIn() 					
				End
			End
		Else If (game_state = "server")
			DetermineGameState()
		Else If (game_state = "get_games")
			GetListOfActiveGames()
		Else If (game_state = "list_games")
			If (TouchDown(0))
				Local game:JsonObject = JsonObject(Self.game_list.Get(0))
				Local game_id:String = game.GetString("game_id")
				Print "Game ID: " + game_id
				GetGameInfoFromServer(game_id)
			End
		Else If (game_state = "tutorial")
			If (moves < 1)
				If (unit.armor < 1)
					Self.game_state = "loser"
				Else If (LiveOpponentCount(Self.game.opponents) = 0)
					Self.game_state = "winner"
				Else If (TouchDown(0))
					If (Self.unit.ControlSelected(TouchX(0), TouchY(0)))
						Self.unit.SetControl(TouchX(0), TouchY(0), 640, 480)
					End
				Else
					Self.unit.ControlReleased()
				End
				
				If KeyHit(KEY_ENTER)
					For Local enemy:Unit = Eachin Self.game.opponents
						Local xrand = Rnd(-15.0, 15.0)
						Local yrand = Rnd(-15.0, 15.0)
						enemy.SetControl(Self.unit.position.x + xrand, Self.unit.position.y + yrand, 640, 480)
					End
					moves = 30
				End
			Else
				For Local enemy:Unit = Eachin Self.game.opponents
					If (enemy.armor > 0)
						enemy.Update()
						If (enemy.currentEnergy = 100)
							game.particles.AddLast(New Particle(enemy.position, 2.5, 1, enemy.heading, 20, enemy.friendly))
							enemy.FireWeapon()
						End
					End
				End
				If (Self.unit.armor > 0)
					Self.unit.Update()
					If (Self.unit.currentEnergy = 100)
						Self.game.particles.AddLast(New Particle(Self.unit.position, 2.5, 1, Self.unit.heading, 20, Self.unit.friendly))
						Self.unit.FireWeapon()
					End
				End
				For Local particle:Particle = Eachin Self.game.particles
					particle.Update()
					If Collided(particle, Self.unit)
						Self.unit.TakeDamage()
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
					Self.unit.SetControl(Self.unit.position.x + Self.unit.velocity.x, Self.unit.position.y + Self.unit.velocity.y, 640, 480)
				End
			End
		Else If (Self.game_state = "multiplayer")
		
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
			Self.game_state = "menu"
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

	Method SignIn:Void()
		Self.multiplayer_service.PostRequest("/sign_in/" + user.username)
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
				Self.game_state = "list_games"
			Else If (action = "Load Game")
				Self.game.LoadFromJson(JsonObject(multiplayer_service.response.Get("game")))
			End
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
	
	
	Method OnRender()
		Cls(100, 100, 100)

		If (game_state = "setup")	
			DrawText("Enter a username: " + user.username, 50, 200)
		Else If (game_state = "menu")
			DrawImage(play_button, 10, 100)
		Else If (game_state = "list_games")
			DrawText("List Games", 50, 50)
			
		Else If (game_state = "loser")
			DrawImage(lose_button, 10, 100)
		Else If (game_state = "winner")
			DrawImage(win_button, 10, 100)
		Else If (game_state = "tutorial")
			If (unit.armor > 0)
				unit.DrawStatic()
			End
			
			For Local enemy:Unit = Eachin Self.game.opponents
				If (enemy.armor > 0)
					enemy.DrawStatic()
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
		Self.game.opponents = New List<Unit>()
		Self.unit = New Unit(150.0, 150.0, -30, 120, 4, 5, t_fighter_img, 1)
		For Local i:Int = 0 To 3
			Local xrand:Float = Rnd(200, 580)
			Local yrand:Float = Rnd(200, 420)
			Local opponent:Unit = New Unit(xrand, yrand, 30, 100, 3, 3, t_fighter_img, 0)
			Self.game.opponents.AddLast(opponent)
		End
		Self.moves = 0
		Self.game.particles = New List<Particle>()
		Self.game_state = "tutorial"
	End
	
	Method SetupMultiplayer:Void()
		Self.game.units.AddLast(New Player(150.0, 150.0, -30, 120, 4, 5, t_fighter_img, 1))
		Self.moves = 0
		Self.game.particles = New List<Particle>()
	End
	

	Method CreateNewMultiplayerGame:Void()
		Self.multiplayer_service.PostRequest("/new_game/" + user.player_id)
		Self.game_state = "server"
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
	If (particle.friendly = unit.friendly)
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