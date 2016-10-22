package gemsbb

import grails.gorm.annotation.Entity
import grails.rest.*
import org.bson.types.ObjectId

@Entity
@Resource(readOnly = false, formats = ['json', 'xml'])
class Tool {
    ObjectId id
    String name
    Integer repeatInterval

    static constraints = {
        name nullable: false
        repeatInterval nullable: true
    }

    static mapping = {
        version false
    }
}
