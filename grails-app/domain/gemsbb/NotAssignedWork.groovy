package gemsbb

import grails.gorm.annotation.Entity
import grails.rest.*
import org.bson.types.ObjectId

@Entity
@Resource(readOnly = false, formats = ['json', 'xml'])
class NotAssignedWork {
    List<NotAssignedMemberWork> notAssignedMembersWork
    List<NotAssignedProjectWork> notAssignedProjectsWork
    List<NotAssignedWorkDetail> details
    Integer totalHours
    Integer membersCount
    Integer projectsCount

    static embedded = ['notAssignedMembersWork', 'notAssignedProjectsWork', 'details']

    static constraints = {
    }

    static mapping = {
        version false
    }
}
