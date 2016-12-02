Import dronetournament

Class ControlPoint
	Field position:Vec2D
	Field width:Float
	Field height:Float
	Field selected:Bool
	Field heading:Float
	Field image:Image

	Method New(x:Float, y:Float, heading:Float, width:Float, height:Float)
		Self.position = New Vec2D(x, y)
		Self.width = width
		Self.height = height
		Self.selected = False
		Self.heading = heading
		Self.image = LoadImage("images/control_point.png", 1, Image.MidHandle)
	End
	
	Method Draw()
		
		If (Self.selected)
			SetColor(255, 255, 128)
			DrawCircle(Self.position.x, Self.position.y, 10)
			SetColor(255, 255, 255)
		End
		DrawImage(Self.image, Self.position.x, Self.position.y, -Self.position.heading, 0.2, 0.2)
	End
	
End