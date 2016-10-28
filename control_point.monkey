Import dronetournament

Class ControlPoint
	Field position:Vec2D
	Field width:Float
	Field height:Float
	Field selected:Bool
	Field heading:Float

	Method New(x:Float, y:Float, heading:Float, width:Float, height:Float)
		Self.position = New Vec2D(x, y)
		Self.width = width
		Self.height = height
		Self.selected = False
		Self.heading = heading
	End
	
	Method Draw()
		SetColor(255, 255, 128)
		DrawRect(Self.position.x, Self.position.y, Self.width, Self.height)
	End
	
End