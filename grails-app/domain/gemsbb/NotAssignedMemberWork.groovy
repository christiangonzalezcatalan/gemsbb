package gemsbb

import grails.gorm.annotation.Entity
import grails.rest.*
import org.bson.types.ObjectId

@Entity
@Resource(readOnly = false, formats = ['json', 'xml'])
class NotAssignedMemberWork {
    Member member
    Integer totalHours

    static constraints = {
        member nullable: false
        totalHours nullable: false
    }

    static mapping = {
        version false
    }
}
