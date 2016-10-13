
Class User

	Field player_id:String
	Field username:String
	Field password:String
	
	Method New(username:String, player_id:String="0", password:String="")
		Self.username = username
		Self.player_id = player_id
		Self.password = password
	End

End
