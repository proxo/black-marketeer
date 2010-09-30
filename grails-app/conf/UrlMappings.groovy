class UrlMappings {
    static mappings = {
    "/${appName}" (controller: "home"){
    	action = [GET: "list", POST: "save"]
     }
     
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
      "/"(controller: 'home')
	  "500"(view:'/error')
	}
}