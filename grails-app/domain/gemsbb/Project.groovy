package gemsbb

import grails.gorm.annotation.Entity
import grails.rest.*
import org.bson.types.ObjectId

@Entity
@Resource(readOnly = false, formats = ['json', 'xml'])
class Project {
    ObjectId id
    String name

    static constraints = {
        name nullable: false
    }

    static mapping = {
        version false
    }
}