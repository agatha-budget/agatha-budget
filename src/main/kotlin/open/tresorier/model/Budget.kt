package open.tresorier.model

class Budget (
    var name: String,
    var personId: String,
    id: String? = null,
    deleted: Boolean? = null
) : DbObject(id, deleted) {

    companion object {
        const val DEFAULT_BUDGET_NAME = "I18N_DEFAULT_BUDGET"
    }
}
