package gemsbb


import grails.rest.*
import grails.converters.*

class ProjectController extends RestfulController<Project> {
    static responseFormats = ['json']

    ProjectController() {
        super(Project)
    }

    private findByExternalKey(String tool, String externalKey) {
        Project project = null
        IdentityMap imap = IdentityMap.findByToolAndEntityTypeAndExternalKey(tool, 'Project', externalKey)
        if(imap != null)
        {
            project = Project.get(imap.key)
        }
        project
    }

    def search() {
        def result = []
        if(params.externalKey != null && params.tool != null) {
            result = findByExternalKey(params.tool, params.externalKey)
        }
        respond result
    }
}
