package open.tresorier.model

import open.tresorier.model.enum.ProfileEnum

class Budget (
    var name: String,
    var personId: String,
    var profile: ProfileEnum,
    id: String? = null,
    deleted: Boolean? = null
) : DbObject(id, deleted) {

    override fun toString(): String {
        return "budget $name of user $personId"
    }

    companion object {
        const val DEFAULT_BUDGET_NAME = "I18N_DEFAULT_BUDGET"
    }
}
