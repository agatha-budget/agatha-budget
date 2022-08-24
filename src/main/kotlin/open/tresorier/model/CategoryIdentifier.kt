package open.tresorier.model

class CategoryIdentifier (
    val budgetId: String,
    val pattern: String,
    val categoryId: String?,
    id: String? = null,
    deleted: Boolean? = null
) : DbObject(id, deleted)