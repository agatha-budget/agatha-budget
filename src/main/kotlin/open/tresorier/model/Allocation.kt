package open.tresorier.model

class Allocation (
    val year: Int,
    val month: Int,
    val categoryId: String,
    var amount: Double,
    id: String? = null,
    deleted: Boolean? = null
) : DbObject(id, deleted)