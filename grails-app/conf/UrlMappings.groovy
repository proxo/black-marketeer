class UrlMappings {
    static mappings = {
	  "/paper"(controller: "paperRest") {
		  action = [GET: "list", POST: "save"]
	  }
	  "/paper/$id"(controller: "paperRest") {
		  action = [GET: "show", PUT: "update", DELETE: "delete"]
		  constraints {
			  id(matches: /\d+/)
		  }
	  }
	  "/$controller/$action?/$id?"{
		  constraints {
			 // apply constraints here
		  }
	  }
	  "/login/$action?"(controller: "login")
	  "/logout/$action?"(controller: "logout")
      "/"(controller: 'home')
	  "500"(view:'/error')
	}
}