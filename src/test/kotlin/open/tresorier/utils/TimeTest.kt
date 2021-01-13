package open.tresorier.utils

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TimeTest {
    @Test fun testAddDelay() {
        val myBirthdate : Long = 715231200000
        val whenIm3HoursOld : Long = 715242912000
        assertEquals(whenIm3HoursOld, myBirthdate + Time.getDuration(hours=3, minutes=15, seconds=12))
    }
}
