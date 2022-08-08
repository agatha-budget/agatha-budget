package open.tresorier.model

class MasterCategory (
    var name: String,
    var budgetId: String,
    var color: String,
    id: String? = null,
    deleted: Boolean? = null
) : DbObject(id, deleted) {

    override fun toString(): String {
        return "id: $id, name $name from budget $budgetId"
    }

}
