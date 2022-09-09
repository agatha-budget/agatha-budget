package open.tresorier.model

class OperationWithDaughters (
        accountId: String,
        day: Day,
        categoryId: String? = null,
        amount: Int = 0,    // cents
        orderInDay: Long,
        memo: String? = null,
        pending: Boolean = false,
        locked: Boolean = false,
        var daugthers: List<Operation> = [],
        id: String? = null,
        deleted: Boolean? = null
) : Operation(accountId, day, categoryId, amount, orderInDay, memo, pending, locked, null, null, null, id, deleted) {

        override fun toString(): String {
                return "day: $day, amount: $amount, memo: $memo, daugthers: $daugthers"
        }
}
