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
        "/projects/$id"(controller:'project', [PUT:'update', GET:'show'])
        "/projects/search"(controller: "project", action: "search")

        "/plans"(resources:'plan')
        "/plans/search"(controller: "plan", action: "search")
        "/plans/$id"(controller:'plan', action:[PUT:'update', GET:'show'])

        "/traces"(resources:'trace')
        "/traces/search"(controller: "trace", action: "search")
        "/traces/$id"(controller:'trace', action:[PUT:'update', GET:'show'])

        "/members"(resources:'member', controller:'member', action:[GET: 'index', POST: 'save'])
        "/members/$id"(controller:'member', [PUT:'update', GET:'show'])
        "/members/search"(controller: "member", action: "search")

        "/notAssignedWorkMetrics"(resources: 'notAssignedWorkMetric', controller:'notAssignedWorkMetric', action:[GET: 'index', POST: 'save'])
        "/notAssignedWorkMetrics/$id"(controller:'notAssignedWorkMetric', [PUT:'update', GET:'show'])
        //"/notAssignedWorkMetric/search"(controller: "member", action: "search")

        "/prueba"(resources:'prueba')

        "/"(controller: 'application', action:'index')
        "500"(view: '/error')
        "404"(view: '/notFound')
    }
}
