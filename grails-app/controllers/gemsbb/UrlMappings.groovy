package gemsbb

class UrlMappings {

    static mappings = {
        /*"/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }*/

        //"/members"(resources:'member')
        // action:[GET:'get', PUT:'put'
        //"/members"(controller:"member", action:"index")
        //"/books/$id"(controller:"book", action:"show")
        //"/books/$id/edit"(controller:"book", action:"edit")
        //"/books/create"(controller:"book", action:"create")
        //"/books/$id"(controller:"book", action:"delete")

        "/projects"(resources:'project')
        "/projects/search"(controller: "project", action: "search")

        "/plans"(resources:'plan')
        "/plans/search"(controller: "plan", action: "search")
        "/plans/$id"(controller:'plan', action:[PUT:'update'])

        "/traces"(resources:'trace')
        "/traces/search"(controller: "trace", action: "search")
        "/traces/$id"(controller:'trace', action:[PUT:'update'])

        "/members"(controller:'member', action:[GET: 'index', POST: 'save'])
        "/members/$id"(controller:'member', action:'update')
        "/members/search"(controller: "member", action: "search")

        "/notAssignedWorkMetrics"(controller:'notAssignedWorkMetric', action:[GET: 'index', POST: 'save'])
        "/notAssignedWorkMetrics/$id"(controller:'notAssignedWorkMetric', action:'update')
        //"/notAssignedWorkMetric/search"(controller: "member", action: "search")

        "/prueba"(resources:'prueba')

        "/"(controller: 'application', action:'index')
        "500"(view: '/error')
        "404"(view: '/notFound')
    }
}
