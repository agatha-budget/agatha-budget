package open.tresorier.model

class MonthData() : HashMap<String, CategoryData>() {

    fun set(key : String, value: CategoryData) : MonthData {
        this[key] = value
        return this
    }

    override fun equals(other: Any?): Boolean {
        if (other is MonthData) {
            var same = (this.size == other.size)
            if (!same){return false}
            for (key in this.keys){
                same = same and (this[key] == other[key])
            }
            return same
        } else {
            return false
        }
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}