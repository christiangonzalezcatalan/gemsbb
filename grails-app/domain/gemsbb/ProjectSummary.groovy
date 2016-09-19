package gemsbb

import grails.gorm.annotation.Entity
import grails.rest.*
import org.bson.types.ObjectId

@Entity
@Resource(readOnly = false, formats = ['json', 'xml'])
class ProjectSummary {
    Project project
    Map metricData

    static embedded = ['project']

    static constraints = {
        project nullable: false
        metricData nullable: false
    }

    static mapping = {
        version false
    }
}
