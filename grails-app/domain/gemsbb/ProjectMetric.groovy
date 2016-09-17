package gemsbb

import grails.gorm.annotation.Entity
import grails.rest.*
import org.bson.types.ObjectId

@Entity
@Resource(readOnly = false, formats = ['json', 'xml'])
class ProjectMetric {
    ObjectId id
    Integer month
    Integer year
    Project project
    String name
    List<ProjectSummary> projectsSummary
    List<MemberSummary> membersSummary
    List<ProjectMetricDetail> details


    static embedded = ['project', 'projectsSummary', 'membersSummary']

    static constraints = {
        month nullable: false
        year nullable: false
        project nullable: false
        name nullable: false
        projectsSummary nullable: false
        membersSummary nullable: false
        details nullable: false
    }

    static mapping = {
        version false
    }
}
