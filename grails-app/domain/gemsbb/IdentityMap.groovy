package gemsbb

import grails.gorm.annotation.Entity
import grails.rest.*
import org.bson.types.ObjectId

@Entity
@Resource(readOnly = false, formats = ['json', 'xml'])
class IdentityMap {
    ObjectId id
    String tool
    String entityType
    ObjectId key
    String externalKey
    //Company company

    static constraints = {
        tool nullable: false
        entityType nullable: false
        key nullable: false
        externalKey nullable: false
    }

    static mapping = {
        tool index: false
        entityType index: false
        key index: false
        externalKey index: false
        version false
    }
}