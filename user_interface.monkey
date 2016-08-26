Import Mojo

Class Button

	Field image:Image
	Field position:Vec2D
	Field touch_box:TouchBox
	
	Method New(x:Float, y:Float, hit_x:Float, hit_y:Float, width:Float, height:Float, image:Image)
		Self.position = New Vec2D(x, y)
		Self.image = image
		Self.touch_box = New TouchBox(hit_x, hit_y, width, height)
	End
	
	Method Selected:Bool()
		Return (Self.touch_box.Selected(TouchX(0), TouchY(0)))
	End
	
	Method Draw()
		DrawImage(Self.image, Self.position.x, Self.position.y)
		' this is used when positioning button hit boxes on screen for now
		'DrawRect(Self.touch_box.position.x, Self.touch_box.position.y, Self.touch_box.width, Self.touch_box.height)
	End

End

Class TouchBox
	Field position:Vec2D
	Field width:Float
	Field height:Float
	
	Method New(x:Float, y:Float, width:Float, height:Float)
		Self.position = New Vec2D(x, y)
		Self.width = width
		Self.height = height
	End
	
	Method Selected:Bool(touch_x:Float, touch_y:Float)
		If ((touch_x >= Self.position.x) And
			(touch_x <= Self.position.x + Self.width) And
			(touch_y >= Self.position.y) And
			(touch_y <= Self.position.y + Self.height))
			Return True
		Else
			Return False
		End
	End
End

Class Vec2D
	Field x:Float
	Field y:Float
	Field heading:Float
	
	Method New(x:Float=0, y:Float=0, h:Float=0)
		Set(x,y,h)
	End
	
	Method Set(x:Float, y:Float, h:Float=0)
		Self.x = x
		Self.y = y
		Self.heading = h
	End
End

Class GameSelect
	Field touch_box:TouchBox
	Field game_id:String
	
	Method New(x:Float, y:Float, width:Float, height:Float, game_id:String)
		Self.game_id = game_id
		Self.touch_box = New TouchBox(x, y, width, height)
	End
	
	Method Selected:Bool()
		Return (Self.touch_box.Selected(TouchX(0), TouchY(0)))
	End
	
	Method Draw()
		SetColor(0, 200, 0)
		DrawRect(Self.touch_box.position.x, Self.touch_box.position.y, Self.touch_box.width, Self.touch_box.height)
		SetColor(255, 255, 255)
		DrawText(Self.game_id, Self.touch_box.position.x + 5, Self.touch_box.position.y + 5)
	End
End