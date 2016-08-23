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

    def 'test name and mail cannot be null'() {
        when: 'null name'
        def m = new Member(email: 'christian@christian.cl')

        then: 'validation should fail'
        !m.validate()

        when: 'enull mail'
        m = new Member(name: 'christian')

        then: 'validation should fail'
        !m.validate()

        when: 'name and email are null'
        m = new Member()

        then: 'validation should fail'
        !m.validate()

        when: 'name and email have value'
        m = new Member(name: 'christian', email: 'christian@christian.cl')

        then: 'validation should pass'
        m.validate()
    }
}
