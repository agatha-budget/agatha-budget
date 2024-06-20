package open.tresorier.model

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class MonthTest {
    @Test fun testCreateMonthFromComparable() {
        val comparable = 201512
        val month = Month.createFromComparable(comparable)
        assertEquals(comparable, month.comparable)
        assertEquals(2015, month.year)
        assertEquals(12, month.month)
    }
}
