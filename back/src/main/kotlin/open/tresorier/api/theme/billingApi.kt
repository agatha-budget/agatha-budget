package open.tresorier.api.theme

import io.javalin.router.JavalinDefaultRoutingApi
import open.tresorier.api.getQueryParam
import open.tresorier.api.getUserFromAuth
import open.tresorier.model.enum.PriceIdEnum
import open.tresorier.services.BillingService

fun addBillingRoute(routes: JavalinDefaultRoutingApi, billingService: BillingService) {

    // handle webhook sent by stripe
    routes.post("/from_stripe") { ctx ->
        val payload = ctx.body()
        val sigHeader = ctx.header("Stripe-Signature")
        billingService.handleWebhook(payload, sigHeader)
    }

    routes.get("/billing") { ctx ->
        val person = getUserFromAuth(ctx)
        if (person.billingId != null) {
            ctx.result(BillingService.createBillingManagementSession(person))
        } else {
            val packageString = getQueryParam<String>(ctx, "package")
            val selectedPackage: PriceIdEnum = PriceIdEnum.valueOf(packageString)
            ctx.result(BillingService.createNewUserBillingSession(person, selectedPackage))
        }
    }

    

}