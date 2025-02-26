package open.tresorier.dao

import open.tresorier.dependenciesinjection.ITest
import open.tresorier.exception.TresorierException
import open.tresorier.model.*
import open.tresorier.model.enum.ProfileEnum
import open.tresorier.utils.TestData
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.koin.core.component.inject

open class AccountDaoTest : ITest {

    val accountDao by inject<IAccountDao>()
    val personDao by inject<IPersonDao>()
    val operationDao by inject<IOperationDao>()
    val budgetDao by inject<IBudgetDao>()


    @Test
    fun getAccountOwner() {
        val account = accountDao.getById(TestData.account1Id)
        val expectedOwner = personDao.getById("person1")
        val owner = accountDao.getOwner(account)
        Assertions.assertEquals(expectedOwner.email, owner.email)
    }

    @Test
    fun getOwnerForUnstoredAccount() {
        val account = Account("courant", "id_of_an_inexisting_budget")
        val exception = Assertions.assertThrows(TresorierException::class.java) {
            accountDao.getOwner(account)
        }
        Assertions.assertEquals("the given account (${account}) appears to have no owner", exception.message)
    }

    @Test
    fun getAccountWithMetadataForBudget() {
        val budget = Budget("how much ?", TestData.person1Id, ProfileEnum.PROFILE_USER)
        budgetDao.insert(budget)
        val account = Account("account", budget.id)
        accountDao.insert(account)
        val operation1 = Operation(account.id, TestData.feb_02_2021, Category.INCOME_ID, 15065, 1, "mémo", false, false)
        val operation2 = Operation(account.id, TestData.march_02_2021, Category.INCOME_ID, -10000, 2, "reimbursement", false, false)
        operationDao.insert(operation1)
        operationDao.insert(operation2)

        val account2 = Account("account2", budget.id)
        accountDao.insert(account2)
        val operation3 = Operation(account2.id, TestData.feb_02_2021, Category.INCOME_ID, 7777, 3, "mémo", false, false)
        operationDao.insert(operation3)

        val accountList = accountDao.findByBudget(budget)
        val expectedList = listOf(
            AccountWithMetadata.createFromAccount(account, 5065),
            AccountWithMetadata.createFromAccount(account2, 7777)
        )

        Assertions.assertEquals(expectedList.toString(), accountList.toString())
    }

    @Test
    fun getAccountWithNullAmountForBudget() {
        val budget = Budget("0 in account", TestData.person1Id, ProfileEnum.PROFILE_USER)
        budgetDao.insert(budget)
        val account = Account("account", budget.id)
        accountDao.insert(account)
        val operation1 = Operation(account.id, TestData.feb_02_2021, Category.INCOME_ID, 10000, 1, "mémo", false, false)
        val operation2 = Operation(account.id, TestData.march_02_2021, Category.INCOME_ID, -10000, 2, "reimbursement", false, false)
        operationDao.insert(operation1)
        operationDao.insert(operation2)

        val accountList = accountDao.findByBudget(budget)
        val expectedList = listOf(
            AccountWithMetadata.createFromAccount(account, 0)
        )

        Assertions.assertEquals(expectedList.toString(), accountList.toString())
    }

    @Test
    fun getAccountWithNoOperationForBudget() {
        val budget = Budget("nothing in account", TestData.person1Id, ProfileEnum.PROFILE_USER)
        budgetDao.insert(budget)
        val account = Account("account", budget.id)
        accountDao.insert(account)

        val accountList = accountDao.findByBudget(budget)
        val expectedList = listOf(
            AccountWithMetadata.createFromAccount(account, 0)
        )

        Assertions.assertEquals(expectedList.toString(), accountList.toString())
    }

    @Test
    fun getAccountWithNegativeAmountForBudget() {
        val budget = Budget("minus in account", TestData.person1Id, ProfileEnum.PROFILE_USER)
        budgetDao.insert(budget)
        val account = Account("account", budget.id)
        accountDao.insert(account)
        val operation1 = Operation(account.id, TestData.feb_02_2021, Category.INCOME_ID, 10000, 1, "mémo", false, false)
        val operation2 = Operation(account.id, TestData.march_02_2021, Category.INCOME_ID, -15000, 2, "reimbursement", false, false)
        operationDao.insert(operation1)
        operationDao.insert(operation2)

        val accountList = accountDao.findByBudget(budget)
        val expectedList = listOf(
            AccountWithMetadata.createFromAccount(account, -5000)
        )

        Assertions.assertEquals(expectedList.toString(), accountList.toString())
    }

    @Test
    fun getRealAmountWithDaughterOperation() {
        val budget = Budget("with daughters", TestData.person1Id, ProfileEnum.PROFILE_USER)
        budgetDao.insert(budget)
        val account = Account("account with daughters", budget.id)
        accountDao.insert(account)
        val motherOperation = Operation(account.id, TestData.feb_02_2021, null, 50, 1, "mother", false, false)
        operationDao.insert(motherOperation)
        val daughterOperation1 = Operation(account.id, TestData.feb_02_2021, Category.INCOME_ID, 26, 2, "daughter1", false, false, motherOperation.id)
        operationDao.insert(daughterOperation1)
        val daughterOperation2 = Operation(account.id, TestData.feb_02_2021, Category.INCOME_ID, 24, 3, "daughter2", false, false, motherOperation.id)
        operationDao.insert(daughterOperation2)

        val accountList = accountDao.findByBudget(budget)
        val expectedList = listOf(
            AccountWithMetadata.createFromAccount(account, 50)
        )

        Assertions.assertEquals(expectedList.toString(), accountList.toString())
    }
}
