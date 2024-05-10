package open.tresorier.model

class AccountWithMetadata (
    name: String,
    budgetId: String,
    var amount: Int,
    var syncedUntil: Long,
    archived: Boolean = false,
    bankAccountId: String? = null,
    id: String? = null,
    deleted: Boolean? = null
) : Account(name, budgetId, archived, bankAccountId, id, deleted) {

    override fun toString(): String {
        return "id: $id, name: $name, amount: $amount, syncedUntil: $syncedUntil, bankAccoundId: $bankAccountId"
    }

    companion object {

        fun createFromAccount(account: Account, amount: Int, syncedUntil: Long = 0) : AccountWithMetadata {
            return AccountWithMetadata(account.name, account.budgetId, amount, syncedUntil, account.archived, account.bankAccountId, account.id, account.deleted)
        }
    }
}
