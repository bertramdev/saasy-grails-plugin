class UrlMappings {

	static mappings = {

        "/api/$typeName/$command/$id?(.$format)?"(controller:'saasyPluginTest', action:'command') {
        }


        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }

        "/"(view:"/index")
        "500"(view:'/error')
	}
}
