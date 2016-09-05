package gemsbb

import static org.springframework.http.HttpStatus.CREATED
import static org.springframework.http.HttpStatus.NOT_FOUND
import static org.springframework.http.HttpStatus.OK

import grails.rest.*
import grails.converters.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class NotAssignedWorkMetricController {
    static responseFormats = ['json']
    static allowedMethods = [save: "POST", update: "PUT", index: "GET"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond NotAssignedWorkMetric.list(params), model:[notAssignedWorkMetricCount: NotAssignedWorkMetric.count()]
    }

    def show(NotAssignedWorkMetric metric) {
        respond metric
    }

    @Transactional
    def save(NotAssignedWorkMetric metric) {
        if (metric == null) {
            transactionStatus.setRollbackOnly()
            render status: NOT_FOUND
            return
        }

        if (metric.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond metric.errors, view:'create'
            return
        }

        metric.save flush:true

        respond metric, [status: CREATED, view:"show"]
    }

    @Transactional
    def update(NotAssignedWorkMetric metric) {
        if (metric == null) {
            transactionStatus.setRollbackOnly()
            render status: NOT_FOUND
            return
        }

        if (metric.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond metric.errors, view:'edit'
            return
        }

        metric.save flush:true

        respond metric, [status: OK, view:"show"]
    }

    @Transactional
    def delete(NotAssignedWorkMetric metric) {

        if (metric == null) {
            transactionStatus.setRollbackOnly()
            render status: NOT_FOUND
            return
        }

        metric.delete flush:true

        render status: NO_CONTENT
    }

    private getMembersWork(json) {
        if(json != null) {
            return json.collect() {
                new NotAssignedMemberWork(
                    member: Member.get(it.member.id),
                    totalHours: it.totalHours
                )
            }
        }
        []
    }

    private getProjectsWork(json) {
        if(json != null) {
            return json.collect() {
                new NotAssignedProjectWork(
                    project: Project.get(it.project.id),
                    totalHours: it.totalHours
                )
            }
        }
        []
    }

    private getDetails(json) {
        if(json != null) {
            return json.collect() {
                new NotAssignedWorkDetail(
                    project: Project.get(it.project.id),
                    member: Member.get(it.member.id),
                    hours: it.hours
                )
            }
        }
        []
    }

    private def getProjects(json) {
        if(json != null) {
            return json.collect() {
                Project.get(it.id)
            }
        }
        []
    }

    private def getMembers(json) {
        if(json != null) {
            return json.collect() {
                Member.get(it.id)
            }
        }
        []
    }

    private setProperties(NotAssignedWorkMetric metric, json) {
        metric.project = Project.get(json.project.id)
        metric.projects = getProjects(json.projects)
        metric.members = getMembers(json.members)
        metric.year = json.year
        metric.month = json.month

        if(json.notAssignedWork != null) {
            metric.notAssignedWork = json.notAssignedWork.collect() {
                new NotAssignedWork(totalHours: it.totalHours,
                    membersCount: it.membersCount,
                    projectsCount: it.projectsCount,
                    notAssignedMembersWork: getMembersWork(it.notAssignedMembersWork),
                    notAssignedProjectsWork: getProjectsWork(it.notAssignedProjectsWork),
                    details: getDetails(it.details)
                )
            }
        }
    }

    /*@Transactional
    def save() {
        NotAssignedWorkMetric metric = new NotAssignedWorkMetric()
        def json = request.JSON
        setProperties(metric, json)

        if (metric.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond metric.errors, view:'create'
            return
        }

        println "Métrica json: ${json}"
        println "Métrica: ${metric.project}"

        metric.save(flush:true)

        respond(metric, [status: CREATED, view:"show"])
    }*/
}
