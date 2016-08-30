package gemsbb

import grails.gorm.annotation.Entity
import grails.rest.*
import org.bson.types.ObjectId

@Entity
@Resource(readOnly = false, formats = ['json', 'xml'])
class Trace {
    ObjectId id
    Project project
    List<TaskTrace> taskTraces

    static embedded = ['taskTraces']

    static constraints = {
        project nullable: false
    }

    static mapping = {
        version false
    }
}
