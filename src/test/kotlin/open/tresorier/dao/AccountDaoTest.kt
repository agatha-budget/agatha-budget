package open.tresorier.dao

import open.tresorier.dependenciesinjection.ITest
import open.tresorier.exception.TresorierException
import open.tresorier.model.Account
import open.tresorier.model.AccountWithAmount
import open.tresorier.model.Category
import open.tresorier.model.Operation
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
        Assertions.assertEquals("the given object appears to have no owner", exception.message)
    }

    @Test
    fun getAccountWithAmountForBudget() {
        val budget = budgetDao.getById(TestData.budget5Id)
        val account = Account("account", budget.id)
        accountDao.insert(account)
        val operation1 = Operation(account.id, TestData.feb_02_2021, Category.INCOME_ID, 150.65, "mémo")
        val operation2 = Operation(account.id, TestData.march_02_2021, Category.INCOME_ID, -100.00, "reimbursement")
        operationDao.insert(operation1)
        operationDao.insert(operation2)

        val account2 = Account("account2", budget.id)
        accountDao.insert(account2)
        val operation3 = Operation(account2.id, TestData.feb_02_2021, Category.INCOME_ID, 77.77, "mémo")
        operationDao.insert(operation3)

        val accountList = accountDao.findByBudget(budget)
        val expectedList = listOf(
            AccountWithAmount.createFromAccount(account, 50.65),
            AccountWithAmount.createFromAccount(account2, 77.77)
        )

        Assertions.assertEquals(expectedList.toString(), accountList.toString())
    }

    @Test
    fun getAccountWithNullAmountForBudget() {
    }

    @Test
    fun getAccountWithNoOperationForBudget() {

    }

    @Test
    fun getAccountWithNegativeAmountForBudget() {
    }
}
