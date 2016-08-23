package gemsbb


import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Plan)
class PlanSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }

    def 'test name cannot be null'() {
        when: 'null project and null tasks'
        def p = new Plan()

        then: 'validation should fail'
        !p.validate()

        when: 'tasks is null'
        p = new Plan(project: new Project(name: 'Mi proyecto'))

        then: 'validation should fail'
        !p.validate()

        when: 'project is null'
        p = new Plan(tasks: [])

        then: 'validation should fail'
        !p.validate()

        when: 'tasks is null'
        p = new Plan(project: new Project(name: 'Mi proyecto'), tasks: [])

        then: 'validation should pass'
        p.validate()
    }
}
