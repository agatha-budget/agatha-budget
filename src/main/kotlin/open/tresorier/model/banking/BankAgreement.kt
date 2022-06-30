package open.tresorier.model.banking

import open.tresorier.utils.Time
import open.tresorier.model.DbObject

open class BankAgreement (
    var personId: String,
    var bankId: String,
    val validUntil: Long = Time.in90Days(),
    var nordigenRequisitionId: String? = null,
    var archived: Boolean = false,
    id: String? = null
    deleted: Boolean? = null
) : DbObject(id, deleted)
