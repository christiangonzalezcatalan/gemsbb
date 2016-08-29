package gemsbb

import static org.springframework.http.HttpStatus.CREATED
import static org.springframework.http.HttpStatus.NOT_FOUND
import static org.springframework.http.HttpStatus.OK

import grails.rest.*
import grails.converters.*
import grails.transaction.Transactional

class ProjectController{ // extends RestfulController<Project> {
    static responseFormats = ['json']
    static allowedMethods = [save: "POST", update: "PUT", index: "GET"]

    //ProjectController() {
    //    super(Project)
    //}

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Project.list(params), model:[projectCount: Project.count()]
    }

    private setProperties(Project project, json) {
        project.name = json.name
    }

    @Transactional
    def save() {
        Project project = new Project()
        def json = request.JSON
        setProperties(project, json)

        if (project.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond project.errors, view:'create'
            return
        }

        project.save(flush:true)

        // Si se crea al cargar desde una herramienta
        if(json.tool != null && json.externalKey != null) {
            IdentityMap imap = new IdentityMap(
                    tool: json.tool,
                    externalKey: json.externalKey.toString(),
                    entityType: 'Project',
                    key: project.id
            )
            imap.save(flush: true)
        }

        respond(project, [status: CREATED])
        //respond(project, [status: CREATED, view:"show"])
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

    private findByExternalKey(String tool, String externalKey) {
        Project project = null
        IdentityMap imap = IdentityMap.findByToolAndEntityTypeAndExternalKey(tool, 'Project', externalKey)
        if(imap != null)
        {
            project = Project.get(imap.key)
        }
        project
    }

    def search() {
        def result = new ArrayList<Project>()
        if(params.externalKey != null && params.tool != null) {
            def findResult = findByExternalKey(params.tool, params.externalKey)
            if(findResult != null) {
                result.add(findResult)
            }
        }
        respond result
    }
}
