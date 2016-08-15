package gemsbb

import grails.gorm.annotation.Entity
import grails.rest.*

@Entity
@Resource(readOnly = false, formats = ['json', 'xml'])
class Miembro {
    String nombre
    String email
    static constraints = {
        nombre nullable: false
        email nullable: false
    }
}