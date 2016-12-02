Import Mojo
Import Math
Import brl.json
Import particle
Import unit
Import unit_type

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


	Method Draw(player_id:String, game_state:String)
		For Local key:Int = Eachin Self.units.Keys
			Local current_unit:Unit = Self.units.Get(key)
			If (current_unit.armor > 0)
				current_unit.DrawStatic(player_id, game_state)
			End
		End
		For Local part:Particle = Eachin Self.particles
			part.Draw()
		End
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

			Local new_unit:Unit = New Unit(unit_json.GetInt("id"), 
											unit_json.GetFloat("x"), 
											unit_json.GetFloat("y"), 
											unit_json.GetFloat("heading"), 
											unit_type, 
											unit_json.GetInt("player_id"), 
											unit_json.GetInt("player_id"),
											unit_json.GetFloat("energy"))
			new_unit.armor = unit_json.GetInt("armor")
			Self.units.Add(new_unit.unit_id, new_unit)
		End
		
		For Local i:Int = 0 Until player_list.Length
			Local player_json:JsonObject = JsonObject(player_list.Get(i))
			Local current_player_id:String = String(player_json.GetInt("player_id"))
			Local current_player_state:String = player_json.GetString("player_state")
			If (current_player_id = player_id)
				Self.player_state = current_player_state
				Exit
			End
		End
		
		For Local i:Int = 0 Until particle_list.Length
			Local particle_json:JsonObject = JsonObject(particle_list.Get(i))
			
			Local new_particle:Particle = New Particle(New Vec2D(particle_json.GetFloat("x"), particle_json.GetFloat("y")),
													    2.5,
													    particle_json.GetFloat("power"),
													    particle_json.GetFloat("heading"),
													    particle_json.GetFloat("speed"),
													    particle_json.GetInt("team"),
													    particle_json.GetInt("lifetime"))
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
			current_unit.position.Set(unit_json.GetFloat("x"), unit_json.GetFloat("y"))
			current_unit.heading = unit_json.GetFloat("heading")
			current_unit.SetServerControl(unit_json.GetFloat("control_x"), unit_json.GetFloat("control_y"), GAME_WIDTH, GAME_HEIGHT)
			current_unit.armor = unit_json.GetInt("armor")
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
					Local move:Vec2D = New Vec2D(move_json.GetFloat("x"), move_json.GetFloat("y"), move_json.GetFloat("heading"))
					
					Self.units.Get(key).points.PushLast(move)
				End
				Local last_point:Vec2D = Self.units.Get(key).points.Get(Self.units.Get(key).points.Length - 1)
				Self.units.Get(key).control.position.Set(last_point.x, last_point.y, last_point.heading)
			End
		End
	End
	
	Method BuildMoveJson:String(player_id:String)
		Print "Prepping move json"
		Local first:Int = 1
		Local move_json:String = "{ ~qdata~q : { ~qplayer_id~q: " + player_id + ", "
		move_json += "~qmoves~q : [ "
	
		For Local unit_id:Int = Eachin Self.units.Keys
			
			If (Self.units.Get(unit_id).player_id = player_id)
				If first = 1
					first = 0
				Else
					move_json += ", "
				End
				move_json += "{ ~qunit_id~q: " + Self.units.Get(unit_id).unit_id + ", "
				move_json += "~qx~q: " + Self.units.Get(unit_id).position.x + ", "
				move_json += "~qy~q: " + Self.units.Get(unit_id).position.y + ", "
				move_json += "~qcontrol-x~q: " + Self.units.Get(unit_id).control.position.x + ", "
				move_json += "~qcontrol-y~q: " + Self.units.Get(unit_id).control.position.y + ", "
				move_json += "~qcontrol-heading~q: " + Self.units.Get(unit_id).control.position.heading + ", " 
				move_json += "~qheading~q: " + Self.units.Get(unit_id).heading 
				move_json += " }"
			End
		End
		move_json += " ] } }"
		
		Return move_json		
	End
	
	
End