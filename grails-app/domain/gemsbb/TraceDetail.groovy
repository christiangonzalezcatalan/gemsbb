package gemsbb

import grails.gorm.annotation.Entity
import grails.rest.*
import org.bson.types.ObjectId

@Entity
@Resource(readOnly = false, formats = ['json', 'xml'])
class TraceDetail {
    Member member
    Date date
    Integer hours

    static constraints = {
        member nullable: false
        date nullable: false
        hours nullable: false
    }

    static mapping = {
        version false
    }
}
