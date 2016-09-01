package gemsbb

import grails.gorm.annotation.Entity
import grails.rest.*
import org.bson.types.ObjectId

@Entity
@Resource(readOnly = false, formats = ['json', 'xml'])
class NotAssignedWorkDetail {
    Member member
    Project project
    Integer hours

    static constraints = {
        member nullable: false
        project nullable: false
        hours nullable: false
    }

    static mapping = {
        version false
    }
}
