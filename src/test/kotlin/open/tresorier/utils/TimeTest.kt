package open.tresorier.utils

import open.tresorier.model.Month
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class TimeTest {
    @Test fun testAddDelay() {
        val myBirthdate : Long = 715231200000
        val whenIm3HoursOld : Long = 715242912000
        assertEquals(whenIm3HoursOld, myBirthdate + Time.getDuration(hours=3, minutes=15, seconds=12))
    }

    @Test fun getTimestampForGivenMonth(){
        val lastMilliSecondOfMarch2141 = 5404060800-1
        val march2141 = Month(3, 2141)
        assertEquals(lastMilliSecondOfMarch2141, Time.getMaxTimestamp(march2141))
    }

    @Test fun testisMoreThanAMonthAgo(){
        assertTrue(Time.isMoreThanAMonthAgo(0))
    }

    @Test fun testLessThanAMonthAgo(){
        val earlierToday = Time.now() - 3600000
        assertFalse(Time.isMoreThanAMonthAgo(earlierToday))
    }

    // leaving this loophole to be able to have free accounts easily
    @Test fun testisMoreThanAMonthAgoInTheFuture(){ 
        val inTheFuture = Time.now() + 3600000
        assertFalse(Time.isMoreThanAMonthAgo(inTheFuture))
    }

    @Test fun testGetDateStringFromTimestamp(){
        val timestamp : Long = 1661182355000
        assertEquals("2022-08-22", Time.getDateStringFromTimestamp(timestamp))
    }

    @Test fun testGetDateStringFromNegativeTimestamp(){
        val invalidTimestamp : Long = -1661182355000
        assertEquals("1917-05-12", Time.getDateStringFromTimestamp(invalidTimestamp))
    }

}
