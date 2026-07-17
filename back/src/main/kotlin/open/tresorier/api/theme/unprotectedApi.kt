package open.tresorier.api.theme

import io.javalin.router.JavalinDefaultRoutingApi
import open.tresorier.exception.TresorierException
import open.tresorier.utils.Properties
import open.tresorier.utils.PropertiesEnum.ENVIRONMENT
import open.tresorier.dependenciesinjection.ServiceManager


fun addUnprotectedRoute(routes: JavalinDefaultRoutingApi, properties: Properties) {

    routes.get("/") { ctx ->
        ctx.result("Hello Sunshine !")
    }

    routes.get("/keycloak") { ctx ->
        ctx.result("Hello Keycloak !")
    }

    routes.get("/error") { ctx ->
        val exception = TresorierException("this is your doing", Exception("why ?"))
        ctx.result(exception.id)
    }

    routes.get("/ping") { ctx ->
        var pinguId = "a7898f2c-70c7-411c-82a9-bb7c55a9c9e5"
        ServiceManager.personService.getById(pinguId)
        ctx.result(properties.get(ENVIRONMENT))
    }


}