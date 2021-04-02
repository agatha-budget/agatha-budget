package open.tresorier.model

class CategoryData (
    var categoryId: String,
    var allocated: Double = 0.00,
    var spent: Double = 0.00,
    var available: Double = 0.00
){

    override fun toString(): String {
        return "$categoryId : $allocated/$spent/$available"
    }

}