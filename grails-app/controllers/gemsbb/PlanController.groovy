package gemsbb

import static org.springframework.http.HttpStatus.CREATED
import static org.springframework.http.HttpStatus.NOT_FOUND
import static org.springframework.http.HttpStatus.NOT_FOUND
import static org.springframework.http.HttpStatus.OK

import grails.rest.*
import grails.converters.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class PlanController {
    static responseFormats = ['json']
    static allowedMethods = [save: "POST", update: "PUT", index: "GET"]


    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Member.list(params), model:[memberCount: Member.count()]
    }

    private setProperties(Plan plan, json) {
        plan.project = Project.get(json.project.id)
        if(json.tasks != null) {
            plan.tasks = json.tasks.collect() {
                new Task(name: it.name,
                        startDate: it.startDate,
                        dueDate: it.dueDate,
                        status: it.status,
                        responsible: Member.get(it.responsible.id),
                        contributors: it.contributors?.collect() {
                            Member.get(it.id)
                        }
                )
            }
        }
    }

    @Transactional
    def save() {
        Plan plan = new Plan()
        setProperties(plan, request.JSON)

        if (plan.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond plan.errors, view:'create'
            return
        }

        plan.save(flush:true)

        respond(plan, [status: CREATED, view:"show"])
    }

    @Transactional
    def update(Member member) {
        if (member == null) {
            transactionStatus.setRollbackOnly()
            render status: NOT_FOUND
            return
        }

        if (member.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond member.errors, view:'edit'
            return
        }

        member.save(flush: true)

        respond(member, [status: OK, view:"show"])
    }
}