package gemsbb


import grails.rest.*
import grails.converters.*
import grails.transaction.Transactional

import static org.springframework.http.HttpStatus.CREATED
import static org.springframework.http.HttpStatus.NOT_FOUND
import static org.springframework.http.HttpStatus.OK

@Transactional(readOnly = true)
class TraceController {
    static responseFormats = ['json']
    static allowedMethods = [save: 'POST', update: 'PUT', index: 'GET']

    def index() {
        params.max = Math.min(max ?: 10, 100)
        respond Trace.list(params), model:[traceCount: Trace.count()]
    }

    private def getDetail(json) {
      println json.collect() {
          new TraceDetail(member: Member.get(it.member.id),
                  date: it.date,
                  hours: it.hours)
      }
        json.collect() {
            new TraceDetail(member: Member.get(it.member.id),
                    date: it.date,
                    hours: it.hours)
        }
    }

    private setProperties(Trace trace, json) {
        trace.project = Project.get(json.project.id)
        if(json.taskTraces != null) {
            trace.taskTraces = json.taskTraces.collect() {
                new TaskTrace(name: it.name,
                    status: it.status,
                    traceDetails: getDetail(it.traceDetails)
                )
            }
        }
    }

    @Transactional
    def save() {
        Trace trace = new Trace()
        def json = request.JSON
        setProperties(trace, request.JSON)

        if (trace.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond trace.errors, view:'create'
            return
        }

        trace.save(flush:true)

        // Si se crea al cargar desde una herramienta
        if(json.tool != null && json.externalKey != null) {
            IdentityMap imap = new IdentityMap(
                    tool: json.tool,
                    externalKey: json.externalKey.toString(),
                    entityType: 'Trace',
                    key: trace.id
            )
            imap.save(flush: true)
        }

        respond(trace, [status: CREATED, view:'show'])
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

        setProperties(trace, request.JSON)

        if (trace.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond trace.errors, view:'edit'
            return
        }

        trace.save(flush: true)

        respond(trace, [status: OK, view: 'show'])
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
