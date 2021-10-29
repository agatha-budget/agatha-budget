package open.tresorier.api

import com.stripe.Stripe
import com.stripe.model.Event
import com.stripe.exception.*
import com.stripe.net.Webhook
import com.stripe.model.checkout.Session
import com.stripe.param.checkout.SessionCreateParams
import open.tresorier.utils.Properties
import open.tresorier.model.Person

object ApiUtils {

    fun createStripeSession(person: Person, priceId: String, successUrl: String, cancelUrl: String): String {
        val properties = Properties.getProperties()
        Stripe.apiKey = properties.getProperty("stripe_api_key")
        val params : SessionCreateParams = SessionCreateParams.Builder()
        .setSuccessUrl(successUrl + "?session_id={CHECKOUT_SESSION_ID}")
        .setCancelUrl(cancelUrl)
        .setClientReferenceId(person.id)
        .setCustomerEmail(person.email)
        .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
        .setMode(SessionCreateParams.Mode.SUBSCRIPTION)
        .addLineItem(SessionCreateParams.LineItem.Builder()
            // For metered billing, do not pass quantity
            .setQuantity(1L)
            .setPrice(priceId)
            .build()
        )
        .putExtraParam("allow_promotion_codes", "true")
        .build()
        val session: Session = Session.create(params)
        return session.getUrl()
    }
}
