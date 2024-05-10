package open.tresorier.model

class AccountWithMetadata (
    name: String,
    budgetId: String,
    var amount: Int,
    archived: Boolean = false,
    bankAccountId: String? = null,
    id: String? = null,
    deleted: Boolean? = null
) : Account(name, budgetId, archived, bankAccountId, id, deleted) {

    override fun toString(): String {
        return "id: $id, name: $name, amount: $amount"
    }

    companion object {

        fun createFromAccount(account: Account, amount: Int) : AccountWithMetadata {
            return AccountWithMetadata(account.name, account.budgetId, amount, account.archived, account.bankAccountId, account.id, account.deleted)
        }
    }
}
