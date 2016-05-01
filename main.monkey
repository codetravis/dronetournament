Import dronetournament

Class DroneTournamentGame Extends App

	Field player:Player
	Field moves:Int

	Method OnCreate()
		Print "Creating Game"
		SetUpdateRate(60)
		player = New Player(150.0, 150.0, -30, 100, 3)
		moves = 0
	End	
	
	Method OnUpdate()
		If (moves < 1)
			If (TouchDown(0))
				player.SetControl(TouchX(0), TouchY(0))
				player.UpdateLine()
			End
			
			If KeyHit(KEY_ENTER)
				moves = 30
			End
		Else
			player.Update()
			moves -= 1
		End
		
	End

	Method OnRender()
		Cls(100, 100, 100)
		player.DrawStatic()
	End
End

Function Main()
	New DroneTournamentGame()
End