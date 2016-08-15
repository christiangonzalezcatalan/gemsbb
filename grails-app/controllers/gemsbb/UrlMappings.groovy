package gemsbb

class UrlMappings {

    static mappings = {
        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }

        "/proyectos"(resources:'proyecto')
        "/planes"(resources:'plan')
        "/miembros"(resources:'miembro')

        "/"(controller: 'application', action:'index')
        "500"(view: '/error')
        "404"(view: '/notFound')
    }
}
