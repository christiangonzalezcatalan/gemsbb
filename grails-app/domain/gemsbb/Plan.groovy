package gemsbb

import grails.gorm.annotation.Entity
import grails.rest.*

@Entity
@Resource(readOnly = false, formats = ['json', 'xml'])
class Plan {
    Proyecto proyecto
    List<Tarea> tareas

    static embedded = { 'tareas' }

    static constraints = {
        proyecto nullable: false
        tareas nullable: false
    }
}