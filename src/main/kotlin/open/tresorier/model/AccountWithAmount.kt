package open.tresorier.model

class AccountWithAmount (
    name: String,
    budgetId: String,
    var computedAmount: Double,
    archived: Boolean = false,
    id: String? = null,
    deleted: Boolean? = null
) : Account(name, budgetId, archived, id, deleted) {

    override fun toString(): String {
        return "id: $id, name: $name, amount: $computedAmount"
    }

    companion object {

        fun createFromAccount(account: Account, amount: Double) : AccountWithAmount {
            return AccountWithAmount(account.name, account.budgetId, amount, account.archived, account.id, account.deleted)
        }
    }
}
