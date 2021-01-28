package open.tresorier.api

import io.javalin.Javalin
import io.supertokens.javalin.SuperTokens
import io.supertokens.javalin.core.exception.SuperTokensException
import open.tresorier.dependenciesinjection.ServiceManager
import open.tresorier.exception.TresorierException
import open.tresorier.model.Budget
import open.tresorier.model.Person
import open.tresorier.utils.Properties


fun main() {

    val properties = Properties.getProperties()
    // Session Manager
    SuperTokens.config()
        .withHosts(properties.getProperty("supertoken_url"), properties.getProperty("supertoken_api_key"))

    ServiceManager.start()

    val app = Javalin.create { config ->
        config.enableCorsForOrigin(properties.getProperty("allowed_origin"))
        //val app = Javalin.create { config -> config.enableCorsForAllOrigins()
    }.start(getHerokuAssignedOrDefaultPort())

    app.get("/") { ctx ->
        ctx.result("Hello Sunshine !")
    }

    app.get("/error") { ctx ->
        val exception = TresorierException("this is your doing", Exception("why ?"))
        ctx.result(exception.id)
    }

    app.get("/ping") { ctx ->
        ctx.status(200)
        ctx.json(properties.getProperty("environment"))
    }

    app.post("/person") { ctx ->
        val name = ctx.queryParam<String>("name").get()
        val password = ctx.queryParam<String>("password").get()
        val email = ctx.queryParam<String>("email").get()
        val person: Person? = ServiceManager.personService.createPerson(name, password, email)
        if (person == null) {
            ctx.status(400)
            ctx.json("the user wasn't created")
        } else {
            ctx.status(200)
            ctx.json(person.id)
        }
    }

    app.before("/session/refresh", SuperTokens.middleware())
    app.post("/session/refresh") { ctx -> ctx.result("refreshed") }

    app.post("/login") { ctx ->
        val email = ctx.queryParam<String>("email").get()
        val password = ctx.queryParam<String>("password").get()
        val person: Person? = ServiceManager.personService.login(email, password)
        if (person == null) {
            val unlockingDate = ServiceManager.personService.getUnlockingDateForEmail(email)
            ctx.status(400)
            ctx.json("{'unlockingDate' : " + unlockingDate + "}")
        } else {
            ctx.status(200)
            SuperTokens.newSession(ctx, person.id).create()
            ctx.json(person.id)
        }
    }


    app.before("/logout", SuperTokens.middleware())
    app.delete("/logout") { ctx ->
        val session = SuperTokens.getFromContext(ctx)
        session.revokeSession()
        ctx.status(200)
        ctx.json("back to login page")
    }

    app.before("/budget", SuperTokens.middleware())

    app.post("/budget") { ctx ->
        val validSession = SuperTokens.getFromContext(ctx)
        val userId = validSession.userId
        val user: Person = ServiceManager.personService.getById(userId)
        val name = ctx.queryParam<String>("name").get()
        val budget: Budget = ServiceManager.budgetService.createBudget(name, user)
        ctx.json(budget.id)

    }

    app.exception(SuperTokensException::class.java, SuperTokens.exceptionHandler())
    app.exception(TresorierException::class.java) { e, ctx ->
        ctx.status(400)
        ctx.json("an exception occured, if it seems irrelevant send this code to your administrator : " + e.id)
    }
}

private fun getHerokuAssignedOrDefaultPort(): Int {
    val herokuPort = System.getenv("PORT");
    if (herokuPort != null) {
        return Integer.parseInt(herokuPort);
    }
    return 7000;
}
