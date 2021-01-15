package open.tresorier.dao

import open.tresorier.model.Budget
import open.tresorier.model.Person

interface IBudgetDao {

    // return null if an error occured (duplicate unique property etc..)
    fun insert(budget: Budget) : Budget?
    fun update(budget: Budget)
    fun delete(budget: Budget)
    fun getById(id: String): Budget?
    fun findByPersonId(personId: String) : List<Budget>

}
