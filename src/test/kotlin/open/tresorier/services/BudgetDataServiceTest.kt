package open.tresorier.services

import open.tresorier.dao.*
import open.tresorier.dependenciesinjection.ITest
import open.tresorier.model.*
import open.tresorier.utils.TestData
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.koin.core.component.inject

class BudgetDataServiceTest : ITest {

    val budgetDataService by inject<BudgetDataService>()
    val budgetDao by inject<IBudgetDao>()
    val masterCategoryDao by inject<IMasterCategoryDao>()
    val categoryDao by inject<ICategoryDao>()
    val allocationDao by inject<IAllocationDao>()
    val accountDao by inject<IAccountDao>()
    val operationDao by inject<IOperationDao>()
    val personDao by inject<IPersonDao>()

    @Test
    fun testAddAllocation() {
        val categoryId = "categoryId"
        val allocationList = listOf(
                Allocation(TestData.jan_2020,categoryId,40.00),
                Allocation(TestData.dec_2020,categoryId,20.00),
                Allocation(TestData.jan_2021,categoryId,20.00),
                Allocation(TestData.feb_2022,categoryId,20.00),
                Allocation(TestData.mar_2022,categoryId,20.00)
        )
        var budgetData : BudgetData = mutableMapOf()
        for (allocation in allocationList) {
            budgetData = BudgetDataService.addAllocation(allocation, budgetData)
        }
        Assertions.assertEquals(20.00, budgetData[TestData.jan_2021.comparable]?.get(categoryId)?.allocated)
    }

    @Test
    fun testAddSpending() {
        val categoryId = "categoryId"
        val spendingList = listOf(
                Spending(TestData.jan_2020,categoryId,40.00),
                Spending(TestData.dec_2020,categoryId,10.00),
                Spending(TestData.jan_2021,categoryId,20.00),
        )
        var budgetData : BudgetData = mutableMapOf()
        for (spending in spendingList) {
            budgetData = BudgetDataService.addSpending(spending, budgetData)
        }
        Assertions.assertEquals(10.00, budgetData[TestData.dec_2020.comparable]?.get(categoryId)?.spent)
    }

    @Test
    fun testComputeAvailableWithNoSpending() {
        val categoryId = "categoryId"
        val allocationList = listOf(
                Allocation(TestData.jan_2020,categoryId,40.00),
                Allocation(TestData.dec_2020,categoryId,20.00),
                Allocation(TestData.jan_2021,categoryId,20.00),
                Allocation(TestData.feb_2022,categoryId,20.00),
                Allocation(TestData.mar_2022,categoryId,20.00)
        )
        var budgetData : BudgetData = mutableMapOf()
        for (allocation in allocationList) {
            budgetData = BudgetDataService.addAllocation(allocation, budgetData)
        }
        budgetData = BudgetDataService.computeAvailable(budgetData)

        Assertions.assertEquals(60.00, budgetData[TestData.dec_2020.comparable]?.get(categoryId)?.available)
        Assertions.assertEquals(120.00, budgetData[TestData.mar_2022.comparable]?.get(categoryId)?.available)
    }

    @Test
    fun testComputeAvailableWithNoAllocation() {
        val categoryId = "categoryId"
        val spendingList = listOf(
                Spending(TestData.jan_2020,categoryId,40.00),
                Spending(TestData.dec_2020,categoryId,10.00),
                Spending(TestData.jan_2021,categoryId,20.00),
        )
        var budgetData : BudgetData = mutableMapOf()
        for (spending in spendingList) {
            budgetData = BudgetDataService.addSpending(spending, budgetData)
        }
        budgetData = BudgetDataService.computeAvailable(budgetData)

        Assertions.assertEquals(-50.00, budgetData[TestData.dec_2020.comparable]?.get(categoryId)?.available)
        Assertions.assertEquals(-70.00, budgetData[TestData.jan_2021.comparable]?.get(categoryId)?.available)
    }

    @Test
    fun testComputeAvailableWithAllocationAndSpending() {
        val categoryId = "categoryId"
        val allocationList = listOf(
                Allocation(TestData.jan_2020,categoryId,40.00),
                Allocation(TestData.dec_2020,categoryId,20.00),
                Allocation(TestData.jan_2021,categoryId,20.00),
                Allocation(TestData.feb_2022,categoryId,20.00),
                Allocation(TestData.mar_2022,categoryId,20.00)
        )
        val spendingList = listOf(
                Spending(TestData.jan_2020,categoryId,40.00),
                Spending(TestData.dec_2020,categoryId,10.00),
                Spending(TestData.jan_2021,categoryId,70.00),
        )
        var budgetData : BudgetData = mutableMapOf()
        for (spending in spendingList) {
            budgetData = BudgetDataService.addSpending(spending, budgetData)
        }
        for (allocation in allocationList) {
            budgetData = BudgetDataService.addAllocation(allocation, budgetData)
        }
        budgetData = BudgetDataService.computeAvailable(budgetData)

        Assertions.assertEquals(10.00, budgetData[TestData.dec_2020.comparable]?.get(categoryId)?.available)
        Assertions.assertEquals(0.00, budgetData[TestData.mar_2022.comparable]?.get(categoryId)?.available)
    }

    @Test
    fun testFindCategoriesData() {
        val budget = Budget("wellAllocatedBudget", TestData.person1Id)
        budgetDao.insert(budget)
        val masterCategory = MasterCategory("Fixed expense", budget.id)
        masterCategoryDao.insert(masterCategory)
        val category = Category("oftenAllocatedCategory", masterCategory.id)
        categoryDao.insert(category)
        val allocationList = listOf(
                Allocation(TestData.jan_2020,category.id,40.00),
                Allocation(TestData.dec_2020,category.id,20.00),
                Allocation(TestData.jan_2021,category.id,20.00),
                Allocation(TestData.feb_2022,category.id,20.00),
                Allocation(TestData.mar_2022,category.id,20.00)

        )
        for (allocation in allocationList) {
            allocationDao.insert(allocation)
        }
        val category2 = Category("lessoftenAllocatedCategory", masterCategory.id)
        categoryDao.insert(category2)
        val account = Account("my own account", budget.id)
        accountDao.insert(account)
        val operationList = listOf(
                Operation( TestData.nov_02_2020 ,account.id, category.id,40.00),
                Operation( TestData.nov_03_2020 ,account.id, category.id,20.00),
                Operation( TestData.feb_02_2021 ,account.id, category2.id,10.00),
                Operation( TestData.march_02_2021 ,account.id, category.id,30.00),

                )
        for (operation in operationList) {
            operationDao.insert(operation)
        }
        val person: Person = personDao.getById(TestData.person1Id)
        val budgetData = budgetDataService.getBudgetData(person, budget)

        Assertions.assertEquals(40.00, budgetData[TestData.jan_2020.comparable]?.get(category.id)?.allocated)
        Assertions.assertEquals(0.00, budgetData[TestData.nov_2020.comparable]?.get(category.id)?.allocated)
        Assertions.assertEquals(60.00, budgetData[TestData.nov_2020.comparable]?.get(category.id)?.spent)
        Assertions.assertEquals(-20.00, budgetData[TestData.nov_2020.comparable]?.get(category.id)?.available)
        Assertions.assertEquals(-10.00, budgetData[TestData.feb_2021.comparable]?.get(category2.id)?.available)
        Assertions.assertEquals(null, budgetData[TestData.feb_2022.comparable]?.get(category2.id)?.available)


    }

    @Test
    fun testFindCategoriesDataWithNoSpending() {

    }

    @Test
    fun testFindCategoriesDataWithNoAllocation() {

    }

    @Test
    fun testFindCategoriesDataForEmptyMonth() {

    }

    @Test
    fun testFindCategoriesDataForEmptyBudget() {

    }

    @Test
    fun testFindCategoriesDataForNonExistingBudget() {

    }
}
