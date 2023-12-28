package open.tresorier.api.theme

import io.javalin.Javalin
import open.tresorier.exception.TresorierException
import open.tresorier.utils.Properties
import open.tresorier.utils.PropertiesEnum.ENVIRONMENT

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
        ctx.result(properties.get(ENVIRONMENT))
    }

    return app
}