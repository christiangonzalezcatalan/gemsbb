package gemsbb


import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Project)
class ProjectSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }

    def 'test name cannot be null'() {
        when: 'null name'
        def p = new Project()

        then: 'validation should fail'
        !p.validate()

        when: 'name has value'
        p = new Project(name: 'Mi proyecto')

        then: 'validation should pass'
        p.validate()
    }
}
