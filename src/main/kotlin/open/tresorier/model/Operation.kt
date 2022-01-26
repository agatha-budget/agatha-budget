package open.tresorier.model

class Operation (
        var accountId: String,
        var day: Day? = null,
        var categoryId: String? = null,
        var amount: Int = 0,
        var memo: String? = null,
        val orderByDay: Int,
        id: String? = null,
        deleted: Boolean? = null
) : DbObject(id, deleted)
