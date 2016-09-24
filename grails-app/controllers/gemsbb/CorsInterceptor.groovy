package gemsbb


class CorsInterceptor {
    CorsInterceptor() {
        matchAll()
    }

    boolean before() {
        header('Access-Control-Allow-Origin', 'http://localhost:3000')
        header('Access-Control-Allow-Credentials', 'true')

        if (request.method == "OPTIONS") {
            header( "Access-Control-Allow-Headers", "origin, authorization, accept, content-type, x-requested-with")
            header( "Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE")
            header( "Access-Control-Max-Age", "3600" )
            response.status = 200
        }
        
        true
    }

    boolean after() { true }

    void afterView() {
        // no-op
    }
}
