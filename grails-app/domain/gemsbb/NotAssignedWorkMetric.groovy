package gemsbb

import grails.gorm.annotation.Entity
import grails.rest.*
import org.bson.types.ObjectId

@Entity
@Resource(readOnly = false, formats = ['json', 'xml'])
class NotAssignedWorkMetric {
    ObjectId id
    Integer month
    Integer year
    Project project
    List<Project> projects
    List<Member> members
    List<NotAssignedWork> notAssignedWork

    static embedded = ['notAssignedWork', 'projects', 'members']

    static constraints = {
    }

    static mapping = {
        version false
    }
}
