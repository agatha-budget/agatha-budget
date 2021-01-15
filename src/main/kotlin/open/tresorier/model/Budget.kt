package open.tresorier.model

class Budget (
    var name: String,
    var personId: String,
    id: String? = null,
    deleted: Boolean? = null
) : DbObject(id, deleted)
