package open.tresorier.api

import io.javalin.Javalin
import io.javalin.http.Context
import io.supertokens.javalin.SuperTokens
import io.supertokens.javalin.core.exception.SuperTokensException
import open.tresorier.dependenciesinjection.ServiceManager
import open.tresorier.exception.TresorierException
import open.tresorier.exception.TresorierIllegalException
import open.tresorier.model.Budget
import open.tresorier.model.Person
import open.tresorier.utils.Properties
import java.util.Properties as JavaProperties


fun main() {

    val properties = Properties.getProperties()
    val app = setUpApp(properties)

    // Session Manager
    SuperTokens.config().withHosts(
        properties.getProperty("supertoken_url"),
        properties.getProperty("supertoken_api_key")
    )
    // Dependencies injection
    ServiceManager.start()

    app.get("/") { ctx ->
        ctx.result("Hello Sunshine !")
    }

    app.get("/error") { ctx ->
        val exception = TresorierException("this is your doing", Exception("why ?"))
        ctx.result(exception.id)
    }

    app.get("/ping") { ctx ->
        ctx.json(properties.getProperty("environment"))
    }

    app.post("/person") { ctx ->
        val name = ctx.queryParam<String>("name").get()
        val password = ctx.queryParam<String>("password").get()
        val email = ctx.queryParam<String>("email").get()
        val person: Person = ServiceManager.personService.createPerson(name, password, email)
        ctx.json(person.name + " was successfully created")
    }

    app.before("/session/refresh", SuperTokens.middleware())
    app.post("/session/refresh") { ctx -> ctx.result("refreshed") }

    app.post("/login") { ctx ->
        val email = getQueryStringParam(ctx, "email")
        val password = getQueryStringParam(ctx, "password")
        val person: Person? = ServiceManager.personService.login(email, password)
        if (person == null) {
            val unlockingDate = ServiceManager.personService.getUnlockingDateForEmail(email)
            ctx.status(400)
            ctx.json("{\"unlockingDate\" : $unlockingDate}")
        }
        person?.let {
            SuperTokens.newSession(ctx, it.id).create()
            ctx.json("{\"name\" : " + it.name + "}")
        }
    }

    app.before("/logout", SuperTokens.middleware())
    app.delete("/logout") { ctx ->
        val session = SuperTokens.getFromContext(ctx)
        session.revokeSession()
        ctx.json("you've been logged out")
    }

    app.before("/budget", SuperTokens.middleware())
    app.post("/budget") { ctx ->
        val user = getUserFromAuth(ctx)
        val name = getQueryStringParam(ctx, "name")
        val budget: Budget = ServiceManager.budgetService.create(user, name)
        ctx.json(budget.id)
    }

    app.put("/budget") { ctx ->
        val user = getUserFromAuth(ctx)
        val budget: Budget = ServiceManager.budgetService.getById(user, getQueryStringParam(ctx, "budget_id"))
        val formerName = budget.name
        val newName = getQueryStringParam(ctx, "new_name")
        ServiceManager.budgetService.update(user, budget, newName)
        ctx.json("updated from $formerName to $newName")
    }

    app.delete("/budget") { ctx ->
        val user = getUserFromAuth(ctx)
        val budget: Budget = ServiceManager.budgetService.getById(user, getQueryStringParam(ctx, "budget_id"))
        ServiceManager.budgetService.delete(user, budget)
        ctx.json("budget ${budget.name} has been deleted")
    }

    app.before("/budget/user", SuperTokens.middleware())
    app.get("/budget/user") { ctx ->
        val user = getUserFromAuth(ctx)
        val budgetList = ServiceManager.budgetService.findByUser(user)
        ctx.json(budgetList)
    }

    app.before("/account", SuperTokens.middleware())
    app.post("/account") { ctx ->
        val user = getUserFromAuth(ctx)
        val budget: Budget = ServiceManager.budgetService.getById(user, getQueryStringParam(ctx, "budget_id"))
        val name = getQueryStringParam(ctx, "name")
        val amount = getQueryDoubleParam(ctx, "amount")
        val account = ServiceManager.accountService.create(user, budget, name, amount)
        ctx.json(account)
    }
}

private fun setUpApp(properties: JavaProperties): Javalin {
    val app = Javalin.create { config ->
            if (properties.getProperty("environment") == "dev") {
                config.enableCorsForAllOrigins()
            } else {
                config.enableCorsForOrigin(properties.getProperty("allowed_origin"))
            }
    }.start(getHerokuAssignedOrDefaultPort())

    app.exception(SuperTokensException::class.java, SuperTokens.exceptionHandler())

    app.exception(TresorierException::class.java) { e, ctx ->
        ctx.status(400)
        ctx.json("an exception occured" + sendToAdminMessage(e.id))
    }

    app.exception(TresorierIllegalException::class.java) { e, ctx ->
        ctx.status(403)
        ctx.json("this transaction is not authorised for the authentified user" + sendToAdminMessage(e.id))
    }

    app.exception(Exception::class.java) { e, ctx ->
        ctx.status(500)
        // is not thrown so that only an id code will be send to the client side, the handling is done inside TresorierException class
        val exception = TresorierException("catched by API", e)
        ctx.json("an unexpected error occured on our side." + sendToAdminMessage(exception.id))
    }

    return app
}

private fun sendToAdminMessage(errorId : String) : String {
    return " Send this code to your administrator for details : $errorId"
}

private fun getHerokuAssignedOrDefaultPort(): Int {
    val herokuPort = System.getenv("PORT");
    if (herokuPort != null) {
        return Integer.parseInt(herokuPort);
    }
    return 7000;
}

private fun getUserFromAuth(ctx: Context): Person {
    val validSession = SuperTokens.getFromContext(ctx)
    val userId = validSession.userId
    return ServiceManager.personService.getById(userId)
}

private fun getQueryStringParam(ctx: Context, name: String) : String {
    return ctx.queryParam<String>(name).get()
}

private fun getQueryDoubleParam(ctx: Context, amount: String) : Double {
    return ctx.queryParam<Double>(amount).get()
}