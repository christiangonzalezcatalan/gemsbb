package gemsbb

import grails.test.mixin.TestFor
import grails.test.mixin.Mock
import spock.lang.Specification
import org.bson.types.ObjectId

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Trace)
@Mock([TaskTrace, TraceDetail])
class TraceSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }

    def 'test constraints'() {
        when: 'null project and null tasks'
        def p = new Trace()

        then: 'validation should fail'
        !p.validate()

        when:
        TraceDetail td = new TraceDetail()

        then:
        !td.validate()

        when:
        td = new TraceDetail(
                member: new Member(name: 'christian', email: 'christian@christian.cl')
        )

        then:
        !td.validate()

        when:
        td = new TraceDetail(
                date: new Date()
        )

        then:
        !td.validate()

        when:
        td = new TraceDetail(
                hours: 4
        )
        then:
        !td.validate()

        when:
        td = new TraceDetail(
                member: new Member(name: 'christian', email: 'christian@christian.cl'),
                date: new Date(),
                hours: 5
        )

        then:
        td.validate()

        when: 'null project and null tasks and null trask trace id'
        TaskTrace ts = new TaskTrace(name: 'Traza de mi proyecto')

        then: 'validation should fail'
        !ts.validate()

        when: 'null task trace id'
        ts = new TaskTrace(
            name: 'Traza de mi proyecto',
            status: 'Finished',
            traceDetails: [td])

        then: 'validation should fail'
        !ts.validate()

        when: 'null name'
        ts = new TaskTrace(
            taskTraceId: new ObjectId(),
            status: 'Finished',
            traceDetails: [td])

        then: 'validation should fail'
        !ts.validate()

        when: 'null status'
        ts = new TaskTrace(
            taskTraceId: new ObjectId(),
            name: 'Traza de mi proyecto',
            traceDetails: [td])

        then: 'validation should fail'
        !ts.validate()

        when: 'null project and null tasks'
        ts = new TaskTrace(
            taskTraceId: new ObjectId(),
            name: 'Traza de mi proyecto',
            status: 'Finished',
            traceDetails: [td])

        then: 'validation should pass'
        ts.validate()

        when:
        p = new Trace(project: new Project(name: 'Mi proyecto'), taskTraces: [ts])

        then: 'validation should pass'
        p.validate()
    }
}
