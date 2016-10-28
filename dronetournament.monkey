Import Mojo
Import Math
Import brl.json
Import user
Import user_interface
Import unit
Import particle

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

Function NewPoint:Vec2D (start_point:Vec2D, start_angle:Float, goal_angle:Float, max_angle_change:Float, distance:Float)

	Local new_angle:Float = 0.0
	
	If (start_angle < 0)
		start_angle = (start_angle Mod 360) + 360
	Else
		start_angle = start_angle Mod 360
	End
	
	If (goal_angle < 0)
		goal_angle = (goal_angle Mod 360) + 360
	Else
		goal_angle = goal_angle Mod 360
	End
	
	
	If (start_angle > goal_angle)
		If (start_angle - goal_angle < 180)
			new_angle = start_angle - Min((start_angle - goal_angle), max_angle_change)
		Else
			new_angle = start_angle + max_angle_change
		End
	Else If (start_angle < goal_angle)
		If (goal_angle - start_angle < 180)
			new_angle = start_angle + Min((goal_angle - start_angle), max_angle_change)
		Else
			new_angle = start_angle - max_angle_change
		End
	Else
		new_angle = start_angle
	End

	Return New Vec2D(start_point.x + distance * Cosr(new_angle * (PI/180)), start_point.y + distance * Sinr(new_angle * (PI/180)), new_angle)

End

Function CounterClockwise:Bool(pointOne:Vec2D, pointTwo:Vec2D, pointThree:Vec2D)
	Return ((pointThree.y - pointOne.y) * (pointTwo.x - pointOne.x) > (pointTwo.y - pointOne.y) * (pointThree.x - pointOne.x)) 
End

Function LinesIntersect:Bool(pointA:Vec2D, pointB:Vec2D, pointC:Vec2D, pointD:Vec2D)
	' To determine if 2 lines intersect there is this sneaky way of doing it
	' where you look at the counterclockwiseness of 3 points at a time
	Local abc:Bool = CounterClockwise(pointA, pointB, pointC)
	Local abd:Bool = CounterClockwise(pointA, pointB, pointD)
	Local cda:Bool = CounterClockwise(pointC, pointD, pointA)
	Local cdb:Bool = CounterClockwise(pointC, pointD, pointB)

	Return(( abc <> abd) And (cda <> cdb))
End

Function Collided(particle:Particle, unit:Unit)

	If (unit.armor <= 0)
		Return False
	Else If (particle.team = unit.team)
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


