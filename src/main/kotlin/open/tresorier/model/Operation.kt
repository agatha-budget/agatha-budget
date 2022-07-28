package open.tresorier.model

class Operation (
        var accountId: String,
        var day: Day,
        var categoryId: String? = null,
        var amount: Int = 0,    // cents
        var orderInDay: Long,
        var memo: String? = null,
        var pending: Boolean = false,
        var locked: Boolean = false,
        var motherOperationId: String? = null,
        id: String? = null,
        deleted: Boolean? = null
) : DbObject(id, deleted) {

        override fun toString(): String {
                return "amount: $amount, memo: $memo, motherOperation: $motherOperationId"
            }

        fun isEquals(operation: Operation?): Boolean {
                if (operation == null) {
                        return false
                }
               if (!operation.day.isEquals(this.day)) {
                        return false
                }
                if (operation.amount != this.amount) {
                        return false
                }
                if (operation.categoryId != this.categoryId) {
                        return false
                }
                if (!operation.memo.equals(this.memo)) {
                        return false
                }
                if (operation.motherOperationId != this.motherOperationId) {
                        return false
                }
                return true
        }
}
