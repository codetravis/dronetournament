Import Mojo
Import Math

Const SCREEN_WIDTH:Int = 640
Const SCREEN_HEIGHT:Int = 480


Class Vec2D
	Field x:Float
	Field y:Float
	
	Method New(x:Float=0, y:Float=0)
		Set(x,y)
	End
	
	Method Set(x:Float, y:Float)
		Self.x = x
		Self.y = y
	End
End

Class Player
	Field position:Vec2D
	Field velocity:Vec2D
	Field control:ControlPoint
	Field maxVelocity:Float
	Field maxRotation:Float
	Field moveXPoints:FloatDeque
	Field moveYPoints:FloatDeque
	Field Points:Deque<Vec2D>
	Field heading:Float

	Method New(x:Float, y:Float, initial_heading:Float, max_velocity_limit:Float, rotationLimit:Float)
		position = New Vec2D(x, y)
		control = New ControlPoint(x + max_velocity_limit, y)
		maxVelocity = max_velocity_limit
		maxRotation = rotationLimit
		heading = initial_heading
		velocity = New Vec2D(maxVelocity * Cosr(heading * (PI/180)), maxVelocity * Sinr(heading * (PI/180)))
		SetControl(velocity.x, velocity.y)
	End

	Method DrawStatic()
		SetColor(128, 255, 128)
		DrawRect(position.x, position.y, 15, 15)
		control.Draw()
		For Local i:Int = 0 Until Points.Length - 1
			SetColor(255, 255, 255)
			Local this_point:Vec2D = Points.Get(i)
			DrawPoint(this_point.x, this_point.y)
		End
		SetColor(0, 255, 0)
		DrawLine(position.x, position.y, position.x + velocity.x, position.y + velocity.y)
	End
	
	Method Update()
		Local next_point:Vec2D = Points.PopFirst()

		heading = ATan2((next_point.y - position.y), (next_point.x - position.x))
		velocity.Set(maxVelocity * Cosr(heading * (PI/180)), maxVelocity * Sinr(heading * (PI/180)))
		position = next_point
	End
	
	Method SetControl(click_x:Float, click_y:Float)

		Local goal_angle = ATan2((click_y - position.y), (click_x - position.x))
		Local start_angle = heading
		Local control_pos:Vec2D = New Vec2D(position.x, position.y)
		Points = New Deque<Vec2D>
		
		For Local i:Int = 0 Until 30
			control_pos = NewPoint(control_pos, start_angle, goal_angle, maxRotation, maxVelocity/30.0)
			If (start_angle > goal_angle)
				start_angle = start_angle - maxRotation
			Else If (start_angle < goal_angle)
				start_angle = start_angle + maxRotation
			End
			goal_angle = ATan2((click_y - control_pos.y), (click_x - control_pos.x))
			Points.PushLast(control_pos)
		End
		control.position.Set(control_pos.x, control_pos.y)
	End
End

Class ControlPoint
	Field position:Vec2D

	Method New(x:Float, y:Float)
		position = New Vec2D(x, y)
	End
	
	Method Draw()
		SetColor(255, 128, 128)
		DrawRect(position.x, position.y, 5, 5)
	End
	
End

Function CubicHermite:FloatDeque (start_point:Float, end_point:Float, start_velocity:Float, end_velocity:Float)
	Local pathPoints:FloatDeque = New FloatDeque
	Local division:Float = 30.0
	
	For Local i:Int = 0 Until division
		Local t:Float = (Float(i)/division)
		Local t_square:Float = t * t
		Local t_cube:Float = t_square * t
		
		Local a:Float = 2*t_cube - 3*t_square + 1
		Local b:Float = -2*t_cube + 3*t_square
		Local c:Float = t_cube - 2*t_square + t
		Local d:Float = t_cube - t_square 
		
		Local point:Float = a * start_point + b * end_point + c * start_velocity + d * end_velocity
		pathPoints.PushLast(point)
		
	End
	Return pathPoints
End

Function NewPoint:Vec2D (start_point:Vec2D, start_angle:Float, goal_angle:Float, max_angle_change:Float, distance:Float)

	Local new_angle:Float
	If (start_angle > goal_angle)
		new_angle = start_angle - max_angle_change
	Else If (start_angle < goal_angle)
		new_angle = start_angle + max_angle_change
	End

	Return New Vec2D(start_point.x + distance * Cosr(new_angle * (PI/180)), start_point.y + distance * Sinr(new_angle * (PI/180)))

End

