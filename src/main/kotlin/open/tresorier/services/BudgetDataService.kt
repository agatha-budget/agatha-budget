package open.tresorier.services

import open.tresorier.dao.IAllocationDao
import open.tresorier.dao.IOperationDao
import open.tresorier.model.*

typealias BudgetData = MutableMap<Int, MutableMap<String, CategoryData>>

class BudgetDataService(private val allocationDao: IAllocationDao,
                        private val operationDao: IOperationDao,
                        private val authorizationService: AuthorizationService) {

    fun getBudgetData(person: Person, budget: Budget, startMonth: Month? = null, endMonth: Month? = null): BudgetData {
        authorizationService.cancelIfUserIsUnauthorized(person, budget)
        val allocations = allocationDao.findByBudget(budget, endMonth)
        val spendings = operationDao.findTotalSpendingByMonth(budget, endMonth)
        var data: BudgetData = mutableMapOf()
        for (allocation in allocations){ data = addAllocation(allocation, data) }
        for (spending in spendings){ data = addSpending(spending, data) }
        data = computeAvailable(data)
        return data
    }
    companion object {

        fun addAllocation(allocation: Allocation, data: BudgetData): BudgetData {
            val categoryId = allocation.categoryId
            val newCategoryData = CategoryData(allocation.amount)

            data[allocation.month.comparable]?.let { mapByMonth ->
                mapByMonth[categoryId]?.let { categoryData ->
                    categoryData.allocated = allocation.amount
                } ?: run {
                    mapByMonth[categoryId] = newCategoryData
                }
            } ?: run {
                data[allocation.month.comparable] = mutableMapOf(Pair(categoryId, newCategoryData))
            }
            return data
        }

        fun addSpending(spending: Spending, data: BudgetData): BudgetData {
            val categoryId = spending.categoryId
            val newCategoryData = CategoryData(allocated = 0.00, spending.amount)

            data[spending.month.comparable]?.let { mapByMonth ->
                mapByMonth[categoryId]?.let { categoryData ->
                    categoryData.spent = spending.amount
                } ?: run {
                    mapByMonth[categoryId] = newCategoryData
                }
            } ?: run {
                data[spending.month.comparable] = mutableMapOf(Pair(categoryId, newCategoryData))
            }
            return data
        }

        fun computeAvailable(data: BudgetData): BudgetData {
            val sortedMonth = data.keys.sortedWith(compareBy { it })
            val totalSpent: MutableMap<String, Double> = mutableMapOf()
            val totalAllocated: MutableMap<String, Double> = mutableMapOf()
            for (month in sortedMonth) {
                data[month]?.let { mapByMonth ->
                    for ((categoryId, categoryData) in mapByMonth) {
                        totalSpent[categoryId] = categoryData.spent + (totalSpent[categoryId] ?: 0.00)
                        totalAllocated[categoryId] = categoryData.allocated + (totalAllocated[categoryId] ?: 0.00)
                        categoryData.available = (totalAllocated[categoryId] ?: 0.00) - (totalSpent[categoryId] ?: 0.00)
                    }
                }
            }
            return data
        }
    }
}

