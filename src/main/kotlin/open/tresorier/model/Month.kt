package open.tresorier.model

class Month (
    var month: Int,
    var year: Int
){
    override fun toString(): String {
        return "$month-$year"
    }
}