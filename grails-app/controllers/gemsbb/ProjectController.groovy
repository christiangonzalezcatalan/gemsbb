package gemsbb


import grails.rest.*
import grails.converters.*

class ProjectController extends RestfulController {
    static responseFormats = ['json', 'xml']
    ProjectController() {
        super(Project)
    }
}
