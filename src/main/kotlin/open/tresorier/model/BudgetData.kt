package open.tresorier.model

class BudgetData ( private val allocations : List<Allocation>, private val spendings : List<Spending> )
{

    val data: MutableMap<Int, MutableMap<String, CategoryData>> = mutableMapOf()

    init {
        for (allocation in allocations){
            addAllocation(allocation)
        }
        for (spending in spendings){
            addSpending(spending)
        }
        computeAvailable()
    }

    private fun addAllocation(allocation: Allocation){
        val categoryId = allocation.categoryId
        val newCategoryData = CategoryData(categoryId, allocation.amount)

        data[allocation.month.comparable]?.let {mapByMonth ->
            mapByMonth[categoryId]?.let { categoryData ->
                categoryData.allocated = allocation.amount
            } ?: run {
                mapByMonth.put(categoryId, newCategoryData)
            }
        } ?: run {
            data.put(allocation.month.comparable, mutableMapOf(Pair(categoryId, newCategoryData)))
        }
    }

    private fun addSpending(spending: Spending){
        val categoryId = spending.categoryId
        val newCategoryData = CategoryData(categoryId, allocated = 0.00, spending.amount)

        data[spending.month.comparable]?.let {mapByMonth ->
            mapByMonth[categoryId]?.let { categoryData ->
                categoryData.spent = spending.amount
            } ?: run {
                mapByMonth.put(categoryId, newCategoryData)
            }
        } ?: run {
            data.put(spending.month.comparable, mutableMapOf(Pair(categoryId, newCategoryData)))
        }
    }

    private fun computeAvailable(){
        val sortedMonth = data.keys.sortedWith(compareBy { it })
        val totalSpent: MutableMap<String, Double> = mutableMapOf()
        val totalAllocated: MutableMap<String, Double> = mutableMapOf()
        for (month in sortedMonth){
            data[month]?.let { mapByMonth ->
                for (categoryData in mapByMonth.values){
                    val categoryId = categoryData.categoryId
                    totalSpent[categoryId] = categoryData.spent + (totalSpent[categoryId]?:0.00)
                    totalAllocated[categoryId] = categoryData.allocated + (totalAllocated[categoryId]?:0.00)
                    categoryData.available = (totalAllocated[categoryId]?:0.00) - (totalSpent[categoryId]?:0.00)
                }
            }
        }
    }

}