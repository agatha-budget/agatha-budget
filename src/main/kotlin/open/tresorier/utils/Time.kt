package open.tresorier.utils

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

}
