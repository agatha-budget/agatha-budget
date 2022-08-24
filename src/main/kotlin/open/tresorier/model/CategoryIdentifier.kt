package open.tresorier.model

class CategoryIdentifier (
    val budgetId: String,
    var pattern: String,
    var categoryId: String?,
    id: String? = null,
    deleted: Boolean? = null
) : DbObject(id, deleted)