Import Mojo
Import Math
Import brl.json
Import user

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

Class Unit
	Field unit_id:Int
	Field position:Vec2D
	Field velocity:Vec2D
	Field control:ControlPoint
	Field moveXPoints:FloatDeque
	Field moveYPoints:FloatDeque
	Field points:Deque<Vec2D>
	Field heading:Float
	Field friendly:Int
	Field currentEnergy:Float
	Field armor:Int
	Field unit_type:UnitType

	Method New(unit_id:Int, x:Float, y:Float, initial_heading:Float, unit_type:UnitType, isfriendly:Int)
		Self.unit_id = unit_id
		Self.unit_type = unit_type

		Self.position = New Vec2D(x, y)
		Self.control = New ControlPoint(x + Self.unit_type.maxVelocity, y, 10, 10)

		Self.heading = initial_heading
		Self.velocity = New Vec2D(Self.unit_type.maxVelocity * Cosr(heading * (PI/180)), Self.unit_type.maxVelocity * Sinr(heading * (PI/180)))
		Self.SetControl(velocity.x, velocity.y, SCREEN_WIDTH, SCREEN_HEIGHT)
		Self.friendly = isfriendly

		Self.currentEnergy = 0.0

		Self.armor = Self.unit_type.maxArmor
	End

	Method DrawStatic()
		If (Self.friendly)
			SetColor(128, 255, 128)
		Else
			SetColor(255, 128, 128)
		End
		
		DrawImage(Self.unit_type.image, Self.position.x, Self.position.y, -Self.heading, 1, 1)
		DrawRect(Self.position.x - 10, Self.position.y - 10, 20, 20)

		If (Self.friendly = 1)
			Self.control.Draw()

			For Local i:Int = 0 Until Self.points.Length - 1
				If ((Self.currentEnergy + i * Self.unit_type.chargeEnergy) Mod 100 = 0)
					SetColor(100, 100, 255)
				Else
					SetColor(255, 255, 255)
				End
				Local this_point:Vec2D = Self.points.Get(i)
				DrawPoint(this_point.x, this_point.y)
			End
		End
	End
	
	Method Update()
		Local next_point:Vec2D = Self.points.PopFirst()

		Self.heading = ATan2((next_point.y - Self.position.y), (next_point.x - Self.position.x))
		Self.velocity.Set(Self.unit_type.maxVelocity * Cosr(heading * (PI/180)), Self.unit_type.maxVelocity * Sinr(Self.heading * (PI/180)))
		Self.position = next_point
		Self.currentEnergy = Min(100.0, Self.currentEnergy + Self.unit_type.chargeEnergy)
	End


	Method ControlSelected(click_x:Float, click_y:Float)
		If (Self.control.selected)
			Return True
		Else If ((click_x >= Self.control.position.x - Self.control.width) And
			(click_x <= (Self.control.position.x + Self.control.width)) And
			(click_y >= Self.control.position.y - Self.control.width) And
			(click_y <= (Self.control.position.y + Self.control.height)))
			Self.control.selected = True
			Return True
		Else
			Return False
		End
	End
	
	Method ControlReleased()
		Self.control.selected = False
	End
	
	Method SetControl(click_x:Float, click_y:Float, map_width:Float, map_height:Float)
		Local goal_angle = ATan2((click_y - Self.position.y), (click_x - Self.position.x))
		Local start_angle = Self.heading
		Local control_pos:Vec2D = New Vec2D(Self.position.x, Self.position.y, Self.heading)
		Self.points = New Deque<Vec2D>
		
		For Local i:Int = 0 Until 30
			control_pos = NewPoint(control_pos, start_angle, goal_angle, Self.unit_type.maxRotation, Self.unit_type.maxVelocity/30.0)
			start_angle = control_pos.heading
			goal_angle = ATan2((click_y - control_pos.y), (click_x - control_pos.x))
			Self.points.PushLast(control_pos)
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
		
		Self.control.position.Set(control_pos.x, control_pos.y)
	End
	
	Method FireWeapon()
		Self.currentEnergy = 0
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

	Method New(x:Float, y:Float, width:Float, height:Float)
		Self.position = New Vec2D(x, y)
		Self.width = width
		Self.height = height
		Self.selected = False
	End
	
	Method Draw()
		SetColor(255, 255, 128)
		DrawRect(Self.position.x, Self.position.y, Self.width, Self.height)
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
	
	Method New(pos:Vec2D, size:Float, power:Float, angle:Float, speed:Float, friendly:Int)
		Local newx = pos.x + 10 * Cosr(angle * (PI/180))
		Local newy = pos.y + 10 * Sinr(angle * (PI/180))
		Self.position = New Vec2D(pos.x, pos.y)
		Self.past_position = New Vec2D(pos.x, pos.y)
		Self.size = size
		Self.power = power
		Self.speed = speed
		Self.angle = angle
		Self.lifetime = 30
		Self.friendly = friendly
	End
	
	Method Draw()
		
		SetColor(255 * friendly, 0, 255)
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

Class UnitType
	Field name:String
	Field maxVelocity:Float
	Field maxRotation:Float
	Field maxEnergy:Float
	Field chargeEnergy:Float
	Field maxArmor:Float
	Field image:Image
	
	Method New(type_json:JsonObject)
		Self.name = type_json.GetString("name")
		Self.maxVelocity = Float(type_json.GetString("speed"))
		Self.maxRotation = Float(type_json.GetString("turn"))
		Self.maxEnergy = Float(type_json.GetString("full_energy"))
		Self.chargeEnergy = Float(type_json.GetString("charge_energy"))
		Self.maxArmor = Float(type_json.GetString("armor"))
		
		Local image_name:String = type_json.GetString("image")
		Self.image = LoadImage("images/" + image_name, 1, Image.MidHandle)
	End
End

Class Game
	Field units:StringMap<Unit>
	Field opponents:List<Unit>
	Field particles:List<Particle>
	Field types:StringMap<UnitType>
	
	Method New()
		Self.units = New StringMap<Unit>()
		Self.opponents = New List<Unit>()
		Self.particles = New List<Particle>()
		Self.types = New StringMap<UnitType>()
	End
	
	Method LoadFromJson(game_json:JsonObject)
		Local unit_list:JsonArray = JsonArray(game_json.Get("units"))
		Local types_list:JsonArray = JsonArray(game_json.Get("types"))
		Local player_list:JsonArray = JsonArray(game_json.Get("players"))
		
		For Local i:Int = 0 Until types_list.Length
			Local type_json:JsonObject = JsonObject(types_list.Get(i))
			Local new_type:UnitType = New UnitType(type_json)
			Self.types.Add(new_type.name, new_type)
		End
		
		For Local i:Int = 0 Until unit_list.Length
			Local unit_json:JsonObject = JsonObject(unit_list.Get(i))
			Local type_name:String = unit_json.GetString("type")
			Local unit_type:UnitType = Self.types.Get(type_name)

			Local new_unit:Unit = New Unit(Int(unit_json.GetString("id")), 
											Float(unit_json.GetString("x")), 
											Float(unit_json.GetString("y")), 
											Float(unit_json.GetString("heading")), 
											unit_type, 1)
			Self.units.Add(new_unit.unit_id, new_unit)
		End 
		
	End
	
	
End
