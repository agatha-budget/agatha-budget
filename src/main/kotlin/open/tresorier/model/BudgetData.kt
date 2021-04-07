package open.tresorier.model

class BudgetData() : HashMap<Int, MonthData>() {

    override fun equals(other: Any?): Boolean {
        if (other is BudgetData) {
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