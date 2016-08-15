package gemsbb


import grails.rest.*

@Resource(readOnly = false, formats = ['json', 'xml'])
class Tarea {
    String nombre
    Date fechaInicio
    Date fechaFin
    String estado
    static hasOne = [responsable: Miembro]
    static hasMany = [colaboradores: Miembro]

    static constraints = {
        fechaInicio nullable: false
        fechaFin nullable: false
        estado nullable: false
        responsable nullable: false
    }

    static mapping = {
        version false
    }
}