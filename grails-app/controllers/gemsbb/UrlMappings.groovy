package gemsbb

class UrlMappings {

    static mappings = {
        "/projects"(resources:'project') {
            "/mappings"(resources:'mapping')
            //"/toolsConfiguration"(resources:'toolConfiguration')
        }
        "/toolsConfiguration"(resources:'toolConfiguration')

        "/plans"(resources:'plan')
        "/organizations"(resources:'organization') {
            "/repositories"(resources:'repository')
        }
        "/projectMetrics"(resources:'projectMetric')
        "/traces"(resources:'trace')
        "/members"(resources:'member')
        "/notAssignedWorkMetrics"(resources: 'notAssignedWorkMetric')

        "/"(controller: 'application', action:'index')
        "500"(view: '/error')
        "404"(view: '/notFound')
    }
}
