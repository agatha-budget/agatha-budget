package open.tresorier.model.banking

import open.tresorier.model.DbObject

open class BankAccount (
    var name: String,
    var agreementId: String,
    id: String? = null,
    deleted: Boolean? = null
) : DbObject(id, deleted) {

    override fun toString(): String {
        return "name: $name, agreementId: $agreementId, id: $id, deleted: $deleted"
    }
}