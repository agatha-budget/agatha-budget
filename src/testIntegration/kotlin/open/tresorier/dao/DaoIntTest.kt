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

class OperationDaoIntTest : OperationDaoTest(), IIntegrationTest {}

class PersonDaoIntTest : PersonDaoTest(), IIntegrationTest {}