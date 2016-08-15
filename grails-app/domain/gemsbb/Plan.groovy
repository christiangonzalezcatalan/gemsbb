package gemsbb

import grails.gorm.annotation.Entity
import grails.rest.*
import org.bson.types.ObjectId

@Entity
@Resource(readOnly = false, formats = ['json', 'xml'])
class Plan {
    ObjectId id
    Proyecto proyecto
    List<Tarea> tareas

    static embedded = ['tareas']

    static constraints = {
        proyecto nullable: false
        tareas nullable: false
    }

    static mapping = {
        version false
    }
}