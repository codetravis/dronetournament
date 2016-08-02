Import brl.httprequest
Import brl.json

Class MultiplayerService Implements IOnHttpRequestComplete
	
	Field server_url:String
	Field raw_response:String
	Field response:JsonObject
	Field http_status:String
	Field server_request:HttpRequest
	
	Method New(url:String)
		Self.server_url = url
		ClearResponse()
	End
	
	Method GetRequest:Void(path:String)
		ClearResponse()
		server_request = New HttpRequest("GET", server_url + path, Self)
		server_request.Send
	End
	
	Method PostRequest:Void(path:String)
		ClearResponse()
		server_request = New HttpRequest("POST", server_url + path, Self)
		server_request.Send
	End
	
	Method PostJsonRequest:Void(path:String, json:String)
		ClearResponse()
		server_request = New HttpRequest("POST", server_url + path, Self)
		server_request.Send(json, "application/json")
	End
	
	Method OnHttpRequestComplete:Void(request:HttpRequest)
		Print request.ResponseText()
		Print request.Status()

		Self.http_status = request.Status()
		Self.raw_response = request.ResponseText()
		Try 
			Self.response = New JsonObject(request.ResponseText())
		Catch json_error:Throwable
			Print "Server unreachable or gave a non JSON response"
			Self.response = New JsonObject("{ ~qaction~q: ~qBad Server Response~q }")
		End
	End
	
	Method HasRequestFinished:Bool()
		If (Self.http_status <> "")
			Return True
		Else
			Return False
		End
	End
	
	Method ClearResponse:Void()
		Self.http_status = ""
		Self.raw_response = ""
		Self.response = New JsonObject()
	End
End