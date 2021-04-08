package open.tresorier.model

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class DayTest {
    @Test fun testCreateDayFromComparable() {
        val comparable = 20151205
        val day = Day.createFromComparable(comparable)
        assertEquals(comparable, day.comparable)
        assertEquals(2015, day.month.year)
        assertEquals(12, day.month.month)
        assertEquals(5, day.day)
    }
}
