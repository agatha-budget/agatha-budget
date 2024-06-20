package open.tresorier.dao

import open.tresorier.model.Budget
import open.tresorier.model.Person

interface IBudgetDao {
    fun insert(budget: Budget) : Budget
    fun update(budget: Budget) : Budget
    fun getById(id: String): Budget
    fun findByPersonId(personId: String) : List<Budget>
    fun getOwner(budget: Budget) : Person

}
