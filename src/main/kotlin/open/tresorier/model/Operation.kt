package open.tresorier.model

class Operation (
        var accountId: String,
        var day: Day? = null,
        var categoryId: String? = null,
        var amount: Double = 0.00,
        var memo: String? = null,
        id: String? = null,
        deleted: Boolean? = null
) : DbObject(id, deleted)
