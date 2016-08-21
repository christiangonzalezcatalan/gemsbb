package gemsbb

import grails.gorm.annotation.Entity
import grails.rest.*
import org.bson.types.ObjectId

@Entity
@Resource(readOnly = false, formats = ['json', 'xml'])
class Plan {
    ObjectId id
    Project project
    List<Task> tasks

    static embedded = ['tasks']

    static constraints = {
        project nullable: false
        tasks nullable: false
    }

    static mapping = {
        version false
    }
}