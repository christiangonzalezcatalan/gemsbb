package gemsbb


import grails.rest.*
import grails.converters.*

class MemberController extends RestfulController<Member> {
    static responseFormats = ['json']

    MemberController() {
        super(Member)
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
