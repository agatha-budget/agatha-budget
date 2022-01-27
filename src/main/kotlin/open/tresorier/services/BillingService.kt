package open.tresorier.services

import com.stripe.exception.*
import com.stripe.net.Webhook
import com.stripe.model.checkout.Session as StripeCheckoutSession
import com.stripe.model.billingportal.Session as StripePortalSession

import com.stripe.model.Event
import com.stripe.model.EventDataObjectDeserializer
import com.stripe.model.Invoice
import com.stripe.model.StripeObject
import com.stripe.param.checkout.SessionCreateParams as CheckoutSessionCreateParam
import com.stripe.param.billingportal.SessionCreateParams as PortalSessionCreateParam

import com.stripe.Stripe

import open.tresorier.utils.Properties
import open.tresorier.utils.Time
import open.tresorier.model.Person
import open.tresorier.exception.*


class BillingService(private val personService: PersonService) {

    fun onSubscriptionValidation(session: StripeCheckoutSession) {
        checkClientIdentifierIsNotNull(session, session.clientReferenceId)
        val person : Person =  personService.getById(session.clientReferenceId)
        person.billingId = session.customer
        person.billingStatus = true
        personService.update(person)
    }

    fun onPaymentSuceeds(invoice: Invoice) {
        checkClientIdentifierIsNotNull(invoice, invoice.customer)
        val person : Person =  personService.getByBillingId(invoice.customer)
        person.billingStatus = true
        personService.update(person)
    }

    fun onPaymentFails(invoice: Invoice) {
        checkClientIdentifierIsNotNull(invoice, invoice.customer)
        val person : Person =  personService.getByBillingId(invoice.customer)
        // if the billing status is already false, set it to null to suspend the access to the app
        person.billingStatus = if (person.billingStatus == true) false else null // ps : the == true is needed because billingStatus is a Boolean? not a Boolean
        personService.update(person)
    }

    fun handleWebhook(payload: String, sigHeader: String?) {
        setStripeApiKey()
        val properties = Properties.getProperties()
        val endpointSecret = properties.getProperty("stripe_webhook")
        val event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
        // Deserialize the nested object inside the event
        val dataObjectDeserializer : EventDataObjectDeserializer = event.getDataObjectDeserializer();
        if (dataObjectDeserializer.getObject().isPresent()) {
            val stripeObject : StripeObject = dataObjectDeserializer.getObject().get();
            // list type of event and object structure : https://stripe.com/docs/api/events/types
            when (event?.type) {
                "checkout.session.completed" -> {
                    // Payment is successful and the subscription is created.
                    onSubscriptionValidation(stripeObject as StripeCheckoutSession)
                }   
                "invoice.paid" -> {
                    // Continue to provision the subscription as payments continue to be made.
                    onPaymentSuceeds(stripeObject as Invoice)
                }    
                "invoice.payment_failed" ->  {
                    // The payment failed or the customer does not have a valid payment method.
                    onPaymentFails(stripeObject as Invoice)
                    
                }
                else -> {} //println("Unhandled event type: " + event?.type)
            }
        } else {
            // Deserialization failed, probably due to an API version mismatch.
            // Refer to the Javadoc documentation on `EventDataObjectDeserializer` for
            // instructions on how to handle this case, or return an error here.
            throw TresorierException("deserialization of event failed : $event")
        }
    }

    companion object {
        fun createNewUserBillingSession(person: Person): String {
            setStripeApiKey()
            val properties = Properties.getProperties()
            val priceId: String = properties.getProperty("price_id")
            val taxId: String = properties.getProperty("tax_id")
            val succesUrl: String = properties.getProperty("succesUrl")
            val cancelUrl: String = properties.getProperty("cancelUrl")

            val params : CheckoutSessionCreateParam = CheckoutSessionCreateParam.Builder()
                .setSuccessUrl(succesUrl)
                .setCancelUrl(cancelUrl)
                .setClientReferenceId(person.id)
                .setCustomerEmail(person.email)
                .addPaymentMethodType(CheckoutSessionCreateParam.PaymentMethodType.CARD)
                .setMode(CheckoutSessionCreateParam.Mode.SUBSCRIPTION)
                .addLineItem(CheckoutSessionCreateParam.LineItem.Builder()
                    // For metered billing, do not pass quantity
                    .setQuantity(1L)
                    .setPrice(priceId)
                    .addTaxRate(taxId)
                    .build()
                )
                .putExtraParam("allow_promotion_codes", "true")
                .build()
            val session: StripeCheckoutSession = StripeCheckoutSession.create(params)
            return session.getUrl()
        }

        fun createBillingManagementSession(person: Person): String {
            setStripeApiKey()
            val properties = Properties.getProperties()
            val succesUrl: String = properties.getProperty("succesUrl")

            val params : PortalSessionCreateParam = PortalSessionCreateParam.Builder()
                .setCustomer(person.billingId)
                .setReturnUrl(succesUrl)
                .build();
        
            val session: StripePortalSession = StripePortalSession.create(params)        
            return session.getUrl()
        }
       
        fun setStripeApiKey() {
            val properties = Properties.getProperties()
            Stripe.apiKey = properties.getProperty("stripe_api_key")
        }

        fun checkClientIdentifierIsNotNull(stripeObject: StripeObject, id : String?) {
            id ?: throw TresorierException("no customer attached to this operation : $stripeObject")
        }

        fun checkIfUserSubscriptionIsActive(person: Person) {
            if (
                person.billingStatus == null
                && Time.isMoreThanAMonthAgo(person.creationDate)
            ) {
                throw SuspendedUserException("this user subscription is not up to date")
            }
        }

    }
}