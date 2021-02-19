package open.tresorier.model

class Operation (
    var date: Long,
    var accountId: String,
    var categoryId: String,
    var memo: String,
    var amount: Double,
    id: String? = null,
    deleted: Boolean? = null
) : DbObject(id, deleted)