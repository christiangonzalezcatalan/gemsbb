package gemsbb

import grails.transaction.Transactional
import grails.plugins.rest.client.RestBuilder
import org.grails.web.json.JSONObject
import org.springframework.http.HttpStatus

@Transactional
class RedmineInjectorService {
    RestBuilder restClient = new RestBuilder()
    def inject() {
        //def resp = restClient.get("http://10.0.2.2:3000/issues.json?project_id=3")
        def resp = restClient.get("http://localhost:8081/issues.json?project_id=3")
        JSONObject result = resp.json
        result.issues.each {
            println it.project.name
            println "${it.status.name} ${it.tracker.name}. ${it.subject}: ${it.description}"
            def rpost = restClient.post("http://localhost:8081/planes") {
                contentType "application/json"
                json {
                    proyecto = "57b110c58acec622e7c43944"
                    tareas = [
                        {
                            nombre = "Tarea 1"
                            fechaInicio = "2016-08-16 10:00:20.0"
                            fechaFin = "2016-08-16 18:00:20.0"
                            estado = "Mi estado"
                            responsable = "57b135d78acec62754906455"
                            colaboradores = ["57b135d78acec62754906455"]
                        },
                        {
                            nombre = "Tarea 2"
                            fechaInicio = "2016-08-16 10:00:20.0"
                            fechaFin = "2016-08-20 10:00:20.0"
                            estado = "Mi estado"
                            responsable = "57b135d78acec62754906455"
                            colaboradores = []
                        }
                    ]
                }
            }
            if(rpost.getStatusCode() != HttpStatus.OK) {
                throw new Exception("Error al guardar el registro del plan. HttpStatusCode: ${rpost.getStatusCode()}")
            }
        }
    }
}
