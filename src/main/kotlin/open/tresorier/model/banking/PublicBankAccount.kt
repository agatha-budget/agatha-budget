package open.tresorier.model.banking

import open.tresorier.model.DbObject

open class PublicBankAccount (
    var id: String,
    var name: String,
    var bankId: String,
    var timestamp: Long
)
