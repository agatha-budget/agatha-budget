package open.tresorier.model
import open.tresorier.utils.Time

class PublicPerson (
    var name: String,
    var email: String,
    // true => up to date, false => not up to date, access ok, null => no billing status
    var billingStatus: Boolean? = null,
    var style: String? = null,
    var dyslexia: Boolean = false,
    val creationDate: Long,
    val hasBillingId: Boolean,
)
