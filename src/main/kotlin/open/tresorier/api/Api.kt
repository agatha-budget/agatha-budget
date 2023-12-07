package open.tresorier.api

import io.javalin.Javalin
import io.javalin.http.Context
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
import org.pac4j.javalin.SecurityHandler
import org.pac4j.javalin.CallbackHandler
import org.pac4j.core.config.Config as AuthenticationConfig

fun main() {

    val properties = Properties()
    var app = setUpApp(properties)
    val authenticationConfig: AuthenticationConfig = setUpAuthentication(properties)
    val callback: CallbackHandler = CallbackHandler(authenticationConfig);
    app.get("/callback", callback);
    app.post("/callback", callback);
    
    val securityHandler: SecurityHandler = SecurityHandler(authenticationConfig, "KeycloakOidcClient")

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

    app = addUnprotectedRoute(app,
     properties, 
     ServiceManager.personService)
    
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

    app.get("/billing") { ctx ->
        val person = getUserFromAuth(ctx)
        if (person.billingId != null) {
            ctx.result(BillingService.createBillingManagementSession(person))
        } else {
            val packageString = getQueryParam<String>(ctx, "package")
            val selectedPackage: PriceIdEnum = PriceIdEnum.valueOf(packageString)
            ctx.result(BillingService.createNewUserBillingSession(person, selectedPackage))
        }
    }

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

    app.get("/mcategory/budget") { ctx ->
        val user = getUserFromAuth(ctx)
        val budget: Budget = ServiceManager.budgetService.getById(user, getQueryParam<String>(ctx, "budget_id"))

        val masterCategories = ServiceManager.masterCategoryService.findByBudget(user, budget)
        ctx.json(masterCategories)
    }

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

    app.get("/category/budget") { ctx ->
        val user = getUserFromAuth(ctx)
        val budget: Budget = ServiceManager.budgetService.getById(user, getQueryParam<String>(ctx, "budget_id"))

        val categories = ServiceManager.categoryService.findByBudget(user, budget)
        ctx.json(categories)
    }

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