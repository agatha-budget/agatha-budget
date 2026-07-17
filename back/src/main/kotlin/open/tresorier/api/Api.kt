package open.tresorier.api

import io.javalin.Javalin
import io.javalin.router.JavalinDefaultRoutingApi
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
    ServiceManager.start()

    val environmentStatus = properties.get(ENVIRONMENT)

    Javalin.create { config ->
        if (environmentStatus == "dev") {
            config.bundledPlugins.enableCors { cors ->
                cors.addRule { it ->
                    it.allowHost(properties.get(ALLOWED_ORIGIN_LOCALHOST))
                }
            }
        } else {
            config.bundledPlugins.enableCors { cors ->
                cors.addRule { it ->
                    it.allowHost(
                        properties.get(ALLOWED_ORIGIN_FRONT),
                        properties.get(ALLOWED_ORIGIN_BETA_FRONT),
                        properties.get(ALLOWED_ORIGIN_STRIPE),
                    )
                }
            }
        }

        config.routes.exception(TresorierException::class.java) { e, ctx ->
            ctx.status(400)
            if (environmentStatus == "dev") {
                ctx.json(e.toMap())
            } else {
                ctx.result("an exception occured" + sendToAdminMessage(e.id))
            }
        }

        config.routes.exception(TresorierIllegalException::class.java) { e, ctx ->
            ctx.status(403)
            if (environmentStatus == "dev") {
                ctx.json(e.toMap())
            } else {
                ctx.result("this transaction is not authorised for the authentified user" + sendToAdminMessage(e.id))
            }
        }

        config.routes.exception(SuspendedUserException::class.java) { e, ctx ->
            ctx.status(402)
            if (environmentStatus == "dev") {
                ctx.json(e.toMap())
            } else {
                ctx.result("User needs to update its subscription" + sendToAdminMessage(e.id))
            }
        }

        config.routes.exception(NoAuthTokenException::class.java) { e, ctx ->
            ctx.status(402)
            if (environmentStatus == "dev") {
                ctx.json(e.toMap())
            } else {
                ctx.result("An errorOccured with the auth token" + sendToAdminMessage(e.id))
            }
        }

        config.routes.exception(Exception::class.java) { e, ctx ->
            ctx.status(500)
            val exception = TresorierException("catched by API", e)
            if (environmentStatus == "dev") {
                ctx.json(exception.toMap())
            } else {
                ctx.result("an unexpected error occured on our side." + sendToAdminMessage(exception.id))
            }
        }

        addRoutes(config.routes, properties)
    }.start(Integer.parseInt(properties.get(SERVER_PORT)))
}

private fun addRoutes(routes: JavalinDefaultRoutingApi, properties: Properties) {
    addBankingRoute(routes, ServiceManager.bankingService, ServiceManager.accountService, ServiceManager.budgetService)
    addAccountRoute(routes, ServiceManager.accountService, ServiceManager.budgetService, ServiceManager.bankingService)
    addOperationRoute(routes, ServiceManager.accountService, ServiceManager.budgetService, ServiceManager.categoryService, ServiceManager.operationService)
    addBudgetDataRoute(routes, ServiceManager.budgetService, ServiceManager.budgetDataService)
    addBillingRoute(routes, ServiceManager.billingService)
    addBudgetRoute(routes, ServiceManager.budgetService)
    addCategoryRoute(routes, ServiceManager.budgetService, ServiceManager.masterCategoryService, ServiceManager.categoryService, ServiceManager.allocationService)
    addUnprotectedRoute(routes, properties)

    routes.get("/person") { ctx ->
        try {
            val person = getUserFromAuth(ctx)
            ctx.json(person.toPublicPerson())
        } catch (e: Exception) {
            val data = getAuthenticationData(ctx)
            val person = ServiceManager.personService.createPerson(data["preferred_username"].toString(), id = data.subject)
            ctx.json(person.toPublicPerson())
        }
    }

    routes.put("/person") { ctx ->
        val person = getUserFromAuth(ctx)
        val newName = getOptionalQueryParam<String>(ctx, "new_name")
        val newStyle = getOptionalQueryParam<String>(ctx, "new_style")
        val newDyslexia = getOptionalQueryParam<Boolean>(ctx, "new_dyslexia")
        val publicPerson: PublicPerson = ServiceManager.personService.updatePublicPerson(person, newName, newStyle, newDyslexia)
        ctx.json(publicPerson)
    }
}
