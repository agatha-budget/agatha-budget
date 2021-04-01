package open.tresorier.utils

import open.tresorier.model.Month
import java.util.*

object Time {

    fun now() : Long {
        return System.currentTimeMillis()
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
        val date = Calendar.getInstance(TimeZone.getTimeZone("GMT"))
        date.set(givenMonth.year, givenMonth.month +1 -1, 1, 0,0,0)
        return date.timeInMillis/1000 - 1
    }

}
