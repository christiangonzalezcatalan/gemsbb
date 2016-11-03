package gemsbb

import grails.gorm.annotation.Entity
import grails.rest.*
import org.bson.types.ObjectId

@Entity
@Resource(readOnly = false, formats = ['json', 'xml'])
class Project {
    ObjectId id
    Organization organization
    String name
    List<ToolConfiguration> toolsConfiguration

    static embedded = ['toolsConfiguration']

    static constraints = {
        name nullable: false
        organization nullable: false
        toolsConfiguration nullable: true
    }

    static mapping = {
        version false
    }
}