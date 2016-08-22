package gemsbb


import grails.rest.*
import grails.converters.*

class MemberController extends RestfulController<Member> {
    static responseFormats = ['json']

    MemberController() {
        super(Member)
    }

    /*private getByExternalKey(String externalKey) {
        IdentityMap.findByToolAndEntityTypeAndExternalKey(params.email)
    }*/

    def search() {
        def result = []
        if(params.externalKey != null) {
            result = Member.findByEmail(params.email)
        }
        if(params.email != null) {
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
