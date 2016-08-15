package gemsbb

import grails.gorm.annotation.Entity
import grails.rest.*
import org.bson.types.ObjectId

@Entity
@Resource(readOnly = false, formats = ['json', 'xml'])
class Proyecto {
    ObjectId id
    String nombre

    static constraints = {
        nombre nullable: false
    }

    static mapping = {
        version false
    }
}