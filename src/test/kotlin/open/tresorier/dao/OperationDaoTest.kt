package open.tresorier.dao


import open.tresorier.dependenciesinjection.ITest
import open.tresorier.exception.TresorierException
import open.tresorier.model.*
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
        val operation = Operation( "545", TestData.feb_02_2021,"56", -25.54, "achat")
        val exception = Assertions.assertThrows(TresorierException::class.java) {
            operationDao.getOwner(operation)
        }
        Assertions.assertEquals("the given object appears to have no owner", exception.message)
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
        val budget = Budget("wellAllocatedBudget", "person1")
        budgetDao.insert(budget)
        val masterCategory = MasterCategory("Fixed expense", budget.id)
        masterCategoryDao.insert(masterCategory)
        val category = Category("oftenAllocatedCategory", masterCategory.id)
        categoryDao.insert(category)
        val category2 = Category("lessoftenAllocatedCategory", masterCategory.id)
        categoryDao.insert(category2)
        val account = Account("my own account", budget.id)
        accountDao.insert(account)
        val operationList = listOf(
                Operation( account.id, TestData.nov_02_2020 , category.id,40.00),
                Operation(account.id, TestData.nov_03_2020 , category.id,20.00),
                Operation(account.id, TestData.feb_02_2021 , category2.id,10.00),
                Operation(account.id, TestData.march_02_2021 , category.id,30.00),

                )
        for (operation in operationList) {
            operationDao.insert(operation)
        }
        val result = operationDao.findTotalSpendingByMonth(budget)
        Assertions.assertEquals(3, result.size)
        Assertions.assertEquals(11, result[0].month.month)
        Assertions.assertEquals(2020, result[0].month.year)
        Assertions.assertEquals(category.id, result[0].categoryId)
        Assertions.assertEquals(60.00, result[0].amount)

        Assertions.assertEquals(2, result[1].month.month)
        Assertions.assertEquals(2021, result[1].month.year)
        Assertions.assertEquals(category2.id, result[1].categoryId)
        Assertions.assertEquals(10.00, result[1].amount)

        Assertions.assertEquals(3, result[2].month.month)
        Assertions.assertEquals(2021, result[2].month.year)
        Assertions.assertEquals(category.id, result[2].categoryId)
        Assertions.assertEquals(30.00, result[2].amount)
    }

    @Test
    fun getSpendingByMonthByCategoryUntilFebruary() {
        val budget = Budget("wellAllocatedBudget", "person1")
        budgetDao.insert(budget)
        val masterCategory = MasterCategory("Fixed expense", budget.id)
        masterCategoryDao.insert(masterCategory)
        val category = Category("oftenAllocatedCategory", masterCategory.id)
        categoryDao.insert(category)
        val category2 = Category("lessoftenAllocatedCategory", masterCategory.id)
        categoryDao.insert(category2)
        val account = Account("my own account", budget.id)
        accountDao.insert(account)
        val operationList = listOf(
                Operation(account.id, TestData.nov_02_2020 , category.id,40.00),
                Operation(account.id, TestData.nov_03_2020 , category.id,20.00),
                Operation(account.id, TestData.feb_02_2021 , category2.id,10.00),
                Operation(account.id, TestData.march_02_2021 , category.id,30.00),

                )
        for (operation in operationList) {
            operationDao.insert(operation)
        }
        val result = operationDao.findTotalSpendingByMonth(budget, Month(2,2021))
        Assertions.assertEquals(2, result.size)
        Assertions.assertEquals(11, result[0].month.month)
        Assertions.assertEquals(2020, result[0].month.year)
        Assertions.assertEquals(category.id, result[0].categoryId)
        Assertions.assertEquals(60.00, result[0].amount)

        Assertions.assertEquals(2, result[1].month.month)
        Assertions.assertEquals(2021, result[1].month.year)
        Assertions.assertEquals(category2.id, result[1].categoryId)
        Assertions.assertEquals(10.00, result[1].amount)
    }

    @Test
    fun getAllAllocationsOfNonExistingBudget() {
        val budget = Budget("wellAllocatedBudget", "person1")
        val result = operationDao.findTotalSpendingByMonth(budget)
        Assertions.assertEquals(0, result.size)
    }
}
