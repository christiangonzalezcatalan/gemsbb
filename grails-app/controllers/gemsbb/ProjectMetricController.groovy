package gemsbb

import static org.springframework.http.HttpStatus.CREATED
import static org.springframework.http.HttpStatus.NOT_FOUND
import static org.springframework.http.HttpStatus.OK

import grails.rest.*
import grails.converters.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class ProjectMetricController {
    static responseFormats = ['json']
    static allowedMethods = [save: "POST", update: "PUT", index: "GET"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond ProjectMetric.list(params), model:[projectCount: ProjectMetric.count()]
    }

    def show(ProjectMetric projectMetric) {
        respond projectMetric
    }

    @Transactional
    def save(ProjectMetric projectMetric) {
        if (projectMetric == null) {
            transactionStatus.setRollbackOnly()
            render status: NOT_FOUND
            return
        }

        if (projectMetric.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond projectMetric.errors, view:'create'
            return
        }

        projectMetric.save flush:true

        respond projectMetric, [status: CREATED, view:"show"]
    }

    @Transactional
    def update(ProjectMetric projectMetric) {
        if (projectMetric == null) {
            transactionStatus.setRollbackOnly()
            render status: NOT_FOUND
            return
        }

        if (projectMetric.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond projectMetric.errors, view:'edit'
            return
        }

        projectMetric.save flush:true

        respond projectMetric, [status: OK, view:"show"]
    }
}
