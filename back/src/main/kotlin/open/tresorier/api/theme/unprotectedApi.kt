package open.tresorier.api.theme

import io.javalin.Javalin
import open.tresorier.exception.TresorierException
import open.tresorier.utils.Properties
import open.tresorier.utils.PropertiesEnum.ENVIRONMENT
import open.tresorier.dependenciesinjection.ServiceManager


fun addUnprotectedRoute(app : Javalin, properties: Properties) : Javalin {

    app.get("/") { ctx ->
        ctx.result("Hello Sunshine !")
    }

    app.get("/keycloak") { ctx ->
        ctx.result("Hello Keycloak !")
    }

    app.get("/error") { ctx ->
        val exception = TresorierException("this is your doing", Exception("why ?"))
        ctx.result(exception.id)
    }

    app.get("/ping") { ctx ->
        var pinguId = "a7898f2c-70c7-411c-82a9-bb7c55a9c9e5"
        ServiceManager.personService.getById(pinguId)
        ctx.result(properties.get(ENVIRONMENT))
    }

    return app
}