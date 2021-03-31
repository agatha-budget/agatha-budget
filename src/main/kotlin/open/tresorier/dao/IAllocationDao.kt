package open.tresorier.dao

import open.tresorier.model.Allocation
import open.tresorier.model.Budget
import open.tresorier.model.Month
import open.tresorier.model.Person

interface IAllocationDao {
    fun insert(allocation: Allocation) : Allocation
    fun update(allocation: Allocation) : Allocation
    fun getById(id: String): Allocation
    fun findByBudgetUntilMonth(budget: Budget, maxMonth: Month? = null) : List<Allocation>
    fun getOwner(allocation: Allocation) : Person
}