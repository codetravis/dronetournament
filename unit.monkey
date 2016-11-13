Import Mojo
Import Math
Import dronetournament
Import control_point
Import unit_type

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
		Local R:Int = 0
		Local G:Int = 0
		Local B:Int = 0
		If (Self.player_id = game_player_id)
			R = 128
			G = 255
			B = 128
		Else
			R = 255
			G = 128
			B = 128
		End
		
		SetColor(R, G, B)
		DrawImage(Self.unit_type.image, Self.position.x, Self.position.y, -Self.heading, 1, 1)
		For Local i = 0 Until Self.armor - 1
			SetColor(0, 0, 255)
			DrawRect(Self.position.x - 20 + (i * 5), Self.position.y + 28, 5, 3)
		End
		SetColor(R, G, B)
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
	
	Method TakeDamage(power:Int=1)
		Self.armor = Self.armor - power
	End
	
	
End