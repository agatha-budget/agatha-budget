package open.tresorier.dao

import open.tresorier.dependenciesinjection.IIntegrationTest
import open.tresorier.exception.TresorierException
import open.tresorier.model.Operation
import open.tresorier.utils.TestData
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class AccountDaoIntTest : AccountDaoTest(), IIntegrationTest {}

class AllocationDaoIntTest : AllocationDaoTest(), IIntegrationTest {}

class BudgetDaoIntTest : BudgetDaoTest(), IIntegrationTest {}

class OperationDaoIntTest : OperationDaoTest(), IIntegrationTest {
    @Test
    override fun  cannotCreateWithAccountAndCategoryFromDistinctBudget() {
        val operation = Operation(TestData.feb_02_2021, "account1", "category5", 8525.74)
        val exception = Assertions.assertThrows(TresorierException::class.java) {
            operationDao.insert(operation)
        }
        Assertions.assertEquals("could not insert operation : $operation", exception.message)
    }
}

class PersonDaoIntTest : PersonDaoTest(), IIntegrationTest {}