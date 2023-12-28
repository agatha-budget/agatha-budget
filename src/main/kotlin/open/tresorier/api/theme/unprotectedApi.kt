package open.tresorier.api.theme

import io.javalin.Javalin
import open.tresorier.api.getQueryParam
import open.tresorier.exception.TresorierException
import open.tresorier.model.Person
import open.tresorier.model.enum.ProfileEnum
import open.tresorier.services.PersonService
import open.tresorier.utils.Properties
import open.tresorier.utils.PropertiesEnum.ENVIRONMENT

fun addUnprotectedRoute(app : Javalin, properties: Properties, personService: PersonService) : Javalin {

    app.get("/") { ctx ->
        ctx.result("Hello Sunshine !")
    }

    app.get("/error") { ctx ->
        val exception = TresorierException("this is your doing", Exception("why ?"))
        ctx.result(exception.id)
    }

    app.get("/ping") { ctx ->
        ctx.result(properties.get(ENVIRONMENT))
    }

    app.post("/signup") { ctx ->
        val name = getQueryParam<String>(ctx, "name")
        val password = getQueryParam<String>(ctx, "password")
        val email = getQueryParam<String>(ctx, "email")
        val profileString = getQueryParam<String>(ctx,"profile")
        val profile: ProfileEnum = ProfileEnum.valueOf(profileString)
        val person: Person = personService.createPerson(name, password, email, profile)
       // SuperTokens.newSession(ctx, person.id).create()
        ctx.json("{\"name\" : " + person.name + "}")
    }
    
    return app
}