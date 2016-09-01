package gemsbb

import grails.gorm.annotation.Entity
import grails.rest.*
import org.bson.types.ObjectId

@Entity
@Resource(readOnly = false, formats = ['json', 'xml'])
class NotAssignedProjectWork {
    Project project
    Integer totalHours

    static constraints = {
        project nullable: false
        totalHours nullable: false
    }

    static mapping = {
        version false
    }
}
