package gemsbb

import static org.springframework.http.HttpStatus.CREATED
import static org.springframework.http.HttpStatus.NOT_FOUND
import static org.springframework.http.HttpStatus.NOT_FOUND
import static org.springframework.http.HttpStatus.OK

import grails.rest.*
import grails.converters.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class MemberController {// extends RestfulController<Member> {
    static responseFormats = ['json']
    static allowedMethods = [save: "POST", update: "PUT", index: "GET"]

    /*MemberController() {
        super(Member)
    }*/

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Member.list(params), model:[memberCount: Member.count()]
    }

    private setProperties(Member member, json) {
        member.name = json.name
        member.email = json.email
    }

    @Transactional
    def save() {
        Member member = new Member()
        setProperties(member, request.JSON)

        if (member.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond member.errors, view:'create'
            return
        }

        member.save(flush:true)

        // Si se crea al cargar desde una herramienta
        if(json.tool != null && json.externalKey != null) {
            IdentityMap imap = new IdentityMap(
                    tool: json.tool,
                    externalKey: json.externalKey,
                    entityType: 'Member',
                    key: member.id
            )
            imap.save(flush: true)
        }

        respond(member, [status: CREATED, view:"show"])
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
        Member member = null
        IdentityMap imap = IdentityMap.findByToolAndEntityTypeAndExternalKey(tool, 'Member', externalKey)
        if(imap != null)
        {
            member = Member.get(imap.key)
        }
        member
    }

    def search() {
        def result = []
        if(params.externalKey != null && params.tool != null) {
            result = findByExternalKey(params.tool, params.externalKey)
        }
        else if(params.email != null) {
            result = Member.findByEmail(params.email)
        }
        respond result
    }

    // Se debe filtrar por empresa!!!
    /*@Override
    protected Member queryForResource(Serializable id) {
        Book.where {
            id == id && author.id = params.authorId
        }.find()
    }*/
}
