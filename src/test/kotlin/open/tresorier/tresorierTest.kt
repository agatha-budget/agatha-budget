package open.tresorier

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TresorierTest {
    @Test fun testTransaction() {
        assertEquals("Grocery Monday", Tresorier().groceryTransaction().memo)
        assertEquals(10.25, Tresorier().groceryTransaction().price)
    }

    /**@Test fun invalidTestTransaction() {
        assertEquals("Grocery Monday", Tresorier().groceryTransaction().memo)
        assertEquals(10.24, Tresorier().groceryTransaction().price)
    }*/
}
