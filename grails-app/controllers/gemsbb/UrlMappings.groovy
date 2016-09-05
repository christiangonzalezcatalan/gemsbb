package gemsbb

class UrlMappings {

    static mappings = {
        "/projects"(resources:'project') {
            "/mappings"(resources:'mapping')
        }

        "/plans"(resources:'plan')
        "/traces"(resources:'trace')
        "/members"(resources:'member')
        "/notAssignedWorkMetrics"(resources: 'notAssignedWorkMetric')

        "/"(controller: 'application', action:'index')
        "500"(view: '/error')
        "404"(view: '/notFound')
    }
}
