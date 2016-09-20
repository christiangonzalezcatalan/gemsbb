package gemsbb

import static org.springframework.http.HttpStatus.CREATED
import static org.springframework.http.HttpStatus.NOT_FOUND
import static org.springframework.http.HttpStatus.OK

import grails.rest.*
import grails.converters.*
import grails.transaction.Transactional
import org.bson.types.ObjectId

@Transactional(readOnly = true)
class ProjectMetricController {
    static responseFormats = ['json']
    static allowedMethods = [save: "POST", update: "PUT", index: "GET"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)

        if(params.projectId != null &&params.name != null && params.month != null && params.year != null) {
            def rparams = params
            def query = ProjectMetric.where {
                project.id == new ObjectId(params.projectId) &&
                name == rparams.name &&
                month == Integer.parseInt(rparams.month) &&
                year == Integer.parseInt(rparams.year)
            }
            def result = query.findAll()
            respond result, model:[projectMetricCount: result.size()]
        }
        else {
            respond ProjectMetric.list(), model:[projectMetricCount: ProjectMetric.count()]
        }
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
