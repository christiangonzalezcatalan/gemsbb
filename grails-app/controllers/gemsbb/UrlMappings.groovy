package gemsbb

class UrlMappings {

    static mappings = {
        /*"/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }*/

        "/projects"(resources:'project')
        "/plans"(resources:'plan')
        "/members"(resources:'member')
        "/members/search"(controller: "member", action: "search")
        "/projects/search"(controller: "project", action: "search")

        "/"(controller: 'application', action:'index')
        "500"(view: '/error')
        "404"(view: '/notFound')
    }
}
