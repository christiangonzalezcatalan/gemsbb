package gemsbb

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class PruebaController {

    static responseFormats = ['json', 'xml']
    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Prueba.list(params), model:[pruebaCount: Prueba.count()]
    }

    def show(Prueba prueba) {
        respond prueba
    }

    @Transactional
    def save(Prueba prueba) {
        if (prueba == null) {
            transactionStatus.setRollbackOnly()
            render status: NOT_FOUND
            return
        }

        if (prueba.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond prueba.errors, view:'create'
            return
        }

        prueba.save flush:true

        respond prueba, [status: CREATED, view:"show"]
    }

    @Transactional
    def update(Prueba prueba) {
        if (prueba == null) {
            transactionStatus.setRollbackOnly()
            render status: NOT_FOUND
            return
        }

        if (prueba.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond prueba.errors, view:'edit'
            return
        }

        prueba.save flush:true

        respond prueba, [status: OK, view:"show"]
    }

    @Transactional
    def delete(Prueba prueba) {

        if (prueba == null) {
            transactionStatus.setRollbackOnly()
            render status: NOT_FOUND
            return
        }

        prueba.delete flush:true

        render status: NO_CONTENT
    }
}
