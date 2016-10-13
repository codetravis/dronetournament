Import Mojo
Import Math
Import brl.json
Import user
Import user_interface

Const SCREEN_WIDTH:Int = 640
Const SCREEN_HEIGHT:Int = 480

Class Unit
	Field unit_id:Int
	Field player_id:String
	Field position:Vec2D
	Field velocity:Vec2D
	Field control:ControlPoint
	Field moveXPoints:FloatDeque
	Field moveYPoints:FloatDeque
	Field points:Deque<Vec2D>
	Field heading:Float
	Field team:Int
	Field currentEnergy:Float
	Field armor:Int
	Field unit_type:UnitType

	Method New(unit_id:Int, x:Float, y:Float, initial_heading:Float, unit_type:UnitType, player_id:String, team_number:Int, current_energy:Float=0.0)
		Self.unit_id = unit_id
		Self.player_id = player_id
		Self.unit_type = unit_type

		Self.position = New Vec2D(x, y)
		Self.control = New ControlPoint(x + Self.unit_type.maxVelocity, y, initial_heading, 10, 10)

		Self.heading = initial_heading
		Self.velocity = New Vec2D(Self.unit_type.maxVelocity * Cosr(heading * (PI/180)), Self.unit_type.maxVelocity * Sinr(heading * (PI/180)))
		Self.SetControl(velocity.x, velocity.y, SCREEN_WIDTH, SCREEN_HEIGHT)
		Self.team = team_number

		Self.currentEnergy = current_energy

		Self.armor = Self.unit_type.maxArmor
	End

	Method DrawStatic(game_player_id:String, game_state:String)
		If (Self.player_id = game_player_id)
			SetColor(128, 255, 128)
		Else
			SetColor(255, 128, 128)
		End
		
		DrawImage(Self.unit_type.image, Self.position.x, Self.position.y, -Self.heading, 1, 1)

		If (Self.player_id = game_player_id And (game_state = "multiplayer" Or game_state = "tutorial" Or game_state = "end_turn"))
			Self.control.Draw()

			For Local i:Int = 0 Until Self.points.Length - 1
				If ((Self.currentEnergy + i * Self.unit_type.chargeEnergy) Mod Self.unit_type.maxEnergy = 0)
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
		Self.currentEnergy = Min(Self.unit_type.maxEnergy, Self.currentEnergy + Self.unit_type.chargeEnergy)
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
			control_pos.heading = 180
		Else If (control_pos.x < 0)
			control_pos.x = 10
			control_pos.heading = 0
		End
		
		If (control_pos.y > map_height)
			control_pos.y = map_height - 10
			control_pos.heading = 270
		Else If (control_pos.y < 0)
			control_pos.y = 10
			control_pos.heading = 90
		End
		
		Self.control.position.Set(control_pos.x, control_pos.y, control_pos.heading)
	End
	
	Method SetServerControl(click_x:Float, click_y:Float, map_width:Float, map_height:Float)
		Local goal_angle = ATan2((click_y - Self.position.y), (click_x - Self.position.x))
		Local start_angle:Float = Self.heading
		Local control_pos:Vec2D = New Vec2D(Self.position.x, Self.position.y, Self.heading)
		Self.points = New Deque<Vec2D>
		
		For Local i:Int = 0 Until 30
			control_pos = NewPoint(control_pos, start_angle, goal_angle, Self.unit_type.maxRotation, Self.unit_type.maxVelocity/30.0)
			start_angle = control_pos.heading
			goal_angle = ATan2((click_y - control_pos.y), (click_x - control_pos.x))
			Self.points.PushLast(control_pos)
		End
		
		Self.control.position.Set(control_pos.x, control_pos.y, control_pos.heading)	
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

Class UnitType
	Field id:Int
	Field name:String
	Field maxVelocity:Float
	Field maxRotation:Float
	Field maxEnergy:Float
	Field chargeEnergy:Float
	Field maxArmor:Float
	Field image:Image
	
	Method New(type_json:JsonObject)
		Self.id = type_json.GetInt("id")
		Self.name = type_json.GetString("name")
		Self.maxVelocity = Float(type_json.GetFloat("speed"))
		Self.maxRotation = Float(type_json.GetFloat("turn"))
		Self.maxEnergy = Float(type_json.GetFloat("full_energy"))
		Self.chargeEnergy = Float(type_json.GetFloat("charge_energy"))
		Self.maxArmor = Float(type_json.GetInt("armor"))
		
		Local image_name:String = type_json.GetString("image_name")
		Self.image = LoadImage("images/" + image_name, 1, Image.MidHandle)
	End
End

Class Game
	Field id:String
	Field units:IntMap<Unit>
	Field opponents:List<Unit>
	Field particles:List<Particle>
	Field types:IntMap<UnitType>
	Field player_state:String
	
	Method New()
		Self.units = New IntMap<Unit>()
		Self.opponents = New List<Unit>()
		Self.particles = New List<Particle>()
		Self.types = New IntMap<UnitType>()
	End
	
	Method LoadFromJson(game_json:JsonObject, player_id:String)
		Self.units.Clear()
		Self.opponents.Clear()
		Self.particles.Clear()
		Self.types.Clear()

		Self.id = String(game_json.GetInt("id"))
		Local unit_list:JsonArray = JsonArray(game_json.Get("units"))
		Local types_list:JsonArray = JsonArray(game_json.Get("types"))
		Local player_list:JsonArray = JsonArray(game_json.Get("players"))
		Local particle_list:JsonArray = JsonArray(game_json.Get("particles"))
		
		For Local i:Int = 0 Until types_list.Length
			Local type_json:JsonObject = JsonObject(types_list.Get(i))
			Local new_type:UnitType = New UnitType(type_json)
			Self.types.Add(new_type.id, new_type)
		End
		
		For Local i:Int = 0 Until unit_list.Length
			Local unit_json:JsonObject = JsonObject(unit_list.Get(i))
			Local type_id:Int = unit_json.GetInt("unit_type_id")
			Local unit_type:UnitType = Self.types.Get(type_id)

			Local new_unit:Unit = New Unit(Int(unit_json.GetInt("id")), 
											Float(unit_json.GetFloat("x")), 
											Float(unit_json.GetFloat("y")), 
											Float(unit_json.GetFloat("heading")), 
											unit_type, 
											Int(unit_json.GetInt("player_id")), 
											Int(unit_json.GetInt("player_id")),
											Float(unit_json.GetFloat("energy")))
			new_unit.armor = Float(unit_json.GetInt("armor"))
			Self.units.Add(new_unit.unit_id, new_unit)
			Print "new unit " + new_unit.unit_id + " should be added" 
		End
		
		For Local i:Int = 0 Until player_list.Length
			Local player_json:JsonObject = JsonObject(player_list.Get(i))
			Local current_player_id:String = String(player_json.GetInt("player_id"))
			Local current_player_state:String = player_json.GetString("player_state")
			If (current_player_id = player_id)
				Self.player_state = current_player_state
				Print "setting game player state to " + current_player_state
				Exit
			End
		End
		
		For Local i:Int = 0 Until particle_list.Length
			Local particle_json:JsonObject = JsonObject(particle_list.Get(i))
			Local current_particle_id:String = particle_json.GetInt("id")
			
			Local new_particle:Particle = New Particle(New Vec2D( Float(particle_json.GetFloat("x")), Float(particle_json.GetFloat("y")) ),
													    2.5,
													    Float(particle_json.GetFloat("power")),
													    Float(particle_json.GetFloat("heading")),
													    Float(particle_json.GetFloat("speed")),
													    Int(particle_json.GetInt("team")),
													    Int(particle_json.GetInt("lifetime")) )
			new_particle.past_position.Set(new_particle.position.x - new_particle.speed * Cosr(new_particle.angle * (PI/180)), 
										    new_particle.position.y - new_particle.speed * Sinr(new_particle.angle * (PI/180)))
			Self.particles.AddLast(new_particle)
		End
		
	End
	
	Method LoadServerMoves(game_json:JsonObject)
		Local unit_list:JsonArray = JsonArray(game_json.Get("units"))
		For Local i:Int = 0 Until unit_list.Length
			Local unit_json:JsonObject = JsonObject(unit_list.Get(i))
			Local current_unit:Unit = Self.units.Get(unit_json.GetInt("id"))
			current_unit.position.Set(Float(unit_json.GetFloat("x")), Float(unit_json.GetFloat("y")))
			current_unit.heading = Float(unit_json.GetFloat("heading"))
			current_unit.SetServerControl(Float(unit_json.GetFloat("control_x")), Float(unit_json.GetFloat("control_y")), SCREEN_WIDTH, SCREEN_HEIGHT)
			current_unit.armor = Int(unit_json.GetInt("armor"))
			Self.units.Set(unit_json.GetInt("id"), current_unit)
		End
	End
	
	Method SetUnitPathsToServerSimulation(server_json:JsonObject, player_id:String)
		Local moves_json:JsonObject = JsonObject(server_json.Get("move_points"))
		For Local key:Int = Eachin Self.units.Keys 
			If (Self.units.Get(key).player_id = player_id)
				Self.units.Get(key).points = New Deque<Vec2D>
				Local moves_array:JsonArray = JsonArray(moves_json.Get(String(key)))
				For Local i:Int = 0 Until moves_array.Length
					Local move_json:JsonObject = JsonObject(moves_array.Get(i))
					Local move:Vec2D = New Vec2D(Float(move_json.GetFloat("x")), Float(move_json.GetFloat("y")), Float(move_json.GetFloat("heading")))
					
					Self.units.Get(key).points.PushLast(move)
				End
				Local last_point:Vec2D = Self.units.Get(key).points.Get(Self.units.Get(key).points.Length - 1)
				Self.units.Get(key).control.position.Set(last_point.x, last_point.y, last_point.heading)
			End
		End
	End
	
End
