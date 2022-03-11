package open.tresorier.model

class Operation (
        var accountId: String,
        var day: Day,
        var categoryId: String? = null,
        var amount: Int = 0,
        var orderInDay: Long,
        var memo: String? = null,
        id: String? = null,
        deleted: Boolean? = null
) : DbObject(id, deleted)
