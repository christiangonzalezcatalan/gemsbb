package gemsbb

import grails.rest.Resource
import grails.persistence.Entity

@Entity
@Resource(readOnly = false, formats = ['json', 'xml'])
class Project {
    String name
    static constraints = {
        name nullable: false
    }
}