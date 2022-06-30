package open.tresorier.model.banking

import open.tresorier.model.DbObject

open class BankRequisition (
    id: String,
    var personId: String,
    val date: Long = 0,
    var archived: Boolean = false,
    deleted: Boolean? = null
) : DbObject(id, deleted)
