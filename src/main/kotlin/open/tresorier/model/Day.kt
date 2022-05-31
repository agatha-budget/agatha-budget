package open.tresorier.model

import com.fasterxml.jackson.annotation.JsonValue
import open.tresorier.exception.TresorierException

class Day (
    var month: Month,
    var day: Int
){
    @JsonValue var comparable: Int = month.year*10000+month.month*100+day

    override fun toString(): String {
        return "$month-$day"
    }

    fun isEquals(day: Day): Boolean {
        return day.comparable == this.comparable
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

        fun checkComparableIsValid(comparable: Int) : Boolean {
            val day = comparable%100
            val month = ((comparable-day)/100)%100
            val year = (comparable-month-day)/10000
            if (month < 1 || month > 12) {
                return false // month is invalid
            }
            // check valid day
            if (day > 28) { // possibility of invalid day
                // february during a bissextile year
                if (month == 2 && year%4 != 0) {
                    return false
                }
                if (day > 29) {
                    // february never have more of 29 days
                    if (month == 2) {
                        return false
                    }
                    if (day > 30) {
                        // for april, june, september and november
                        var listMonths = listOf(4, 6, 9, 11)
                        if (listMonths.contains(month)) {
                            return false
                        }
                        // day superior of 31
                        if (day > 31) {
                            return false
                        }
                    }
                }
            }
            return true
        }
    }
}