Import Math
Import dronetournament

Class Particle
	Field position:Vec2D
	Field past_position:Vec2D
	Field size:Float
	Field power:Float
	Field speed:Float
	Field angle:Float
	Field lifetime:Int
	Field team:Int
	
	Method New(pos:Vec2D, size:Float, power:Float, angle:Float, speed:Float, unit_team:Int, life_time:Int=30)
		Self.position = New Vec2D(pos.x, pos.y)
		Self.past_position = New Vec2D(pos.x, pos.y)
		Self.size = size
		Self.power = power
		Self.speed = speed
		Self.angle = angle
		Self.lifetime = life_time
		Self.team = unit_team
	End
	
	Method Draw()
		SetColor(55 * Self.team, 0, 255)
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