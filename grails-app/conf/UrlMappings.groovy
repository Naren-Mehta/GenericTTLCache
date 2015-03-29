class UrlMappings {

    static mappings = {
        "/$controller/$action?/$id?" {
            constraints {
                // apply constraints here
            }
        }

        "/"(controller: "home", action: "landingPage")
        "500"(view: '/error')
    }
}
