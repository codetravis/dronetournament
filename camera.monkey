Import dronetournament

Class Camera
	
	Field pan_distance:Float
	Field position:Vec2D
	Field top_limit:Int
	Field bottom_limit:Int
	Field left_limit:Int
	Field right_limit:Int

	Method New (game_width:Int=640, game_height:Int=480, screen_width:Int=640, screen_height:Int=480)
		Self.pan_distance = 10
		Self.position = New Vec2D(0, 0)
		
		Self.top_limit = 0
		Self.left_limit = 0
		Self.bottom_limit = -1 * (game_height - screen_height)
		Self.right_limit = -1 * (game_width - screen_width)
	End
	
	Method Reset()
		Self.position.x = 0
		Self.position.y = 0
	End

	Method MoveRight()
		Print "Move Camera Right"
		Self.position.x -= pan_distance
		If (Self.position.x < Self.right_limit)
			Self.position.x = Self.right_limit
		End
	End

	Method MoveLeft()
		Print "Move Camera Left"
		Self.position.x += pan_distance
		If (Self.position.x > Self.left_limit)
			Self.position.x = Self.left_limit
		End
	End
	
	Method MoveUp()
		Print "Move Camera Up"
		Self.position.y += pan_distance
		If (Self.position.y > Self.top_limit)
			Self.position.y = Self.top_limit
		End
	End
	
	Method MoveDown()
		Print "Move Camera Down"
		Self.position.y -= pan_distance
		If (Self.position.y < Self.bottom_limit)
			Self.position.y = Self.bottom_limit
		End
	End

End