package open.tresorier.model
import open.tresorier.utils.Time

class Person (
    var name: String,
    var hashedPassword: String,
    var email: String,
    var billingId: String? = null,
    // true => up to date, false => not up to date, access ok, null => no billing status
    var billingStatus: Boolean? = null,
    var style: String? = null,
    var dyslexia: Boolean = false,
    var unlockingDate: Long = 0,
    var loginAttemptCount: Int = 0,
    id: String? = null,
    deleted: Boolean? = null,
    val creationDate: Long = Time.now(),
) : DbObject(id, deleted) {

    fun toPublicPerson() : PublicPerson {
        return PublicPerson(
            this.name,
            this.email,
            this.billingStatus,
            this.style,
            this.dyslexia,
            this.creationDate,
            this.billingId != null
        )
    }

    override fun toString(): String {
        return "$id-$name-$email"
    }
}
