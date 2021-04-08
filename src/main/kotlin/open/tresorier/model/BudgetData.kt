package open.tresorier.model

class BudgetData() : HashMap<Int, MonthData>() {

    fun getSortedMonths() : List<Int>{
        return this.keys.sortedWith(compareBy { it })
    }

    fun getFirstMonth() : Month? {
        return if (this.keys.size > 0) {
            val firstMonthComparable = getSortedMonths().first()
            Month.createFromComparable(firstMonthComparable)
        } else {
            null
        }
    }

    fun getLastMonth() : Month? {
        return if (this.keys.size > 0) {
            val lastMonthComparable = getSortedMonths().last()
            Month.createFromComparable(lastMonthComparable)
        } else {
            null
        }
    }

    fun set(key : Int, value: MonthData) : BudgetData {
        this[key] = value
        return this
    }

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