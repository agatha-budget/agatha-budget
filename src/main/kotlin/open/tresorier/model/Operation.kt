package open.tresorier.model

class Operation (
    var date: Long,
    var accountId: String,
    var categoryId: String,
    var amount: Double,
    var memo: String? = null,
    id: String? = null,
    deleted: Boolean? = null
) : DbObject(id, deleted)
