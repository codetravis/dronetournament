Import dronetournament

Class DroneTournamentGame Extends App

	Field player:Player
	Field moves:Int
	Field players:List<Player>
	Field opponents:List<Player>
	Field particles:List<Particle>
	Field game_state:String
	Field play_button:Image
	Field win_button:Image
	Field lose_button:Image
	Field t_fighter_img:Image

	Method OnCreate()
		Print "Creating Game"
		game_state = "menu"
		SetUpdateRate(30)
		
		play_button = LoadImage("images/play_button.png")
		win_button = LoadImage("images/win_button.png")
		lose_button = LoadImage("images/lose_button.png")
		t_fighter_img = LoadImage("images/t_fighter.png", 1, Image.MidHandle)
		
		
		
	End	
	
	Method OnUpdate()
		If (game_state = "menu")
			If (TouchDown(0))
				opponents = New List<Player>()
				player = New Player(150.0, 150.0, -30, 120, 4, 5, t_fighter_img, 1)
				For Local i:Int = 0 To 3
					Local xrand:Float = Rnd(200, 580)
					Local yrand:Float = Rnd(200, 420)
					Local opponent:Player = New Player(xrand, yrand, 30, 100, 3, 3, t_fighter_img, 0)
				opponents.AddLast(opponent)
				End
				moves = 0
				particles = New List<Particle>()
				game_state = "tutorial"
			End
		Else If (game_state = "tutorial")
			If (moves < 1)
				If (player.armor < 1)
					game_state = "loser"
				Else If (LiveOpponentCount(opponents) = 0)
					game_state = "winner"
				Else If (TouchDown(0))
					If (player.ControlSelected(TouchX(0), TouchY(0)))
						player.SetControl(TouchX(0), TouchY(0), 640, 480)
					End
				Else
					player.ControlReleased()
				End
				
				If KeyHit(KEY_ENTER)
					For Local enemy:Player = Eachin opponents
						Local xrand = Rnd(-15.0, 15.0)
						Local yrand = Rnd(-15.0, 15.0)
						enemy.SetControl(player.position.x + xrand, player.position.y + yrand, 640, 480)
					End
					moves = 30
				End
			Else
				For Local enemy:Player = Eachin opponents
					If (enemy.armor > 0)
						enemy.Update()
						If (enemy.currentEnergy = 100)
							particles.AddLast(New Particle(enemy.position, 2.5, 1, enemy.heading, 20, enemy.friendly))
							enemy.FireWeapon()
						End
					End
				End
				If (player.armor > 0)
					player.Update()
					If (player.currentEnergy = 100)
						particles.AddLast(New Particle(player.position, 2.5, 1, player.heading, 20, player.friendly))
						player.FireWeapon()
					End
				End
				For Local particle:Particle = Eachin particles
					particle.Update()
					If Collided(particle, player)
						player.TakeDamage()
						particles.Remove(particle)
					Else
						For Local opponent:Player = Eachin opponents
							If Collided(particle, opponent)
								opponent.TakeDamage()
								particles.Remove(particle)
								Exit
							End
						End
					End

					If particle.lifetime <= 0
						particles.Remove(particle)
					End
				End
				moves -= 1
				If (moves < 1)
					player.SetControl(player.position.x + player.velocity.x, player.position.y + player.velocity.y, 640, 480)
				End
			End
		Else If (game_state = "multiplayer")
		
		Else If (game_state = "loser")
			If (TouchDown(0))
				game_state = "menu"
			End
		Else If (game_state = "winner")
			If (TouchDown(0))
				game_state = "menu"
			End
		End
		
	End

	Method OnRender()
		If (game_state = "menu")
			Cls(100, 100, 100)
			DrawImage(play_button, 10, 100)
		Else If (game_state = "loser")
			Cls(100, 100, 100)
			DrawImage(lose_button, 10, 100)
		Else If (game_state = "winner")
			Cls(100, 100, 100)
			DrawImage(win_button, 10, 100)
		Else If (game_state = "tutorial")
			Cls(100, 100, 100)
			If (player.armor > 0)
				player.DrawStatic()
			End
			
			For Local enemy:Player = Eachin opponents
				If (enemy.armor > 0)
					enemy.DrawStatic()
				End
			End
			
			For Local part:Particle = Eachin particles
				part.Draw()
			End
		End
	End
	
	Method Collided(particle:Particle, unit:Player)
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
	
	Method LinesIntersect:Bool(pointA:Vec2D, pointB:Vec2D, pointC:Vec2D, pointD:Vec2D)
		' To determine if 2 lines intersect there is this sneaky way of doing it
		' where you look at the counterclockwiseness of 3 points at a time
		Local abc:Bool = CounterClockwise(pointA, pointB, pointC)
		Local abd:Bool = CounterClockwise(pointA, pointB, pointD)
		Local cda:Bool = CounterClockwise(pointC, pointD, pointA)
		Local cdb:Bool = CounterClockwise(pointC, pointD, pointB)

		Return(( abc <> abd) And (cda <> cdb))
	End
	
	Method LiveOpponentCount:Int(enemies:List<Player>)
		Local live_opponents:Int = 0
		For Local enemy:Player = Eachin enemies
			If (enemy.armor > 0)
				live_opponents = live_opponents + 1
			End
		End
		Return live_opponents
	End
	
End

Function CounterClockwise:Bool(pointOne:Vec2D, pointTwo:Vec2D, pointThree:Vec2D)
	Return ((pointThree.y - pointOne.y) * (pointTwo.x - pointOne.x) > (pointTwo.y - pointOne.y) * (pointThree.x - pointOne.x)) 
End

Function Main()
	New DroneTournamentGame()
End