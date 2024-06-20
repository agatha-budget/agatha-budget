package open.tresorier.dao


import open.tresorier.dependenciesinjection.ITest
import open.tresorier.exception.TresorierException
import open.tresorier.model.*
import open.tresorier.model.enum.ProfileEnum
import open.tresorier.utils.TestData
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.koin.core.component.inject

open class OperationDaoTest : ITest {

    val operationDao by inject<IOperationDao>()
    val personDao by inject<IPersonDao>()
    val budgetDao by inject<IBudgetDao>()
    val accountDao by inject<IAccountDao>()
    val masterCategoryDao by inject<IMasterCategoryDao>()
    val categoryDao by inject<ICategoryDao>()

    @Test
    fun getOwner() {
        val operation = operationDao.getById("operation1")
        val expectedOwner = personDao.getById("person1")
        val owner = operationDao.getOwner(operation)
        Assertions.assertEquals(expectedOwner.email, owner.email)
    }

    @Test
    fun getOwnerForUnstored() {
        val operation = Operation( "545", TestData.feb_02_2021,"56", -2554, 1, "achat", false, false)
        val exception = Assertions.assertThrows(TresorierException::class.java) {
            operationDao.getOwner(operation)
        }
        Assertions.assertEquals("the given operation (${operation}) appears to have no owner", exception.message)
    }

    @Test
    open fun cannotCreateWithAccountAndCategoryFromDistinctBudget() {
        // require postgresql implementation so is not available for unit test -> check OperationDaoIntTest
    }

    @Test
    open fun  cannotCreateWithNullCategoryIsOk() {
        // require postgresql implementation so is not available for unit test -> check OperationDaoIntTest
    }

    @Test
    fun getSpendingByMonthByCategoryForAllMonths() {
        val budget = Budget("wellAllocatedBudget", "person1", ProfileEnum.PROFILE_USER)
        budgetDao.insert(budget)
        val masterCategory = MasterCategory("Fixed expense", budget.id, null)
        masterCategoryDao.insert(masterCategory)
        val category = Category("oftenAllocatedCategory", masterCategory.id)
        categoryDao.insert(category)
        val category2 = Category("lessoftenAllocatedCategory", masterCategory.id)
        categoryDao.insert(category2)
        val account = Account("my own account", budget.id)
        accountDao.insert(account)
        val operationList = listOf(
                Operation(account.id, TestData.nov_02_2020, category.id, 4000, 1, null, false, false),
                Operation(account.id, TestData.nov_03_2020, category.id, 2000, 2, null, false, false),
                Operation(account.id, TestData.feb_02_2021, category2.id, 1000, 3, null, false, false),
                Operation(account.id, TestData.march_02_2021, category.id, 3000, 4, null, false, false),

                )
        for (operation in operationList) {
            operationDao.insert(operation)
        }
        val result = operationDao.findTotalSpendingByMonth(budget)
        Assertions.assertEquals(3, result.size)
        Assertions.assertEquals(11, result[0].month.month)
        Assertions.assertEquals(2020, result[0].month.year)
        Assertions.assertEquals(category.id, result[0].categoryId)
        Assertions.assertEquals(6000, result[0].amount)

        Assertions.assertEquals(2, result[1].month.month)
        Assertions.assertEquals(2021, result[1].month.year)
        Assertions.assertEquals(category2.id, result[1].categoryId)
        Assertions.assertEquals(1000, result[1].amount)

        Assertions.assertEquals(3, result[2].month.month)
        Assertions.assertEquals(2021, result[2].month.year)
        Assertions.assertEquals(category.id, result[2].categoryId)
        Assertions.assertEquals(3000, result[2].amount)
    }

    @Test
    fun getSpendingByMonthByCategoryUntilFebruary() {
        val budget = Budget("wellAllocatedBudget", "person1", ProfileEnum.PROFILE_USER)
        budgetDao.insert(budget)
        val masterCategory = MasterCategory("Fixed expense", budget.id, null)
        masterCategoryDao.insert(masterCategory)
        val category = Category("oftenAllocatedCategory", masterCategory.id)
        categoryDao.insert(category)
        val category2 = Category("lessoftenAllocatedCategory", masterCategory.id)
        categoryDao.insert(category2)
        val account = Account("my own account", budget.id)
        accountDao.insert(account)
        val operationList = listOf(
                Operation(account.id, TestData.nov_02_2020, category.id, 4000, 1, null, false, false),
                Operation(account.id, TestData.nov_03_2020, category.id, 2000, 2, null, false, false),
                Operation(account.id, TestData.feb_02_2021, category2.id, 1000, 3, null, false, false),
                Operation(account.id, TestData.march_02_2021, category.id, 3000, 4, null, false, false),

                )
        for (operation in operationList) {
            operationDao.insert(operation)
        }
        val result = operationDao.findTotalSpendingByMonth(budget, Month(2,2021))
        Assertions.assertEquals(2, result.size)
        Assertions.assertEquals(11, result[0].month.month)
        Assertions.assertEquals(2020, result[0].month.year)
        Assertions.assertEquals(category.id, result[0].categoryId)
        Assertions.assertEquals(6000, result[0].amount)

        Assertions.assertEquals(2, result[1].month.month)
        Assertions.assertEquals(2021, result[1].month.year)
        Assertions.assertEquals(category2.id, result[1].categoryId)
        Assertions.assertEquals(1000, result[1].amount)
    }

    @Test
    fun getAllAllocationsOfNonExistingBudget() {
        val budget = Budget("wellAllocatedBudget", "person1", ProfileEnum.PROFILE_USER)
        val result = operationDao.findTotalSpendingByMonth(budget)
        Assertions.assertEquals(0, result.size)
    }

    @Test
    fun getAmountForBudget() {
        val budget = Budget("professional budget", "person1", ProfileEnum.PROFILE_USER)
        budgetDao.insert(budget)
        val masterCategory = MasterCategory("Fixed expense", budget.id, null)
        masterCategoryDao.insert(masterCategory)
        val category = Category("oftenAllocatedCategory", masterCategory.id)
        categoryDao.insert(category)
        val accountA = Account("my own account", budget.id)
        accountDao.insert(accountA)
        val accountB = Account("my own account", budget.id)
        accountDao.insert(accountB)

        val budget2= Budget("personal budget", "person1", ProfileEnum.PROFILE_USER)
        budgetDao.insert(budget2)
        val masterCategory2 = MasterCategory("Fixed expense", budget2.id, null)
        masterCategoryDao.insert(masterCategory2)
        val category2 = Category("oftenAllocatedCategory", masterCategory2.id)
        categoryDao.insert(category2)
        val account2 = Account("my own account", budget2.id)
        accountDao.insert(account2)

        val operationList = listOf(
            Operation(accountA.id, TestData.nov_02_2020, category.id, 4000, 1, null, false, false),
            Operation(accountB.id, TestData.nov_03_2020, Category.INCOME_ID, 2000, 2, "income", false, false),
            Operation(accountA.id, TestData.nov_03_2020, Category.TRANSFERT_ID, - 3000, 3, "transfert to B", false, false),
            Operation(accountB.id, TestData.nov_03_2020, Category.TRANSFERT_ID, 3000, 4, "transfert from A", false, false),
            Operation(accountB.id, TestData.march_02_2021, category.id, 5600, 5, "again", false, false),
            Operation(account2.id, TestData.nov_02_2020, category2.id, 4000, 6, "not in the right budget", false, false),
        )
        for (operation in operationList) {
            operationDao.insert(operation)
        }

        val amountBeforeEverything = operationDao.findAmountByBudget(budget, TestData.jan_2020)
        Assertions.assertEquals(0, amountBeforeEverything)

        val amountInNovember = operationDao.findAmountByBudget(budget, TestData.nov_2020)
        Assertions.assertEquals(6000, amountInNovember)

        val amountNow = operationDao.findAmountByBudget(budget)
        Assertions.assertEquals(11600, amountNow)
    }

    @Test
    fun getAmountForEmptyBudget() {
        val budget = Budget("professional budget", "person1", ProfileEnum.PROFILE_USER)
        budgetDao.insert(budget)
        val amountNow = operationDao.findAmountByBudget(budget)
        Assertions.assertEquals(0, amountNow)
    }

    @Test
    fun getAmountForBudgetWithDaughterOperation() {
        val budget = Budget("professional budget", "person1", ProfileEnum.PROFILE_USER)
        budgetDao.insert(budget)
        val account = Account("my own account", budget.id)
        accountDao.insert(account)
        val operationList = listOf(
                Operation(account.id, TestData.nov_02_2020, null, 4000, 1),
        )
        val operationDaughters = listOf (
            Operation(account.id, TestData.nov_02_2020, null, 3000, 1, null, false, false, operationList[0].id),
            Operation(account.id, TestData.nov_02_2020, null, 1000, 1, null, false, false, operationList[0].id),

        )
        for (operation in operationList) {
            operationDao.insert(operation)
        }
        for (operation in operationDaughters) {
            operationDao.insert(operation)
        }
        val amountNow = operationDao.findAmountByBudget(budget)
        Assertions.assertEquals(4000, amountNow)
    }

    @Test
    fun duplicateImportIdentifierAreIgnoredWhenInserted() {
        val budget = Budget("professional budget", "person1", ProfileEnum.PROFILE_USER)
        budgetDao.insert(budget)
        val account = Account("my own account", budget.id)
        accountDao.insert(account)
        val importedOperation = Operation(account.id, TestData.nov_02_2020, null, 4000, 1, null, false, false, null, "importIdentifier must be unique")
        operationDao.insert(importedOperation)
        val importedTwiceOperation = Operation(account.id, TestData.nov_02_2020, null, 4000, 1, null, false, false, null, "importIdentifier must be unique")
        operationDao.insert(importedTwiceOperation)
        Assertions.assertThrows(TresorierException::class.java) {
            operationDao.getById(importedTwiceOperation.id)
        }
    }
}
