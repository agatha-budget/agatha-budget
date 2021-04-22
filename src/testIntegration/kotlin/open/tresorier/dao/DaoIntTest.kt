package open.tresorier.dao

import open.tresorier.dependenciesinjection.IIntegrationTest
import open.tresorier.exception.TresorierException
import open.tresorier.model.Operation
import open.tresorier.utils.TestData
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

class AccountDaoIntTest : AccountDaoTest(), IIntegrationTest {}

class AllocationDaoIntTest : AllocationDaoTest(), IIntegrationTest {}

class BudgetDaoIntTest : BudgetDaoTest(), IIntegrationTest {}

class CategoryDaoIntTest : CategoryDaoTest(), IIntegrationTest {}

class OperationDaoIntTest : OperationDaoTest(), IIntegrationTest {
    @Test
    override fun  cannotCreateWithAccountAndCategoryFromDistinctBudget() {
        val operation = Operation( "account1", TestData.feb_02_2021,"category5", 8525.74)
        val exception = assertThrows(TresorierException::class.java) {
            operationDao.insert(operation)
        }
        assertEquals("could not insert operation : $operation", exception.message)
    }

    @Test
    override fun  cannotCreateWithNullCategoryIsOk() {
        val operation = Operation( "account1", TestData.feb_02_2021,null, 8525.74)
        operationDao.insert(operation)
        val storedOperation = operationDao.getById(operation.id)
        assertEquals(storedOperation.amount, operation.amount)
    }
}

class PersonDaoIntTest : PersonDaoTest(), IIntegrationTest {}