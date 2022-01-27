package open.tresorier.model

class Operation (
        var accountId: String,
        var day: Day,
        var categoryId: String? = null,
        var amount: Int = 0,
        var memo: String? = null,
        var orderInDay: Long,
        id: String? = null,
        deleted: Boolean? = null
) : DbObject(id, deleted)
