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
        if(result.issues.size() > 0) {
            def firstIssue = result.issues[0]
            println firstIssue.project.name
            println "${firstIssue.status.name} ${firstIssue.tracker.name}. ${firstIssue.subject}: ${firstIssue.description}"
            println firstIssue

            def listTareas = []

            result.issues.each {
                def issue = it
                listTareas.add({
                    nombre = "Tarea 1"
                    fechaInicio = Date.parse('yyyy-MM-dd', issue.start_date)
                    fechaFin = Date.parse('yyyy-MM-dd', issue.due_date)
                    estado = issue.status.name
                    responsable = "57b135d78acec62754906455"
                    colaboradores = ["57b135d78acec62754906455"]
                })
            }

            def rpost = restClient.post("http://localhost:8081/planes") {
                contentType "application/json"
                json {
                    proyecto = "57b110c58acec622e7c43944"
                    tareas = listTareas
                }
            }
            if (rpost.getStatusCode() != HttpStatus.OK) {
                throw new Exception("Error al guardar el registro del plan. HttpStatusCode: ${rpost.getStatusCode()}")
            }
        }
    }
}
