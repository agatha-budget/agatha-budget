package open.tresorier.utils

import open.tresorier.model.Month
import java.text.SimpleDateFormat
import java.util.*

object Time {

    val HOUR = 3600000
    val DAY = 86400000
    val WEEK = 604800000
    val MONTH = 2629743000
    fun now() : Long {
        return System.currentTimeMillis()
    }

    fun isInTheFuture(date: Long) : Boolean {
        return date > now()
    }

    fun anHourAgo(): Long {
        return now() - HOUR
    }

    fun twoMonthAgo() : Long {
        return now() - (2 * MONTH)
    }

    fun threeMonthAgo() : Long {
        return now() - (3 * MONTH)
    }

    fun aWeekAgo() : Long {
        return now() - WEEK
    }

    fun inTwoWeeks(): Long {
        return now() + (2 * WEEK)
    }

    fun isMoreThanAMonthAgo(date : Long) : Boolean {
        return ((now() - date) > MONTH)
    }

    fun isLessThan89DaysAgo(date : Long) : Boolean {
        return ((now() - date) < 7689600000)
    }

    fun ninetyDaysLater(date: Long) : Long {
        return (date + 7776000000)
    }

    fun ninetyDaysAgo() : Long {
        return (now() - 7776000000)
    }

    fun getDuration(hours : Int = 0, minutes : Int = 0, seconds : Int = 0) : Long {
        val duration : Int = (hours*(60*60)
            + minutes*(60)
            + seconds
            )*1000 // milisec
        return duration.toLong()
    }

    // may need to replace
    fun getMaxTimestamp(givenMonth: Month) : Long {
        // month +1 to get the next month -1 because month are counted from 0 to 11
        val firstSecOfNextMonth = Calendar.getInstance(TimeZone.getTimeZone("GMT"))
        firstSecOfNextMonth.set(givenMonth.year, givenMonth.month +1 -1, 1, 0,0,0)
        return (firstSecOfNextMonth.timeInMillis/1000) - 1
    }

    fun getMinTimestamp(givenMonth: Month) : Long {
        val date = Calendar.getInstance(TimeZone.getTimeZone("GMT"))
        date.set(givenMonth.year, givenMonth.month -1, 1, 0,0,0)
        return date.timeInMillis/1000
    }

    fun getDateStringFromTimestamp(timestamp: Long) : String {
        val date = Date(timestamp)
        val format = SimpleDateFormat("yyyy-MM-dd")
        return format.format(date)
    }
}