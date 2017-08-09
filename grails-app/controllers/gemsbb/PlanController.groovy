package gemsbb

import static org.springframework.http.HttpStatus.CREATED
import static org.springframework.http.HttpStatus.NOT_FOUND
import static org.springframework.http.HttpStatus.OK

import grails.rest.*
import grails.converters.*
import grails.transaction.Transactional
import org.bson.types.ObjectId

import com.budjb.rabbitmq.publisher.RabbitMessagePublisher

@Transactional(readOnly = true)
class PlanController {
    static responseFormats = ['json']
    static allowedMethods = [save: "POST", update: "PUT", index: "GET"]
    RabbitMessagePublisher rabbitMessagePublisher

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        if(params.projectId != null) {
            def result = Plan.findAllByProject(new ObjectId(params.projectId))
            respond result, model:[planCount: result.size()]
        }
        else {
            respond Plan.list(params), model:[planCount: Plan.count()]
        }
    }

    def show(Plan plan) {
        respond plan
    }

    @Transactional
    def save(Plan plan) {
        if (plan == null) {
            transactionStatus.setRollbackOnly()
            render status: NOT_FOUND
            return
        }

        if (plan.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond plan.errors, view:'create'
            return
        }

        plan.save flush:true

        println "Evento: Plan modificado"
        rabbitMessagePublisher.send {
            routingKey = 'Plan.update'
            exchange = 'testGemsBBExchange'
            body = plan.project.id.toString()
        }

        respond plan, [status: CREATED, view:"show"]
    }

    private setProperties(Plan plan, json) {
        plan.project = Project.get(json.project.id)
        if(json.tasks != null) {
            plan.tasks = json.tasks.collect() {
                new Task(taskId: new ObjectId(it.taskId),
                        name: it.name,
                        startDate: it.startDate,
                        dueDate: it.dueDate,
                        status: it.status,
                        responsible: Member.get(it.responsible.id[0]),
                        contributors: it.contributors?.collect() {
                            Member.get(it.id)
                        }
                )
            }
        }
    }

    private hasChanged(Plan plan, json) {
        if(json.tasks == null && plan.tasks == null) {
            return false
        }
        if((json.tasks == null && plan.tasks != null) || (json.tasks != null && plan.tasks == null)) {
            return true
        }
        if(json.tasks.size() != plan.tasks.size()) {
            return true
        }

        Boolean result = false
        json.tasks.each() {
            def jsonTask = it
            def currentTask = plan.tasks.find() { it.taskId.toString() == jsonTask.taskId }

            if(currentTask == null) {
                result = true
                return
            }
            if(jsonTask.name != currentTask.name || jsonTask.status != currentTask.status) {
                result = true
                return
            }
            if(Date.parse("yyyy-MM-dd'T'HH:mm:ss'Z'", jsonTask.startDate) != currentTask.startDate || 
                    Date.parse("yyyy-MM-dd'T'HH:mm:ss'Z'", jsonTask.dueDate) != currentTask.dueDate) {
                result = true
                return
            }
            if(jsonTask.responsible.id[0] != currentTask.responsible.id.toString()) {
                result = true
                return
            }

            /*if(jsonTask.traceDetails == null && currentTrace.traceDetails == null) {
                result = true
                return
            }

            if((jsonTask.traceDetails == null) != (currentTrace.traceDetails == null)) {
                result = true
                return
            }
            if(jsonTask.traceDetails.size() != currentTrace.traceDetails.size()) {
                result = true
                return
            }
            jsonTask.traceDetails.each() {
                def jsonDetail = it
                def d = currentTrace.traceDetails.find() { 
                    it.date == Date.parse("yyyy-MM-dd'T'HH:mm:ss'Z'", jsonDetail.date) && 
                    it.member.id.toString() == jsonDetail.member.id && 
                    it.hours == jsonDetail.hours }
                if(d == null) {
                    result = true
                    return
                }
            }*/

        }

        result
    }

    @Transactional
    def update() {
        def json = request.JSON
        Plan plan = Plan.get(json.id)

        if (plan == null) {
            transactionStatus.setRollbackOnly()
            render status: NOT_FOUND
            return
        }

        if(hasChanged(plan, request.JSON)) {
            setProperties(plan, request.JSON)

            if (plan.hasErrors()) {
                transactionStatus.setRollbackOnly()
                respond plan.errors, view:'edit'
                return
            }

            plan.save(flush: true)

            println "Evento: Plan modificado"
            rabbitMessagePublisher.send {
                routingKey = 'Plan.update'
                exchange = 'testGemsBBExchange'
                body = plan.project.id.toString()
            }
        }
        else {
            println "Plan sin cambios."
        }

        respond plan, [status: OK, view:"show"]
    }
    /*def update(Plan plan) {
        if (plan == null) {
            transactionStatus.setRollbackOnly()
            render status: NOT_FOUND
            return
        }

        if (plan.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond plan.errors, view:'edit'
            return
        }

        plan.save flush:true

        respond plan, [status: OK, view:"show"]
    }*/

    @Transactional
    def delete(Plan plan) {

        if (plan == null) {
            transactionStatus.setRollbackOnly()
            render status: NOT_FOUND
            return
        }

        plan.delete flush:true

        render status: NO_CONTENT
    }
}
