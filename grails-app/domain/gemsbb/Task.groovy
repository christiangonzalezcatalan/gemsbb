package gemsbb


import grails.rest.*
import org.bson.types.ObjectId
import grails.gorm.annotation.Entity

@Resource(readOnly = false, formats = ['json', 'xml'])
class Task {
    ObjectId taskId
    String name
    Date startDate
    Date dueDate
    String status
    Member responsible
    static hasMany = [contributors: Member]

    static constraints = {
        taskId nullable: false
        name nullable: false
        startDate nullable: false
        dueDate nullable: false
        status nullable: false
        responsible nullable: false
    }

    static mapping = {
        version false
    }
}
