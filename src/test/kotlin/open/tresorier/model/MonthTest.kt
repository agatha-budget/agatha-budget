package open.tresorier.model

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class MonthTest {
    @Test fun testCreateMonthFromComparable() {
        val yearMonth = 201512
        val month = Month.createFromComparable(yearMonth)
        assertEquals(yearMonth, month.comparable)
    }
}
