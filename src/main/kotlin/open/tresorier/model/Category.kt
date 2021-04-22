package open.tresorier.model

class Category (
    var name: String,
    var masterCategoryId: String?,
    var archived: Boolean = false,
    id: String? = null,
    deleted: Boolean? = null
) : DbObject(id, deleted) {

    override fun toString(): String {
        return "id: $id, name $name from masterCategory $masterCategoryId"
    }

    companion object {
        const val INCOME_ID = "universal_income_category"
        const val TRANSFERT_ID = "universal_transfert_category"
    }
}
