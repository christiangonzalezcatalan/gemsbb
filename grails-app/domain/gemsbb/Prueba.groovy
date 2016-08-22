package gemsbb

import org.bson.types.ObjectId

class Prueba {
    ObjectId id
    String name

    static constraints = {
        name nullable: false
    }

    static mapping = {
        version false
    }
}
