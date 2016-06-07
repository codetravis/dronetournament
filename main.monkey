Import dronetournament

Class DroneTournamentGame Extends App

	Field player:Player
	Field opponent:Player
	Field moves:Int
	Field players:List<Player>
	Field particles:List<Particle>

	Method OnCreate()
		Print "Creating Game"
		SetUpdateRate(60)
		player = New Player(150.0, 150.0, -30, 150, 5, 1)
		opponent = New Player(250.0, 250.0, 30, 100, 3, 0)
		moves = 0
		particles = New List<Particle>()
	End	
	
	Method OnUpdate()
		If (moves < 1)
			If (TouchDown(0))
				If (player.ControlSelected(TouchX(0), TouchY(0)))
					player.SetControl(TouchX(0), TouchY(0), 640, 480)
				End
			Else
				player.ControlReleased()
			End
			
			If KeyHit(KEY_ENTER)
				opponent.SetControl(player.position.x, player.position.y, 640, 480)
				moves = 30
			End
		Else
			opponent.Update()
			player.Update()
			If (player.currentEnergy = 100)
				particles.AddLast(New Particle(player.position, 2.5, 1, player.heading, 20))
				player.FireWeapon()
			End
			For Local part:Particle = Eachin particles
				part.Update()
				If part.lifetime <= 0
					particles.Remove(part)
				End
			End
			moves -= 1
			If (moves < 1)
				player.SetControl(player.position.x + player.velocity.x, player.position.y + player.velocity.y, 640, 480)
			End
		End
		
	End

	Method OnRender()
		Cls(100, 100, 100)
		player.DrawStatic()
		opponent.DrawStatic()
		For Local part:Particle = Eachin particles
			part.Draw()
		End
	End
End

Function Main()
	New DroneTournamentGame()
End