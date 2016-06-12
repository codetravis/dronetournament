Import dronetournament

Class DroneTournamentGame Extends App

	Field player:Player
	Field moves:Int
	Field players:List<Player>
	Field opponents:List<Player>
	Field particles:List<Particle>
	Field game_state:String
	Field play_button:Image

	Method OnCreate()
		Print "Creating Game"
		game_state = "menu"
		SetUpdateRate(60)
		opponents = New List<Player>()
		player = New Player(150.0, 150.0, -30, 120, 4, 3, 1)
		For Local i:Int = 0 To 3
			Local xrand:Float = Rnd(200, 580)
			Local yrand:Float = Rnd(200, 420)
			Local opponent:Player = New Player(xrand, yrand, 30, 100, 3, 3, 0)
			opponents.AddLast(opponent)
		End
		moves = 0
		particles = New List<Particle>()
		play_button = LoadImage("images/Play_Button.png")
	End	
	
	Method OnUpdate()
		If (game_state = "menu")
			If (TouchDown(0))
				game_state = "tutorial"
			End
		Else If (game_state = "tutorial")
			If (moves < 1)
				If (TouchDown(0))
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
					enemy.Update()
				End
				player.Update()
				If (player.currentEnergy = 100)
					particles.AddLast(New Particle(player.position, 2.5, 1, player.heading, 20, player.friendly))
					player.FireWeapon()
				End
				For Local particle:Particle = Eachin particles
					particle.Update()
					For Local opponent:Player = Eachin opponents
						If Collided(particle, opponent)
							opponent.TakeDamage()
							particles.Remove(particle)
							Continue
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
		End
		
	End

	Method OnRender()
		If (game_state = "menu")
			Cls(100, 100, 100)
			DrawImage(play_button, 10, 100)
		Else If (game_state = "tutorial")
		
			Cls(100, 100, 100)
			player.DrawStatic()
			
			For Local enemy:Player = Eachin opponents
				If enemy.armor > 0
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

		Local top_left:Vec2D = New Vec2D(unit.position.x, unit.position.y)
		Local top_right:Vec2D = New Vec2D(unit.position.x + 15, unit.position.y)
		Local bottom_left:Vec2D = New Vec2D(unit.position.x, unit.position.y + 15)
		Local bottom_right:Vec2D = New Vec2D(unit.position.x + 15, unit.position.y + 15)
		If (LinesIntersect(particle.past_position, particle.position, top_left, top_right ) Or
			LinesIntersect(particle.past_position, particle.position, top_left, bottom_left) Or
			LinesIntersect(particle.past_position, particle.position, top_right, bottom_right ) Or
			LinesIntersect(particle.past_position, particle.position, bottom_right, bottom_left))
			Print "Collision"
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
	
	
End

Function CounterClockwise:Bool(pointOne:Vec2D, pointTwo:Vec2D, pointThree:Vec2D)
	Return ((pointThree.y - pointOne.y) * (pointTwo.x - pointOne.x) > (pointTwo.y - pointOne.y) * (pointThree.x - pointOne.x)) 
End

Function Main()
	New DroneTournamentGame()
End