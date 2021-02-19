package open.tresorier.model

class Category (
    var name: String,
    var budgetId: String,
    var archived: Boolean = false,
    id: String? = null,
    deleted: Boolean? = null
) : DbObject(id, deleted)
