package gemsbb

import static org.springframework.http.HttpStatus.CREATED
import static org.springframework.http.HttpStatus.NOT_FOUND
import static org.springframework.http.HttpStatus.OK

import grails.rest.*
import grails.converters.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class MappingController {
    static responseFormats = ['json']
    static allowedMethods = [save: "POST", update: "PUT", index: "GET"]

    def index(Integer max) {
        def queryParams = params
        queryParams.max = Math.min(max ?: 10, 100)

        def query = Mapping.where {
           project == Project.get(queryParams.projectId)
        }

        if(params.tool != null) {
            query = query.where {
                tool == queryParams.tool
            }
        }

        if(params.entityType != null) {
            query = query.where {
                entityType == queryParams.entityType
            }
        }

        respond  query.findAll(), model:[mappingCount: query.count()]
    }

    def show(Mapping mapping) {
        respond mapping
    }

    @Transactional
    def save(Mapping mapping) {
        if (mapping == null) {
            transactionStatus.setRollbackOnly()
            render status: NOT_FOUND
            return
        }

        mapping.project = Project.get(params.projectId)

        if (!mapping.validate()) {
            transactionStatus.setRollbackOnly()
            respond mapping.errors, view:'create'
            return
        }

        mapping.save flush:true

        respond mapping, [status: CREATED, view:"show"]
    }

    @Transactional
    def update(Mapping mapping) {
        if (mapping == null) {
            transactionStatus.setRollbackOnly()
            render status: NOT_FOUND
            return
        }

        mapping.project = Project.get(params.projectId)

        if (!mapping.validate()) {
            transactionStatus.setRollbackOnly()
            respond mapping.errors, view:'edit'
            return
        }

        mapping.save flush:true

        respond mapping, [status: OK, view:"show"]
    }
}
