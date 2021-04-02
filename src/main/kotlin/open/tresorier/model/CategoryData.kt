package open.tresorier.model

class CategoryData (
    var allocated: Double = 0.00,
    var spent: Double = 0.00,
    var available: Double = 0.00
){

    override fun toString(): String {
        return "$allocated/$spent/$available"
    }

}