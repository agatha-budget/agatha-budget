package open.tresorier.dao

import open.tresorier.model.*

interface IAllocationDao {
    fun insertOrUpdate(allocation: Allocation) : Allocation
    fun getByIdentifiers(category: Category, month: Month): Allocation
    fun findByBudget(budget: Budget, maxMonth: Month? = null) : List<Allocation>
    fun getOwner(allocation: Allocation) : Person
}