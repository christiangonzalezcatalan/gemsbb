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
        "/members"(controller:'member', action:[GET: 'index', POST: 'save'])
        "/members/$id"(controller:'member', action:'update')

        //"/members"(resources:'member')
        // action:[GET:'get', PUT:'put'
        //"/members"(controller:"member", action:"index")
        //"/books/$id"(controller:"book", action:"show")
        //"/books/$id/edit"(controller:"book", action:"edit")
        //"/books/create"(controller:"book", action:"create")
        //"/books/$id"(controller:"book", action:"delete")

        "/members/search"(controller: "member", action: "search")
        "/projects/search"(controller: "project", action: "search")
        "/prueba"(resources:'prueba')

        "/"(controller: 'application', action:'index')
        "500"(view: '/error')
        "404"(view: '/notFound')
    }
}
