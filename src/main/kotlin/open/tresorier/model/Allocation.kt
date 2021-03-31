package open.tresorier.model

class Allocation (
    val month: Month,
    val categoryId: String,
    var amount: Double,
    id: String? = null,
    deleted: Boolean? = null
) : DbObject(id, deleted)