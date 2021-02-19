package open.tresorier.dao

import open.tresorier.model.Account
import open.tresorier.model.Person

interface IAccountDao {
    fun insert(account: Account) : Account
    fun update(account: Account) : Account
    fun getById(id: String): Account
    fun findByBudgetId(budgetId: String) : List<Account>
    fun getAccountOwner(account: Account) : Person
}
