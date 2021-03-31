package open.tresorier.model

class MasterCategory (
    var name: String,
    var budgetId: String,
    var archived: Boolean = false,
    id: String? = null,
    deleted: Boolean? = null
) : DbObject(id, deleted) {

    companion object {
        const val INCOME_ID = "universal_income_category"
        const val TRANSFERT_ID = "universal_transfert_category"

    }
}
