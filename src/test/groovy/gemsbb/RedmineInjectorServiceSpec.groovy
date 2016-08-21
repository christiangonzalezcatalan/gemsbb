package gemsbb

import static org.mockserver.integration.ClientAndServer.startClientAndServer
import static org.mockserver.model.HttpRequest.request
import static org.mockserver.model.HttpResponse.response
import static gemsbb.Mocks.RedmineResponses.listarRegistrosPlan

import org.mockserver.integration.ClientAndServer
import org.mockserver.model.Header
import grails.test.mixin.TestFor
import spock.lang.Specification
import org.mockserver.model.Parameter
import org.mockserver.verify.VerificationTimes

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(RedmineInjectorService)
class RedmineInjectorServiceSpec extends Specification {
    protected static ClientAndServer mockServer

    def setupSpec() {
        mockServer = startClientAndServer(8081)
    }

    def cleanupSpec() {
        mockServer.stop()
    }

    def setup() {
    }

    def cleanup() {
        mockServer.reset()
    }

    void "test inject"() {
        setup:
        mockServer.when(
            request()
                .withMethod('GET')
                .withPath('/issues.json')
                .withQueryStringParameters(new Parameter('project_id', '3'))
        ).respond(response()
            .withStatusCode(200)
            .withHeaders(new Header('Content-Type', 'application/json; charset=utf-8'))
            .withBody(listarRegistrosPlan())
        )

        mockServer.when(
                request()
                        .withMethod('POST')
                        .withPath('/plans')
        ).respond(response()
                .withStatusCode(200)
                .withHeaders(new Header('Content-Type', 'application/json; charset=utf-8'))
                .withBody("")
        )

        when:
        service.inject()

        then:
        mockServer.verify(
            request()
                .withMethod("POST")
                .withPath("/plans")
                //.withBody("{username: 'foo', password: 'bar'}")
                ,
            VerificationTimes.exactly(1)
        );
    }

    // 1. Se debería usar mock de servicios para tener respuestas cargadas.
    // 2. Se puede testear interacción con llamadas a get...
    // 3. Se puede testear que se guarden n registros en plan.
    // 4. Se puede testear llamadas a api para guardar planes.
}
