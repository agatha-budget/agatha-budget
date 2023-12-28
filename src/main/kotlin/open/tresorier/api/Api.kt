package open.tresorier.api

import io.javalin.Javalin
import open.tresorier.api.theme.*
import open.tresorier.dependenciesinjection.ServiceManager
import open.tresorier.exception.NoAuthTokenException
import open.tresorier.exception.SuspendedUserException
import open.tresorier.exception.TresorierException
import open.tresorier.exception.TresorierIllegalException
import open.tresorier.model.PublicPerson
import open.tresorier.utils.Properties
import open.tresorier.utils.PropertiesEnum.*

fun main() {

    val properties = Properties()
    var app = setUpApp(properties)

    // Dependencies injection
    ServiceManager.start()

    app = addRoute(app, properties)

    app.get("/person") { ctx ->
        try {
            val person = getUserFromAuth(ctx)
            ctx.json(person.toPublicPerson())
        } catch (e : Exception) {
            val data = getAuthenticationData(ctx)
            val person = ServiceManager.personService.createPerson(data["preferred_username"].toString(), id=data.subject)
            ctx.json(person.toPublicPerson())
        }
    }

}

private fun addRoute(app: Javalin, properties: Properties) : Javalin {
    var _app = addBankingRoute(app,
        ServiceManager.bankingService,
        ServiceManager.accountService,
        ServiceManager.budgetService)

    _app = addAccountRoute(_app,
        ServiceManager.accountService,
        ServiceManager.budgetService,
        ServiceManager.bankingService)

    _app = addOperationRoute(_app,
        ServiceManager.accountService,
        ServiceManager.budgetService,
        ServiceManager.categoryService,
        ServiceManager.operationService)

    _app = addBudgetDataRoute(_app,
        ServiceManager.budgetService,
        ServiceManager.budgetDataService)

    _app = addBillingRoute(_app,
        ServiceManager.billingService)

    _app = addBudgetRoute(_app,
        ServiceManager.budgetService)

    _app = addCategoryRoute(_app,
        ServiceManager.budgetService,
        ServiceManager.masterCategoryService,
        ServiceManager.categoryService,
        ServiceManager.allocationService)

    _app = addUnprotectedRoute(_app, properties)

    _app.put("/person") { ctx ->
        val person = getUserFromAuth(ctx)
        //optional
        val newName = getOptionalQueryParam<String>(ctx, "new_name")
        val newStyle = getOptionalQueryParam<String>(ctx, "new_style")
        val newDyslexia = getOptionalQueryParam<Boolean>(ctx, "new_dyslexia")

        val publicPerson : PublicPerson = ServiceManager.personService.updatePublicPerson(person, newName, newStyle, newDyslexia)
        ctx.json(publicPerson)
    }

    return _app
}

private fun setUpApp(properties: Properties): Javalin {
    val environmentStatus = properties.get(ENVIRONMENT)
    val app = Javalin.create { config ->
            if (environmentStatus == "dev") {
                config.plugins.enableCors { cors ->
                    cors.add { it -> 
                        it.allowHost(
                            properties.get(ALLOWED_ORIGIN_LOCALHOST),
                        )
                    }
                }
            } else {
                config.plugins.enableCors { cors ->
                    cors.add { it ->
                        it.allowHost(
                            properties.get(ALLOWED_ORIGIN_FRONT), 
                            properties.get(ALLOWED_ORIGIN_BETA_FRONT),
                            properties.get(ALLOWED_ORIGIN_STRIPE),
                        )
                    }
                }
            }
    }.start(7000)

    app.exception(TresorierException::class.java) { e, ctx ->
        ctx.status(400)
        if (environmentStatus == "dev") {
            ctx.json(e.toMap())
        } else {
            ctx.result("an exception occured" + sendToAdminMessage(e.id))
        }
    }

    app.exception(TresorierIllegalException::class.java) { e, ctx ->
        ctx.status(403)
        if (environmentStatus == "dev") {
            ctx.json(e.toMap())
        } else {
            ctx.result("this transaction is not authorised for the authentified user" + sendToAdminMessage(e.id))
        }
    }

    app.exception(SuspendedUserException::class.java) { e, ctx ->
        ctx.status(402)
        if (environmentStatus == "dev") {
            ctx.json(e.toMap())
        } else {
            ctx.result("User needs to update its subscription" + sendToAdminMessage(e.id))
        }
    }

    app.exception(NoAuthTokenException::class.java) { e, ctx ->
        ctx.status(402)
        if (environmentStatus == "dev") {
            ctx.json(e.toMap())
        } else {
            ctx.result("An errorOccured with the auth token" + sendToAdminMessage(e.id))
        }
    }

    app.exception(Exception::class.java) { e, ctx ->
        ctx.status(500)
        // is not thrown so that only an id code will be send to the client side, the handling is done inside TresorierException class
        val exception = TresorierException("catched by API", e)
        if (environmentStatus == "dev") {
            ctx.json(exception.toMap())
        } else {
            ctx.result("an unexpected error occured on our side." + sendToAdminMessage(exception.id))
        }
    }

    return app
}