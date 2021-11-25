package open.tresorier.model

class Person (
    var name: String,
    var hashedPassword: String,
    var email: String,
    var billingId: String? = null,
    var billingStatus: Boolean? = null,
    var unlockingDate: Long = 0,
    var loginAttemptCount: Int = 0,
    id: String? = null,
    deleted: Boolean? = null
) : DbObject(id, deleted)
