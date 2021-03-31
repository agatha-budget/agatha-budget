package open.tresorier.dao


import open.tresorier.dependenciesinjection.IIntegrationTest
import open.tresorier.exception.TresorierException
import open.tresorier.model.Operation
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.koin.core.component.inject

class OperationDaoIntTest : IIntegrationTest {

    val operationDao by inject<IOperationDao>()
    val personDao by inject<IPersonDao>()

    @Test
    fun getOwner() {
        val operation = operationDao.getById("operation1")
        val expectedOwner = personDao.getById("person1")
        val owner = operationDao.getOwner(operation)
        Assertions.assertEquals(expectedOwner.email, owner.email)
    }

    @Test
    fun getOwnerForUnstored() {
        val operation = Operation(1225, "545", "56", -25.54, "achat")
        val exception = Assertions.assertThrows(TresorierException::class.java) {
            operationDao.getOwner(operation)
        }
        Assertions.assertEquals("the given object appears to have no owner", exception.message)
    }

    @Test
    fun cannotCreateWithAccountAndCategoryFromDistinctBudget() {
        val operation = Operation(1225, "account1", "category5", 8525.74)
        val exception = Assertions.assertThrows(TresorierException::class.java) {
            operationDao.insert(operation)
        }
        Assertions.assertEquals("could not insert operation : $operation", exception.message)
    }
}
