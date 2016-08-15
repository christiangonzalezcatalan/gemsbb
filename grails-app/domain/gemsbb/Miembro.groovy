package gemsbb

import grails.gorm.annotation.Entity
import grails.rest.*
import org.bson.types.ObjectId

@Entity
@Resource(readOnly = false, formats = ['json', 'xml'])
class Miembro {
    ObjectId id
    String nombre
    String email

    static constraints = {
        nombre nullable: false
        email nullable: false
    }

    static mapping = {
        version false
    }
}