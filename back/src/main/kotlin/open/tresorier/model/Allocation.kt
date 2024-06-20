package open.tresorier.model

class Allocation (
    val month: Month,
    val categoryId: String,
    var amount: Int,
) {

    override fun toString(): String {
        return "month : $month, categoryId: $categoryId, amount: $amount"
    }
}