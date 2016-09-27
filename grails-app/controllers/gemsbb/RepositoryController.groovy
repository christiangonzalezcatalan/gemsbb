package gemsbb

import static org.springframework.http.HttpStatus.CREATED
import static org.springframework.http.HttpStatus.NOT_FOUND
import static org.springframework.http.HttpStatus.OK

import grails.rest.*
import grails.converters.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class RepositoryController {
static responseFormats = ['json']
    static allowedMethods = [save: "POST", update: "PUT", index: "GET"]

    def index(Integer max) {
        def queryParams = params
        queryParams.max = Math.min(max ?: 10, 100)

        println queryParams.organizationId

        def query = Repository.where {
           organization == Organization.get(queryParams.organizationId)
        }

        if(queryParams.name != null) {
            query = query.where {
                toolName == queryParams.toolName
            }
        }

        respond  query.findAll(), model:[repositoryCount: query.count()]
    }

    def show(Repository repository) {
        respond repository
    }

    @Transactional
    def save(Repository repository) {
        if (repository == null) {
            transactionStatus.setRollbackOnly()
            render status: NOT_FOUND
            return
        }

        repository.organization = Organization.get(params.organizationId)

        if (!repository.validate()) {
            transactionStatus.setRollbackOnly()
            respond repository.errors, view:'create'
            return
        }

        repository.save flush:true

        respond repository, [status: CREATED, view:"show"]
    }

    @Transactional
    def update(Repository repository) {
        if (repository == null) {
            transactionStatus.setRollbackOnly()
            render status: NOT_FOUND
            return
        }

        repository.organization = Organization.get(params.organizationId)

        if (!repository.validate()) {
            transactionStatus.setRollbackOnly()
            respond repository.errors, view:'edit'
            return
        }

        repository.save flush:true

        respond repository, [status: OK, view:"show"]
    }
}
