package open.tresorier.model

class CategoryData (
    var allocated: Double = 0.00,
    var spent: Double = 0.00,
    var available: Double = 0.00
){

    override fun toString(): String {
        return "$allocated/$spent/$available"
    }

    override fun equals(other: Any?): Boolean {
        if (other is CategoryData) {
            val sameAllocation = (this.allocated == other.allocated)
            val sameSpent = (this.spent == other.spent)
            val sameAvailable = (this.available == other.available)
            return sameAllocation and sameSpent and sameAvailable
        } else {
            return false
        }
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }

}