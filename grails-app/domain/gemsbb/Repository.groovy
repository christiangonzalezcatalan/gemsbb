package gemsbb

import grails.gorm.annotation.Entity
import grails.rest.*
import org.bson.types.ObjectId

@Entity
@Resource(readOnly = false, formats = ['json', 'xml'])
class Repository {
    ObjectId id
    Organization organization
    String toolName
    Map data

    static constraints = {
        organization nullable: false
        toolName nullable: false
    }

    static mapping = {
        version false
    }
}
