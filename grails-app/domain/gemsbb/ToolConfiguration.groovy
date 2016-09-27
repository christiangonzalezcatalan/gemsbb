package gemsbb

import grails.gorm.annotation.Entity
import grails.rest.*
import org.bson.types.ObjectId

@Entity
@Resource(readOnly = false, formats = ['json', 'xml'])
class ToolConfiguration {
    ObjectId id
    Project project
    String toolName
    List<String> processNames
    Map parameters

    static constraints = {
        project nullable: false
        processNames nullable: false
    }

    static mapping = {
        version false
    }
}
