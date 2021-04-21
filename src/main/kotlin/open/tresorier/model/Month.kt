package open.tresorier.model

import open.tresorier.exception.TresorierException

class Month (
    var month: Int,
    var year: Int
){
    var comparable: Int = year*100+month

    override fun toString(): String {
        return "$month-$year"
    }

    fun findNext() : Month {
        return if (month != 12) Month(month + 1, year) else Month(1, year+1)
    }

    fun findBefore() : Month {
        return if (month != 1) Month(month - 1, year) else Month(12, year-1)
    }

    companion object {
        fun cancelIfEndLessThanStart(start: Month, end: Month){
            this.cancelIfEndLessThanStart(start.comparable, end.comparable)
        }

        fun cancelIfEndLessThanStart(start: Int, end: Int){
            if (start > end){
                throw TresorierException("the given period is invalid : $start -> $end")
            }
        }

        fun createFromComparable(comparable: Int) : Month {
            // ex 202012 = Month(12,2020)
            val month = comparable%100
            val year = (comparable-month)/100
            return Month(month, year)
        }
    }
}