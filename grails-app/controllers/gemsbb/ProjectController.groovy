package gemsbb

import static org.springframework.http.HttpStatus.CREATED
import static org.springframework.http.HttpStatus.NOT_FOUND
import static org.springframework.http.HttpStatus.OK

import grails.rest.*
import grails.converters.*
import grails.transaction.Transactional
import org.bson.types.ObjectId

@Transactional(readOnly = true)
class ProjectController {
    static responseFormats = ['json']
    static allowedMethods = [save: "POST", update: "PUT", index: "GET"]

    def index(Integer max) {
        def queryParams = params
        queryParams.max = Math.min(max ?: 10, 100)
        def query = Project.where {}

        if(params.toolName != null) {
            query = query.where {
                toolsConfiguration.toolName == queryParams.toolName
            }
        }

        if(params.organizationId != null) {
            query = query.where {
                organization == new ObjectId(params.organizationId)
            }
        }

        if(params.processName != null) {
            query = query.where {
                toolsConfiguration.processNames == queryParams.processName
            }
        }

        respond  query.findAll(), model:[projectCount: query.count()]
    }

    def show(Project project) {
        respond project
    }

    @Transactional
    def save(Project project) {
        if (project == null) {
            transactionStatus.setRollbackOnly()
            render status: NOT_FOUND
            return
        }

        if (project.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond project.errors, view:'create'
            return
        }

        project.save flush:true

        respond project, [status: CREATED, view:"show"]
    }

    @Transactional
    def update(Project project) {
        if (project == null) {
            transactionStatus.setRollbackOnly()
            render status: NOT_FOUND
            return
        }

        if (project.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond project.errors, view:'edit'
            return
        }

        project.save flush:true

        respond project, [status: OK, view:"show"]
    }

    @Transactional
    def delete(Project project) {

        if (project == null) {
            transactionStatus.setRollbackOnly()
            render status: NOT_FOUND
            return
        }

        project.delete flush:true

        render status: NO_CONTENT
    }
}
