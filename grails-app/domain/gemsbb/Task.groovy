package gemsbb


import grails.rest.*

@Resource(readOnly = false, formats = ['json', 'xml'])
class Task {
    String name
    Date startDate
    Date dueDate
    String status
    Member responsible
    static hasMany = [contributors: Member]

    static constraints = {
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