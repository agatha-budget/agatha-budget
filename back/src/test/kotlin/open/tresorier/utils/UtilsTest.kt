package open.tresorier.utils

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class UtilsTest {
    @Test fun testTruncateEmpty() {
        assertEquals("", Utils.truncStringToMax("", 2))
    }

    @Test fun testTruncateNegative() {
        assertEquals("", Utils.truncStringToMax("should disappear", -1))
    }

    @Test fun testTruncateToZero() {
        assertEquals("", Utils.truncStringToMax("should disappear too", 0))
    }

    @Test fun testTruncateShorter() {
        assertEquals("sho", Utils.truncStringToMax("should be truncated", 3))
    }

    @Test fun testTruncateNoTruncate() {
        assertEquals("should be complete", Utils.truncStringToMax("should be complete", 100))
    }
}
