package open.tresorier.services

import open.tresorier.dao.IAllocationDao
import open.tresorier.dao.IOperationDao
import open.tresorier.model.*

class BudgetDataService(private val allocationDao: IAllocationDao,
                        private val operationDao: IOperationDao,
                        private val authorizationService: AuthorizationService) {

    fun getBudgetData(person: Person, budget: Budget, startMonth: Month?= null, endMonth: Month? = null): BudgetData {
        authorizationService.cancelIfUserIsUnauthorized(person, budget)
        val allocations = allocationDao.findByBudget(budget, endMonth)
        val spendings = operationDao.findTotalSpendingByMonth(budget, endMonth)
        var data = BudgetData()
        for (allocation in allocations){ data = addAllocation(allocation, data) }
        for (spending in spendings){ data = addSpending(spending, data) }
        startMonth?.let {
            data = initStartMonthIfNeeded(data, it)
        }
        data = computeAvailable(data)
        startMonth?.let {
            data = extractDataForPeriod(data, it, endMonth)
        }
        return data
    }
    companion object {

        fun extractDataForPeriod(data: BudgetData, startMonth: Month, endMonth: Month? = null) : BudgetData{
            endMonth?.let {Month.cancelIfEndLessThanStart(startMonth, endMonth)}
            // will get until endMonth or last month
            // if last month is null meaning the data set is empty :
            // will use month before startMonth to cut the loop short and return an empty map
            val end : Month = endMonth ?: data.getLastMonth() ?: startMonth.findBefore()
            val extractedData = BudgetData()
            var month = startMonth
            while (month.comparable <= end.comparable){
                data[month.comparable]?.let {
                    extractedData[month.comparable] = it
                }
                month = month.findNext()
            }
            return extractedData
        }

        fun initStartMonthIfNeeded(data: BudgetData, startMonth: Month) : BudgetData {
            val listOfKnownCategory = this.getAllKnownCategoriesUntilMonth(data, startMonth)
            val monthData : MonthData = data[startMonth.comparable] ?: MonthData()
            for (categoryId in listOfKnownCategory){
                if (categoryId !in monthData.keys){
                    monthData[categoryId] = CategoryData()
                }
            }
            data[startMonth.comparable] = monthData
            return data
        }

        fun getAllKnownCategoriesUntilMonth(data: BudgetData, maxMonth: Month) : List<String> {
            val categoryList = mutableListOf<String>()
            var month = data.getFirstMonth() ?: maxMonth
            while (month.comparable < maxMonth.comparable){
                data[month.comparable]?.let {
                    for (categoryId in it.keys){
                        if (categoryId !in categoryList){
                            categoryList.add(categoryId)
                        }
                    }
                }
                month = month.findNext()
            }
            return categoryList
        }

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
                data[allocation.month.comparable] = MonthData().set(categoryId, newCategoryData)
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
                data[spending.month.comparable] = MonthData().set(categoryId, newCategoryData)
            }
            return data
        }

        fun computeAvailable(data: BudgetData): BudgetData {
            val sortedMonth = data.getSortedMonths()
            val totalSpent: MutableMap<String, Double> = mutableMapOf()
            val totalAllocated: MutableMap<String, Double> = mutableMapOf()
            for (month in sortedMonth) {
                data[month]?.let { mapByMonth ->
                    for ((categoryId, categoryData) in mapByMonth) {
                        totalSpent[categoryId] = categoryData.spent + (totalSpent[categoryId] ?: 0.00)
                        totalAllocated[categoryId] = categoryData.allocated + (totalAllocated[categoryId] ?: 0.00)
                        val notRoundedAvailable = (totalAllocated[categoryId] ?: 0.00) + (totalSpent[categoryId] ?: 0.00)
                        categoryData.available = "%.2f".format(notRoundedAvailable).toDouble()
                    }
                }
            }
            return data
        }
    }
}

