package open.tresorier.utils

import open.tresorier.model.Month
import java.util.*
import java.text.SimpleDateFormat

object Time {

    fun now() : Long {
        return System.currentTimeMillis()
    }

    fun twoMonthAgo() : Long {
        return now() - 2 * 2629743000
    }

    fun threeMonthAgo() : Long {
        return now() - 3 * 2629743000
    }

    fun aWeekAgo() : Long {
        return now() - 7 * 86400000
    }

    fun isMoreThanAMonthAgo(date : Long) : Boolean {
        return ((now() - date) > 2629743000)
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
