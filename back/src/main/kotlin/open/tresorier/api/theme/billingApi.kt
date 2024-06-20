package open.tresorier.api.theme

import io.javalin.Javalin
import open.tresorier.api.getQueryParam
import open.tresorier.api.getUserFromAuth
import open.tresorier.model.enum.PriceIdEnum
import open.tresorier.services.BillingService

fun addBillingRoute(app : Javalin, billingService: BillingService) : Javalin {

    // handle webhook sent by stripe
    app.post("/from_stripe") { ctx ->
        val payload = ctx.body()
        val sigHeader = ctx.header("Stripe-Signature")
        billingService.handleWebhook(payload, sigHeader)
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

    
    return app
}