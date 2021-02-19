package open.tresorier.dao


import open.tresorier.dependenciesinjection.IIntegrationTest
import open.tresorier.exception.TresorierException
import open.tresorier.model.Allocation
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.koin.core.component.inject

class AllocationDaoIntTest : IIntegrationTest {

    val allocationDao by inject<IAllocationDao>()
    val personDao by inject<IPersonDao>()

    @Test
    fun getOwner() {
        val allocation = allocationDao.getById("1")
        val expectedOwner = personDao.getById("1")
        val owner = allocationDao.getOwner(allocation)
        Assertions.assertEquals(expectedOwner.email, owner.email)
    }

    @Test
    fun getOwnerForUnstored() {
        val allocation = Allocation(2020, 12, "251",-254.25)
        val exception = Assertions.assertThrows(TresorierException::class.java) {
            allocationDao.getOwner(allocation)
        }
        Assertions.assertEquals("the given object appears to have no owner", exception.message)
    }

    @Test
    fun cannotHaveAllocationForInvalidMonth() {
        val allocation = Allocation(1225, 14, "1", -254.25)
        val exception = Assertions.assertThrows(TresorierException::class.java) {
            allocationDao.insert(allocation)
        }
        Assertions.assertEquals("could not insert allocation : $allocation", exception.message)
    }

    @Test
    fun cannotHaveAllocationForInvalidMonth2() {
        val allocation = Allocation(1225, -5, "1", -254.25)
        val exception = Assertions.assertThrows(TresorierException::class.java) {
            allocationDao.insert(allocation)
        }
        Assertions.assertEquals("could not insert allocation : $allocation", exception.message)
    }
}
