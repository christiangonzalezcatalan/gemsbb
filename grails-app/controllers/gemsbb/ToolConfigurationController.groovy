package gemsbb

import static org.springframework.http.HttpStatus.CREATED
import static org.springframework.http.HttpStatus.NOT_FOUND
import static org.springframework.http.HttpStatus.OK

import grails.rest.*
import grails.converters.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class ToolConfigurationController {
    static responseFormats = ['json']
    static allowedMethods = [save: "POST", update: "PUT", index: "GET"]

    def index(Integer max) {
        def queryParams = params
        queryParams.max = Math.min(max ?: 10, 100)
        def query = ToolConfiguration.where {}

        if(queryParams.projectId != null) {
            query = query.where {
                project == Project.get(queryParams.projectId)
            }
        }

        if(params.toolName != null) {
            query = query.where {
                toolName == queryParams.toolName
            }
        }

        if(params.processName != null) {
            query = query.where {
                processNames == params.processName
            }
        }

        respond  query.findAll(), model:[toolConfigurationCount: query.count()]
    }

    def show(ToolConfiguration toolConfiguration) {
        respond toolConfiguration
    }

    @Transactional
    def save(ToolConfiguration toolConfiguration) {
        if (toolConfiguration == null) {
            transactionStatus.setRollbackOnly()
            render status: NOT_FOUND
            return
        }

        //toolConfiguration.project = Project.get(params.projectId)

        if (!toolConfiguration.validate()) {
            transactionStatus.setRollbackOnly()
            respond toolConfiguration.errors, view:'create'
            return
        }

        toolConfiguration.save flush:true

        respond toolConfiguration, [status: CREATED, view:"show"]
    }

    @Transactional
    def update(ToolConfiguration toolConfiguration) {
        if (toolConfiguration == null) {
            transactionStatus.setRollbackOnly()
            render status: NOT_FOUND
            return
        }

        //toolConfiguration.project = Project.get(params.projectId)

        if (!toolConfiguration.validate()) {
            transactionStatus.setRollbackOnly()
            respond toolConfiguration.errors, view:'edit'
            return
        }

        toolConfiguration.save flush:true

        respond toolConfiguration, [status: OK, view:"show"]
    }
}
