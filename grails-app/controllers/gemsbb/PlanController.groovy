package gemsbb

import static org.springframework.http.HttpStatus.CREATED
import static org.springframework.http.HttpStatus.NOT_FOUND
import static org.springframework.http.HttpStatus.OK

import grails.rest.*
import grails.converters.*
import grails.transaction.Transactional
import org.bson.types.ObjectId

@Transactional(readOnly = true)
class PlanController {
    static responseFormats = ['json']
    static allowedMethods = [save: "POST", update: "PUT", index: "GET"]

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

        respond plan, [status: CREATED, view:"show"]
    }

    @Transactional
    def update(Plan plan) {
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
    }

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

    /*def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Plan.list(params), model:[planCount: Plan.count()]
    }

    def show(Plan plan) {
        respond plan
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
        def json = request.JSON
        setProperties(plan, request.JSON)

        if (plan.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond plan.errors, view:'create'
            return
        }

        plan.save(flush:true)

        // Si se crea al cargar desde una herramienta
        if(json.tool != null && json.externalKey != null) {
            IdentityMap imap = new IdentityMap(
                    tool: json.tool,
                    externalKey: json.externalKey.toString(),
                    entityType: 'Plan',
                    key: plan.id
            )
            imap.save(flush: true)
        }

        respond(plan, [status: CREATED, view:"show"])
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

        setProperties(plan, request.JSON)

        if (plan.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond plan.errors, view:'edit'
            return
        }

        plan.save(flush: true)

        respond(plan, [status: OK, view:"show"])
    }

    private findByExternalKey(String tool, String externalKey) {
        Plan plan = null
        IdentityMap imap = IdentityMap.findByToolAndEntityTypeAndExternalKey(tool, 'Plan', externalKey)
        if(imap != null)
        {
            plan = Plan.get(imap.key)
        }
        plan
    }

    def search() {
        def result = new ArrayList<Plan>()
        if(params.externalKey != null && params.tool != null) {
            def findResult = findByExternalKey(params.tool, params.externalKey)
            if(findResult != null) {
                result.add(findResult)
            }
        }
        respond result
    }*/
}
