package open.tresorier.model.banking

import open.tresorier.model.DbObject

open class BankAccount (
    var name: String,
    var accountId: String? = null,
    id: String? = null,
    deleted: Boolean? = null
) : DbObject(id, deleted)
