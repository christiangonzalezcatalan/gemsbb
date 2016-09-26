package gemsbb

import grails.gorm.annotation.Entity
import grails.rest.*
import org.bson.types.ObjectId

@Entity
@Resource(readOnly = false, formats = ['json', 'xml'])
class MappingDetail {
    ObjectId internalId
    String entityType
    String externalId

    static constraints = {
        entityType nullable: false
        externalId nullable: false
        internalId nullable: false
    }

    static mapping = {
        version false
    }
}
