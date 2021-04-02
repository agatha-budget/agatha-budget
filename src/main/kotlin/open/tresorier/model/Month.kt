package open.tresorier.model

class Month (
    var month: Int,
    var year: Int
){
    var comparable: Int = year*100+month

    override fun toString(): String {
        return "$month-$year"
    }

}