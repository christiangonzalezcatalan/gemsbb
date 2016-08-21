package gemsbb

import grails.gorm.annotation.Entity
import grails.rest.*
import org.bson.types.ObjectId

@Entity
@Resource(readOnly = false, formats = ['json', 'xml'])
class Member {
    ObjectId id
    String name
    String email

    static constraints = {
        name nullable: false
        email nullable: false
    }

    static mapping = {
        version false
    }
}