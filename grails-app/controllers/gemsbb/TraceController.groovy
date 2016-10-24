package gemsbb


import grails.rest.*
import grails.converters.*
import grails.transaction.Transactional
import org.bson.types.ObjectId

import static org.springframework.http.HttpStatus.CREATED
import static org.springframework.http.HttpStatus.NOT_FOUND
import static org.springframework.http.HttpStatus.OK

import com.budjb.rabbitmq.publisher.RabbitMessagePublisher

import groovy.json.JsonOutput

@Transactional(readOnly = true)
class TraceController {
    static responseFormats = ['json']
    static allowedMethods = [save: 'POST', update: 'PUT', index: 'GET']
    //@Autowired
    RabbitMessagePublisher rabbitMessagePublisher

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        if(params.projectId != null) {
            def result = Trace.findAllByProject(new ObjectId(params.projectId))
            respond result, model:[traceCount: result.size()]
        }
        else {
            respond Trace.list(params), model:[traceCount: Trace.count()]
        }
    }

    def show(Trace trace) {
        respond trace
    }

    private def getDetail(json) {
        json.collect() {
            new TraceDetail(member: Member.get(new ObjectId(it.member.id)),
                    date: Date.parse("yyyy-MM-dd'T'HH:mm:ss'Z'", it.date),
                    hours: it.hours)
        }
    }

    private setProperties(Trace trace, json) {
        trace.project = Project.get(json.project.id)
        if(json.taskTraces != null) {
            trace.taskTraces = json.taskTraces.collect() {
                new TaskTrace(
                    taskTraceId: it.taskTraceId,
                    name: it.name,
                    status: it.status,
                    traceDetails: getDetail(it.traceDetails)
                )
            }
        }
    }

    private hasChanged(Trace trace, json) {
        if(json.taskTraces == null && trace.taskTraces == null) {
            return false
        }
        if((json.taskTraces == null && trace.taskTraces != null) ||(json.taskTraces != null && trace.taskTraces == null)) {
            return true
        }
        if(json.taskTraces.size() != trace.taskTraces.size()) {
            return true
        }

        Boolean result = false
        json.taskTraces.each() {
            def jsonTrace = it
            
            def currentTrace = trace.taskTraces.find() { it.taskTraceId.toString() == jsonTrace.taskTraceId }
            if(currentTrace == null) {
                result = true
                return
            }
            if(jsonTrace.name != currentTrace.name || jsonTrace.status != currentTrace.status) {
                result = true
                return
            }

            if(jsonTrace.traceDetails == null && currentTrace.traceDetails == null) {
                result = true
                return
            }

            if((jsonTrace.traceDetails == null) != (currentTrace.traceDetails == null)) {
                result = true
                return
            }
            if(jsonTrace.traceDetails.size() != currentTrace.traceDetails.size()) {
                result = true
                return
            }
            jsonTrace.traceDetails.each() {
                def jsonDetail = it
                def d = currentTrace.traceDetails.find() { 
                    it.date == Date.parse("yyyy-MM-dd'T'HH:mm:ss'Z'", jsonDetail.date) && 
                    it.member.id.toString() == jsonDetail.member.id && 
                    it.hours == jsonDetail.hours }
                if(d == null) {
                    result = true
                    return
                }
            }

        }

        result
    }

    @Transactional
    def save(Trace trace) {
        if (trace == null) {
            transactionStatus.setRollbackOnly()
            render status: NOT_FOUND
            return
        }

        if (trace.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond trace.errors, view:'create'
            return
        }

        trace.save flush:true
        println "Evento: Traza modificada"
        rabbitMessagePublisher.send {
            routingKey = 'Trace.update'
            exchange = 'testGemsBBExchange'
            body = trace.project.id.toString()
        }

        respond trace, [status: CREATED, view:"show"]
    }

    @Transactional
    def update() {
        def json = request.JSON
        Trace trace = Trace.get(json.id)
        if (trace == null) {
            transactionStatus.setRollbackOnly()
            render status: NOT_FOUND
            return
        }

        if(hasChanged(trace, request.JSON)) {
            setProperties(trace, request.JSON)

            if (trace.hasErrors()) {
                transactionStatus.setRollbackOnly()
                respond trace.errors, view:'edit'
                return
            }

            trace.save(flush: true)

            println "Evento: Traza modificada"
            rabbitMessagePublisher.send {
                routingKey = 'Trace.update'
                exchange = 'testGemsBBExchange'
                body = trace.project.id.toString()
            }
        }
        else {
            println "Traza sin cambios."
        }

        respond trace, [status: OK, view:"show"]
    }

    private findByExternalKey(String tool, String externalKey) {
        Trace trace = null
        IdentityMap imap = IdentityMap.findByToolAndEntityTypeAndExternalKey(tool, 'Trace', externalKey)
        if(imap != null)
        {
            trace = Trace.get(imap.key)
        }
        trace
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
    }
}
