package gemsbb


import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Member)
class MemberSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }

    def 'test name, mail and orgaization cannot be null'() {
        when: 'name, email and organization are null'
        def m = new Member()

        then: 'validation should fail'
        !m.validate()

        when: 'null name'
        m = new Member(email: 'christian@christian.cl', organization: new Organization())

        then: 'validation should fail'
        !m.validate()

        when: 'null email'
        m = new Member(name: 'christian', organization: new Organization())

        then: 'validation should fail'
        !m.validate()

        when: 'name, email and organization have value'
        m = new Member(name: 'christian', email: 'christian@christian.cl', organization: new Organization())

        then: 'validation should pass'
        m.validate()
    }
}
