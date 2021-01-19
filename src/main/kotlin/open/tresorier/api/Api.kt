package open.tresorier.api

import io.javalin.Javalin
import io.supertokens.javalin.*
import io.supertokens.javalin.core.exception.SuperTokensException
import open.tresorier.model.Person
import open.tresorier.utils.Properties
import open.tresorier.exception.TresorierException
import open.tresorier.dependenciesinjection.ServiceManager

fun main() {

    val properties = Properties.getProperties()
    // Session Manager
    SuperTokens.config().withHosts(properties.getProperty("supertoken_url"), properties.getProperty("supertoken_api_key"))

    ServiceManager.start()

    val app = Javalin.create { config ->
                                   config.enableCorsForOrigin(properties.getProperty("allowed_origin"))
    }.apply {
        exception(SuperTokensException::class.java, SuperTokens.exceptionHandler())
        exception(Exception::class.java) { e, _ -> e.printStackTrace() }
    }.start(getHerokuAssignedOrDefaultPort())

    app.get("/") { ctx ->
                       ctx.result("Hello Sunshine !")
    }


    app.get("/error") { ctx ->
                       val exception = TresorierException(Exception("why ?"), "this is your doing")
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
                          val person : Person? = ServiceManager.personService.createPerson(name, password, email)
                          if (person == null) {
                              ctx.status(400)
                              ctx.json("the user wasn't created")
                          } else {
                              ctx.status(200)
                              ctx.json(person.id)
                          }
    }

    app.before("/session/refresh") { SuperTokens.middleware() }
    app.post("/session/refresh") { ctx -> ctx.result("refreshed") }

    app.post("/login") { ctx ->
                             val email = ctx.queryParam<String>("email").get()
                         val password = ctx.queryParam<String>("password").get()
                         val person : Person? = ServiceManager.personService.login(email, password)
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

    app.get("/login") { ctx ->
                             val email = ctx.queryParam<String>("email").get()
                         val password = ctx.queryParam<String>("password").get()
                         val person : Person? = ServiceManager.personService.login(email, password)
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

    app.before("/logout") { SuperTokens.middleware() }
    app.delete("/logout") { ctx ->
                            val session = SuperTokens.getFromContext(ctx)
                            session.revokeSession()
                            ctx.status(200)
                            ctx.json("back to login page")
    }

    app.before("/budget") { SuperTokens.middleware() }
    app.post("/budget") { ctx ->
                          ctx.json("create budget")
    }


}

private fun getHerokuAssignedOrDefaultPort() : Int {
    val herokuPort = System.getenv("PORT");
    if (herokuPort != null) {
        return Integer.parseInt(herokuPort);
    }
    return 7000;
}
