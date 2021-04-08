package open.tresorier.model

import open.tresorier.exception.TresorierException

class Day (
    var month: Month,
    var day: Int
){
    var comparable: Int = month.year*10000+month.month*100+day

    override fun toString(): String {
        return "$month-$day"
    }

    companion object {
        fun cancelIfEndLessThanStart(start: Day, end: Day){
            this.cancelIfEndLessThanStart(start.comparable, end.comparable)
        }

        fun cancelIfEndLessThanStart(start: Int, end: Int){
            if (start > end){
                throw TresorierException("the given period is invalid : $start -> $end")
            }
        }

        fun createFromComparable(comparable: Int) : Day {
            // ex 20201201 = Day(Month(12,2020), 1)
            val day = comparable%100
            val month = ((comparable-day)/100)%100
            val year = (comparable-month-day)/10000
            return Day(Month(month, year), day)
        }
    }
}