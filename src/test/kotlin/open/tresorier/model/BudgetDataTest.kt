package open.tresorier.model

import open.tresorier.dependenciesinjection.ITest
import open.tresorier.utils.TestData
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class BudgetDataTest : ITest {

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

        val budgetData = BudgetData(allocationList, listOf()).data

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

        val budgetData = BudgetData(listOf(), spendingList).data

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

        val budgetData = BudgetData(allocationList, listOf()).data

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

        val budgetData = BudgetData(listOf(), spendingList).data

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

        val budgetData = BudgetData(allocationList, spendingList).data

        Assertions.assertEquals(10.00, budgetData[TestData.dec_2020.comparable]?.get(categoryId)?.available)
        Assertions.assertEquals(0.00, budgetData[TestData.mar_2022.comparable]?.get(categoryId)?.available)
    }
}
