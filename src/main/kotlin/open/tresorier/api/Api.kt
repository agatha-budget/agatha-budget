package open.tresorier.api

import io.javalin.Javalin
import io.javalin.http.Context
import io.supertokens.javalin.SuperTokens
import io.supertokens.javalin.core.exception.SuperTokensException
import open.tresorier.dependenciesinjection.ServiceManager
import open.tresorier.exception.TresorierException
import open.tresorier.exception.TresorierIllegalException
import open.tresorier.exception.SuspendedUserException
import open.tresorier.model.*
import open.tresorier.utils.Properties
import open.tresorier.utils.PropertiesEnum.*
import open.tresorier.services.BillingService
import open.tresorier.model.enum.ProfileEnum
import open.tresorier.model.enum.PriceIdEnum
import open.tresorier.api.theme.*

fun main() {

    val properties = Properties()
    var app = setUpApp(properties)

    // Session Manager
    SuperTokens.config().withHosts(
        properties.get(SUPERTOKEN_URL),
        properties.get(SUPERTOKEN_API_KEY)
    )
    // Dependencies injection
    ServiceManager.start()

    app = addBankingRoute(app,
     ServiceManager.bankingService,
     ServiceManager.accountService,
     ServiceManager.budgetService)

    app = addAccountRoute(app,
     ServiceManager.accountService,
     ServiceManager.budgetService,
     ServiceManager.bankingService)

    app = addOperationRoute(app,
     ServiceManager.accountService,
     ServiceManager.budgetService,
     ServiceManager.categoryService,
     ServiceManager.operationService)

    app = addBudgetDataRoute(app,
     ServiceManager.budgetService,
     ServiceManager.budgetDataService)

    app.before("/session/refresh", SuperTokens.middleware())
    app.post("/session/refresh") { ctx -> ctx.result("refreshed") }

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
        val name = ctx.queryParam<String>("name").get()
        val password = ctx.queryParam<String>("password").get()
        val email = ctx.queryParam<String>("email").get()
        val profileString = ctx.queryParam<String>("profile").get()
        val profile: ProfileEnum = ProfileEnum.valueOf(profileString)
        val person: Person = ServiceManager.personService.createPerson(name, password, email, profile)
        SuperTokens.newSession(ctx, person.id).create()
        ctx.json("{\"name\" : " + person.name + "}")
    }

    app.before("/person", SuperTokens.middleware())
    app.get("/person") { ctx ->
        val publicPerson : PublicPerson = getUserFromAuth(ctx).toPublicPerson() 
        ctx.json(publicPerson)
    }

    app.put("/person") { ctx ->
        val person = getUserFromAuth(ctx)
        //optional
        val newName = getOptionalQueryParam<String>(ctx, "new_name")
        val newStyle = getOptionalQueryParam<String>(ctx, "new_style")
        val newDyslexia = getOptionalQueryParam<Boolean>(ctx, "new_dyslexia")

        val publicPerson : PublicPerson = ServiceManager.personService.updatePublicPerson(person, newName, newStyle, newDyslexia)
        ctx.json(publicPerson)    
    }

    // handle webhook sent by stripe
    app.post("/from_stripe") { ctx -> 
        val payload = ctx.body()
        val sigHeader = ctx.header("Stripe-Signature")
        ServiceManager.billingService.handleWebhook(payload, sigHeader)
    }

    app.before("/billing", SuperTokens.middleware())
    app.get("/billing") { ctx ->
        val person = getUserFromAuth(ctx)
        if (person.billingId != null) {
            ctx.result(BillingService.createBillingManagementSession(person))
        } else {
            val packageString = ctx.queryParam<String>("package").get()
            val selectedPackage: PriceIdEnum = PriceIdEnum.valueOf(packageString)
            ctx.result(BillingService.createNewUserBillingSession(person, selectedPackage))
        }
    }

    app.post("/login") { ctx ->
        val email = getQueryParam<String>(ctx, "email")
        val password = getQueryParam<String>(ctx, "password")
        try {
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
        } catch (e: Exception) {
            ctx.status(400)
            ctx.json("{\"unlockingDate\" : null }")
        }
    }

    app.before("/logout", SuperTokens.middleware())
    app.delete("/logout") { ctx ->
        val session = SuperTokens.getFromContext(ctx)
        session.revokeSession()
        ctx.result("you've been logged out")
    }

    app.before("/budget", SuperTokens.middleware())
    app.post("/budget") { ctx ->
        val user = getUserFromAuth(ctx)
        val name = getQueryParam<String>(ctx, "name")
        val profileString = getQueryParam<String>(ctx, "profile")
        val profile: ProfileEnum = ProfileEnum.valueOf(profileString)
        val budget: Budget = ServiceManager.budgetService.create(user, name, profile)
        ctx.result(budget.id)
    }

    app.put("/budget") { ctx ->
        val user = getUserFromAuth(ctx)
        val budget: Budget = ServiceManager.budgetService.getById(user, getQueryParam<String>(ctx, "budget_id"))
        val formerName = budget.name
        val newName = getQueryParam<String>(ctx, "new_name")
        ServiceManager.budgetService.update(user, budget, newName)
        ctx.result("updated from $formerName to $newName")
    }

    app.delete("/budget") { ctx ->
        val user = getUserFromAuth(ctx)
        val budget: Budget = ServiceManager.budgetService.getById(user, getQueryParam<String>(ctx, "budget_id"))
        ServiceManager.budgetService.delete(user, budget)
        ctx.result("budget ${budget.name} has been deleted")
    }

    app.before("/budget/user", SuperTokens.middleware())
    app.get("/budget/user") { ctx ->
        val user = getUserFromAuth(ctx)
        val budgetList = ServiceManager.budgetService.findByUser(user)
        ctx.json(budgetList)
    }

    app.before("/mcategory", SuperTokens.middleware())
    app.post("/mcategory") { ctx ->
        val user = getUserFromAuth(ctx)
        val budget: Budget = ServiceManager.budgetService.getById(user, getQueryParam<String>(ctx, "budget_id"))
        val name = getQueryParam<String>(ctx, "name")

        val mcategory = ServiceManager.masterCategoryService.create(user, budget, name)
        ctx.json(mcategory)
    }

    app.put("/mcategory") { ctx ->
        val user = getUserFromAuth(ctx)
        val masterCategory: MasterCategory = ServiceManager.masterCategoryService.getById(user, getQueryParam<String>(ctx, "id"))

        //optional
        val newName = getOptionalQueryParam<String>(ctx, "new_name")
        val newColor = getOptionalQueryParam<String>(ctx, "new_color")
        val newArchived = getOptionalQueryParam<Boolean>(ctx, "new_archived")
        val newDeleted = getOptionalQueryParam<Boolean>(ctx, "new_deleted")

        val updatedMasterCategory = ServiceManager.masterCategoryService.update(user, masterCategory, newName, newColor, newArchived, newDeleted)
        ctx.json(updatedMasterCategory)
    }

    app.before("/mcategory/budget", SuperTokens.middleware())
    app.get("/mcategory/budget") { ctx ->
        val user = getUserFromAuth(ctx)
        val budget: Budget = ServiceManager.budgetService.getById(user, getQueryParam<String>(ctx, "budget_id"))

        val masterCategories = ServiceManager.masterCategoryService.findByBudget(user, budget)
        ctx.json(masterCategories)
    }

    app.before("/category", SuperTokens.middleware())
    app.post("/category") { ctx ->
        val user = getUserFromAuth(ctx)
        val masterCategory: MasterCategory = ServiceManager.masterCategoryService.getById(user, getQueryParam<String>(ctx, "master_category_id"))
        val name = getQueryParam<String>(ctx, "name")

        val category = ServiceManager.categoryService.create(user, masterCategory, name)
        ctx.json(category)
    }

    app.put("/category") { ctx ->
        // required
        val user = getUserFromAuth(ctx)
        val category: Category = ServiceManager.categoryService.getById(user, getQueryParam<String>(ctx, "id"))

        //optional
        val newName = getOptionalQueryParam<String>(ctx, "new_name")
        val newMasterCategory: MasterCategory? = getOptionalQueryParam<String>(ctx, "new_master_category_id")?.let{
            ServiceManager.masterCategoryService.getById(user, it)
        }
        val newArchived = getOptionalQueryParam<Boolean>(ctx, "new_archived")
        val newDeleted = getOptionalQueryParam<Boolean>(ctx, "new_deleted")
        val updatedCategory = ServiceManager.categoryService.update(user, category, newName, newMasterCategory, newArchived, newDeleted)
        ctx.json(updatedCategory)
    }

    app.before("/category/budget", SuperTokens.middleware())
    app.get("/category/budget") { ctx ->
        val user = getUserFromAuth(ctx)
        val budget: Budget = ServiceManager.budgetService.getById(user, getQueryParam<String>(ctx, "budget_id"))

        val categories = ServiceManager.categoryService.findByBudget(user, budget)
        ctx.json(categories)
    }

    app.before("/allocation", SuperTokens.middleware())
    app.post("/allocation") { ctx ->
        val user = getUserFromAuth(ctx)
        val month : Month = Month.createFromComparable(getQueryParam<Int>(ctx, "month"))
        val category: Category = ServiceManager.categoryService.getById(user, getQueryParam<String>(ctx, "category_id"))
        val amount : Int = getQueryParam<Int>(ctx, "amount")
        val allocation = ServiceManager.allocationService.insertOrUpdate(user, month, category, amount)
        ctx.json(allocation)
    }
}

private fun setUpApp(properties: Properties): Javalin {
    val environmentStatus = properties.get(ENVIRONMENT)
    val app = Javalin.create { config ->
            if (environmentStatus == "dev") {
                config.enableCorsForAllOrigins()
            } else {
                config.enableCorsForOrigin(
                    properties.get(ALLOWED_ORIGIN_FRONT),
                    properties.get(ALLOWED_ORIGIN_BETA_FRONT),
                    properties.get(ALLOWED_ORIGIN_STRIPE)
                )
            }
    }.start(getHerokuAssignedOrDefaultPort())

    app.exception(SuperTokensException::class.java, SuperTokens.exceptionHandler())

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