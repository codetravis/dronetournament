Import Mojo
Import Math

Const SCREEN_WIDTH:Int = 640
Const SCREEN_HEIGHT:Int = 480


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

Class Player
	Field position:Vec2D
	Field velocity:Vec2D
	Field control:ControlPoint
	Field maxVelocity:Float
	Field minVelocity:Float
	Field maxRotation:Float
	Field moveXPoints:FloatDeque
	Field moveYPoints:FloatDeque
	Field Points:Deque<Vec2D>
	Field heading:Float
	Field friendly:Int
	Field maxEnergy:Float
	Field currentEnergy:Float
	Field chargeEnergy:Float
	Field armor:Int

	Method New(x:Float, y:Float, initial_heading:Float, max_velocity_limit:Float, rotationLimit:Float, armor:Int, isfriendly:Int)
		Self.position = New Vec2D(x, y)
		Self.control = New ControlPoint(x + max_velocity_limit, y, 10, 10)
		Self.maxVelocity = max_velocity_limit
		Self.minVelocity = maxVelocity * 0.5
		Self.maxRotation = rotationLimit
		Self.heading = initial_heading
		Self.velocity = New Vec2D(maxVelocity * Cosr(heading * (PI/180)), maxVelocity * Sinr(heading * (PI/180)))
		Self.SetControl(velocity.x, velocity.y, SCREEN_WIDTH, SCREEN_HEIGHT)
		Self.friendly = isfriendly
		Self.maxEnergy = 100.0
		Self.currentEnergy = 0.0
		Self.chargeEnergy = 5.0
		Self.armor = armor
	End

	Method DrawStatic()
		If (friendly)
			SetColor(128, 255, 128)
		Else
			SetColor(255, 128, 128)
		End
		DrawRect(position.x, position.y, 15, 15)
		control.Draw()
		For Local i:Int = 0 Until Points.Length - 1
			If ((currentEnergy + i * chargeEnergy) Mod 100 = 0)
				SetColor(100, 100, 255)
			Else
				SetColor(255, 255, 255)
			End
			Local this_point:Vec2D = Points.Get(i)
			DrawPoint(this_point.x, this_point.y)
		End
		If (friendly)
			SetColor(0, 255, 0)
			DrawText(heading, 0, 0)
		Else
			SetColor(255, 0, 0)
		End
		DrawLine(position.x, position.y, position.x + velocity.x, position.y + velocity.y)
	End
	
	Method Update()
		Local next_point:Vec2D = Points.PopFirst()

		heading = ATan2((next_point.y - position.y), (next_point.x - position.x))
		velocity.Set(maxVelocity * Cosr(heading * (PI/180)), maxVelocity * Sinr(heading * (PI/180)))
		position = next_point
		currentEnergy = Min(100.0, currentEnergy + chargeEnergy)
	End


	Method ControlSelected(click_x:Float, click_y:Float)
		If (control.selected)
			Return True
		Else If ((click_x >= control.position.x - control.width) And
			(click_x <= (control.position.x + control.width)) And
			(click_y >= control.position.y - control.width) And
			(click_y <= (control.position.y + control.height)))
			control.selected = True
			Return True
		Else
			Return False
		End
	End
	
	Method ControlReleased()
		control.selected = False
	End
	
	Method SetControl(click_x:Float, click_y:Float, map_width:Float, map_height:Float)
		Local goal_angle = ATan2((click_y - position.y), (click_x - position.x))
		Local start_angle = Self.heading
		Local control_pos:Vec2D = New Vec2D(position.x, position.y, Self.heading)
		Points = New Deque<Vec2D>
		
		For Local i:Int = 0 Until 30
			control_pos = NewPoint(control_pos, start_angle, goal_angle, maxRotation, maxVelocity/30.0)
			start_angle = control_pos.heading
			goal_angle = ATan2((click_y - control_pos.y), (click_x - control_pos.x))
			Points.PushLast(control_pos)
		End
		
		If (control_pos.x > map_width)
			control_pos.x = map_width - 10
		Else If (control_pos.x < 0)
			control_pos.x = 10
		End
		
		If (control_pos.y > map_height)
			control_pos.y = map_height - 10
		Else If (control_pos.y < 0)
			control_pos.y = 10
		End
		
		control.position.Set(control_pos.x, control_pos.y)
	End
	
	Method FireWeapon()
		currentEnergy = 0
	End
	
	Method TakeDamage()
		Self.armor = Self.armor - 1
	End
End

Class ControlPoint
	Field position:Vec2D
	Field width:Float
	Field height:Float
	Field selected:Bool

	Method New(x:Float, y:Float, w:Float, h:Float)
		position = New Vec2D(x, y)
		width = w
		height = h
		selected = False
	End
	
	Method Draw()
		SetColor(255, 255, 128)
		DrawRect(position.x, position.y, width, height)
	End
	
End


Class Particle
	Field position:Vec2D
	Field past_position:Vec2D
	Field size:Float
	Field power:Float
	Field speed:Float
	Field angle:Float
	Field lifetime:Int
	Field friendly:Int
	
	Method New(pos:Vec2D, si:Float, pow:Float, ang:Float, sp:Float, friendly:Int)
		Self.position = New Vec2D(pos.x, pos.y)
		Self.past_position = New Vec2D(pos.x, pos.y)
		Self.size = si
		Self.power = pow
		Self.speed = sp
		Self.angle = ang
		Self.lifetime = 30
		Self.friendly = friendly
	End
	
	Method Draw()
		SetColor(0, 0, 255)
		DrawCircle(position.x - size, position.y - size, size)
		DrawLine(past_position.x, past_position.y, position.x, position.y)
	End
	
	Method Update()
		Local posx:Float = position.x
		Local posy:Float = position.y
		past_position.Set(posx, posy)
		position.Set(position.x + speed * Cosr(angle * (PI/180)), position.y + speed * Sinr(angle * (PI/180)))
		lifetime = lifetime - 1
	End
	
End

Function NewPoint:Vec2D (start_point:Vec2D, start_angle:Float, goal_angle:Float, max_angle_change:Float, distance:Float)

	Local new_angle:Float
	If ((start_angle >= 0 And goal_angle >= 0) Or (start_angle < 0 And goal_angle < 0))
		If (start_angle > goal_angle)
			new_angle = start_angle - Min((start_angle - goal_angle), max_angle_change)
		Else If (start_angle < goal_angle)
			new_angle = start_angle + Min((goal_angle - start_angle), max_angle_change)
		Else
			new_angle = start_angle
		End
	Else If (start_angle >= 0 And goal_angle < 0)
		If (start_angle - goal_angle > 180)
			new_angle = start_angle + max_angle_change
		Else
			new_angle = start_angle - Min((start_angle - goal_angle), max_angle_change)
		End
	Else If (start_angle < 0 And goal_angle >= 0)
		If (goal_angle - start_angle > 180)
			new_angle = start_angle - max_angle_change
		Else
			new_angle = start_angle + Min((goal_angle - start_angle), max_angle_change)
		End	
	End

	If (Abs(new_angle - start_angle) > max_angle_change)
		Print("Something went wrong -- New Angle: " + new_angle + " Start Angle: " + start_angle)
	End

	Return New Vec2D(start_point.x + distance * Cosr(new_angle * (PI/180)), start_point.y + distance * Sinr(new_angle * (PI/180)), new_angle)

End

