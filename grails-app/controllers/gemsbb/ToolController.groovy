package gemsbb

import static org.springframework.http.HttpStatus.CREATED
import static org.springframework.http.HttpStatus.NOT_FOUND
import static org.springframework.http.HttpStatus.OK

import grails.rest.*
import grails.converters.*
import grails.transaction.Transactional
import org.bson.types.ObjectId

@Transactional(readOnly = true)
class ToolController {
    static responseFormats = ['json']
    static allowedMethods = [save: "POST", update: "PUT", index: "GET"]

    def index(Integer max) {
        def queryParams = params
        queryParams.max = Math.min(max ?: 10, 100)

        def query = Tool.where {
        }

        if(queryParams.name != null) {
            query = query.where {
                name == queryParams.name
            }
        }

        respond query.findAll(), model:[toolCount: query.count()]
    }

    def show(Tool tool) {
        respond tool
    }

    @Transactional
    def save(Tool tool) {
        if (tool == null) {
            transactionStatus.setRollbackOnly()
            render status: NOT_FOUND
            return
        }

        if (tool.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond tool.errors, view:'create'
            return
        }

        tool.save flush:true

        respond tool, [status: CREATED, view:"show"]
    }

    @Transactional
    def update(Tool tool) {
        if (tool == null) {
            transactionStatus.setRollbackOnly()
            render status: NOT_FOUND
            return
        }

        if (tool.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond tool.errors, view:'edit'
            return
        }

        tool.save flush:true

        respond tool, [status: OK, view:"show"]
    }

    @Transactional
    def delete(Tool tool) {

        if (tool == null) {
            transactionStatus.setRollbackOnly()
            render status: NOT_FOUND
            return
        }

        tool.delete flush:true

        render status: NO_CONTENT
    }
}
