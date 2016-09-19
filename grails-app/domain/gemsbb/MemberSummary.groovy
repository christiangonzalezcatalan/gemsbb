package gemsbb

import grails.gorm.annotation.Entity
import grails.rest.*
import org.bson.types.ObjectId

@Entity
@Resource(readOnly = false, formats = ['json', 'xml'])
class MemberSummary {
    Member member
    Map metricData

    static embedded = ['member']

    static constraints = {
        member nullable: false
        metricData nullable: false
    }

    static mapping = {
        version false
    }
}
