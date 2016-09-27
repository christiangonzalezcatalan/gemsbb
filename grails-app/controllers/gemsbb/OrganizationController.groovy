package gemsbb

import static org.springframework.http.HttpStatus.CREATED
import static org.springframework.http.HttpStatus.NOT_FOUND
import static org.springframework.http.HttpStatus.OK

import grails.rest.*
import grails.converters.*
import grails.transaction.Transactional
import org.bson.types.ObjectId

@Transactional(readOnly = true)
class OrganizationController {
    static responseFormats = ['json']
    static allowedMethods = [save: "POST", update: "PUT", index: "GET"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Organization.list(params), model:[organizationCount: Organization.count()]
    }

    def show(Organization organization) {
        respond organization
    }

    @Transactional
    def save(Organization organization) {
        if (organization == null) {
            transactionStatus.setRollbackOnly()
            render status: NOT_FOUND
            return
        }

        if (organization.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond organization.errors, view:'create'
            return
        }

        organization.save flush:true

        respond organization, [status: CREATED, view:"show"]
    }

    @Transactional
    def update(Organization organization) {
        if (organization == null) {
            transactionStatus.setRollbackOnly()
            render status: NOT_FOUND
            return
        }

        if (organization.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond organization.errors, view:'edit'
            return
        }

        organization.save flush:true

        respond organization, [status: OK, view:"show"]
    }

    @Transactional
    def delete(Organization organization) {

        if (organization == null) {
            transactionStatus.setRollbackOnly()
            render status: NOT_FOUND
            return
        }

        organization.delete flush:true

        render status: NO_CONTENT
    }
}
