Import Mojo
Import Math
Import brl.json

Class UnitType
	Field id:Int
	Field name:String
	Field maxVelocity:Float
	Field maxRotation:Float
	Field maxEnergy:Float
	Field chargeEnergy:Float
	Field maxArmor:Int
	Field image:Image
	
	Method New(type_json:JsonObject)

		Self.id = type_json.GetInt("id")
		Self.name = type_json.GetString("name")
		Self.maxVelocity = type_json.GetFloat("speed")
		Self.maxRotation = type_json.GetFloat("turn")
		Self.maxEnergy = type_json.GetFloat("full_energy")
		Self.chargeEnergy = type_json.GetFloat("charge_energy")
		Self.maxArmor = type_json.GetInt("armor")
		
		Local image_name:String = type_json.GetString("image_name")
		Self.image = LoadImage("images/" + image_name, 1, Image.MidHandle)

	End
End