package open.tresorier.model

open class Account (
    var name: String,
    var budgetId: String,
    var archived: Boolean = false,
    var bankAccountId: String? = null,
    id: String? = null,
    deleted: Boolean? = null
) : DbObject(id, deleted) {

    override fun toString(): String {
        return "id: $id, name: $name, budget : $budgetId, bankAccountId: $bankAccountId"
    }
}
