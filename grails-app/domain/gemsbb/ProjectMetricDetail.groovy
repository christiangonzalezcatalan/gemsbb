package gemsbb

import grails.gorm.annotation.Entity
import grails.rest.*
import org.bson.types.ObjectId

@Entity
@Resource(readOnly = false, formats = ['json', 'xml'])
class ProjectMetricDetail {
    Project project
    Member member
    Date date
    Map metricData

    static constraints = {
        project nullable: false
        member nullable:false
        date nullable:false
        metricData nullable: false
    }

    static mapping = {
        version false
    }
}
