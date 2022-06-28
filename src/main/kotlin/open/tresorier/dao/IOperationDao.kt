package open.tresorier.dao

import open.tresorier.model.*

interface IOperationDao {
    fun insert(operation: Operation) : Operation
    fun update(operation: Operation) : Operation
    fun delete(operation: Operation)
    fun getById(id: String): Operation
    fun findTotalSpendingByMonth(budget: Budget, maxMonth: Month? = null) : List<Spending>
    fun findAmountByBudget(budget: Budget, month: Month? = null) : Int
    fun findByAccount(account: Account, category: Category?) : List<Operation>
    fun findByBudget(budget: Budget, category: Category?) : List<Operation>
    fun getOwner(operation: Operation) : Person
}