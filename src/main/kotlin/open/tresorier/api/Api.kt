package open.tresorier.api

import com.nimbusds.oauth2.sdk.auth.ClientAuthenticationMethod
import io.javalin.Javalin
import open.tresorier.api.theme.*
import open.tresorier.dependenciesinjection.ServiceManager
import open.tresorier.exception.SuspendedUserException
import open.tresorier.exception.TresorierException
import open.tresorier.exception.TresorierIllegalException
import open.tresorier.model.*
import open.tresorier.utils.Properties
import open.tresorier.utils.PropertiesEnum.*
import org.pac4j.core.client.Clients
import org.pac4j.core.matching.matcher.CorsMatcher
import org.pac4j.javalin.CallbackHandler
import org.pac4j.javalin.SecurityHandler
import org.pac4j.oidc.client.KeycloakOidcClient
import org.pac4j.oidc.config.KeycloakOidcConfiguration
import org.pac4j.core.config.Config as PAC4J_Config

fun main() {

    val properties = Properties()
    var app = setUpApp(properties)
    val authenticationConfig: PAC4J_Config = setUpAuthentication(properties)
    val callback = CallbackHandler(authenticationConfig);
    app.get("/callback", callback);
    app.post("/callback", callback);
    
    val securityHandler: SecurityHandler = SecurityHandler(authenticationConfig, "KeycloakOidcClient", "isAuthenticated", "cors")

    // Dependencies injection
    ServiceManager.start()

    app = addRoute(app, properties)
    app.before("/keycloak", securityHandler)
    app.get("/keycloak") { ctx ->
        ctx.result("Hello Keycloak !")
    }

    app.before("/budget/user", securityHandler)
    app.get("/budget/user") { ctx ->
        ctx.result("Hello Keycloak protected!")
        //val user = getUserFromAuth(ctx)
        //val budgetList = ServiceManager.budgetService.findByUser(user)
        //ctx.json(budgetList)
    }

    app.get("/person") { ctx ->
        ctx.result("Hello Open Door et co !")
        //val publicPerson : PublicPerson = getUserFromAuth(ctx).toPublicPerson()
        //ctx.json(publicPerson)
    }

}

private fun setUpAuthentication(properties: Properties): PAC4J_Config {

    val oidcConfig = KeycloakOidcConfiguration()
    oidcConfig.clientId = properties.get(KEYCLOAK_ID)
    oidcConfig.secret = properties.get(KEYCLOAK_SECRET)
    oidcConfig.discoveryURI = properties.get(KEYCLOAK_DISC_URI)
    oidcConfig.baseUri = properties.get(KEYCLOAK_BASE_URI)
    oidcConfig.realm = properties.get(KEYCLOAK_REALM)
    oidcConfig.clientAuthenticationMethod = ClientAuthenticationMethod.CLIENT_SECRET_BASIC

    val keyCloakClient = KeycloakOidcClient(oidcConfig)
    val clients = Clients(properties.get(API_BASE_URL) + "/callback", keyCloakClient)
    val config = PAC4J_Config(clients)
    val corsMatcher = CorsMatcher()
    corsMatcher.allowOrigin = properties.get(FRONT_URL) + " " + properties.get(KEYCLOAK_BASE_URI)
    config.addMatcher("cors", corsMatcher)
    return config
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

    _app = addUnprotectedRoute(_app,
        properties,
        ServiceManager.personService)

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