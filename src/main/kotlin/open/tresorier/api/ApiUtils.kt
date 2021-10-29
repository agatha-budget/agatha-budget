package open.tresorier.api

import com.stripe.Stripe
import com.stripe.model.Event
import com.stripe.exception.*
import com.stripe.net.Webhook
import com.stripe.model.checkout.Session
import com.stripe.param.checkout.SessionCreateParams
import open.tresorier.utils.Properties

object ApiUtils {

    fun createStripeSession(priceId: String, successUrl: String, cancelUrl: String): Session {
        val properties = Properties.getProperties()
        Stripe.apiKey = properties.getProperty("stripe_api_key")
        val params : SessionCreateParams = SessionCreateParams.Builder()
        .setSuccessUrl(successUrl + "?session_id={CHECKOUT_SESSION_ID}")
        .setCancelUrl(cancelUrl)
        .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
        .setMode(SessionCreateParams.Mode.SUBSCRIPTION)
        .addLineItem(SessionCreateParams.LineItem.Builder()
            // For metered billing, do not pass quantity
            .setQuantity(1L)
            .setPrice(priceId)
            .build()
        )
        .build()
        val session: Session = Session.create(params)
        return session
    }
}
