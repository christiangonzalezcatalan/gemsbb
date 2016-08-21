package gemsbb

import grails.transaction.Transactional
import grails.plugins.rest.client.RestBuilder
import org.grails.web.json.JSONObject
import org.springframework.http.HttpStatus

@Transactional
class RedmineInjectorService {
    RestBuilder restClient = new RestBuilder()

    private def getProject(String id, String name) {

    }

    def inject() {
        // 1. Obtener proyecto. Si no está en bb, crear.
        // 2. Para cada tarea del plan:
        //  2.1. Obtener responsable. Si no existe en bb, crear.
        //  2.2. Agregar issues a la lista de tareas

        //def resp = restClient.get("http://10.0.2.2:3000/issues.json?project_id=3")
        def resp = restClient.get("http://localhost:8081/issues.json?project_id=3")
        JSONObject result = resp.json
        if(result.issues.size() > 0) {
            def firstIssue = result.issues[0]
            println firstIssue.project.name
            println "${firstIssue.status.name} ${firstIssue.tracker.name}. ${firstIssue.subject}: ${firstIssue.description}"
            println firstIssue

            def taskList = []

            result.issues.each {
                def issue = it
                taskList.add({
                    name = "Task 1"
                    startDate = Date.parse('yyyy-MM-dd', issue.start_date)
                    dueDate = Date.parse('yyyy-MM-dd', issue.due_date)
                    status = issue.status.name
                    responsible = "57b135d78acec62754906455"
                    contributors = ["57b135d78acec62754906455"]
                })
            }

            def rpost = restClient.post("http://localhost:8081/plans") {
                contentType "application/json"
                json {
                    project = "57b110c58acec622e7c43944"
                    tasks = taskList
                }
            }
            if (rpost.getStatusCode() != HttpStatus.OK) {
                throw new Exception("Error al guardar el registro del plan. HttpStatusCode: ${rpost.getStatusCode()}")
            }
        }
    }
}
