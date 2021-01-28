package open.tresorier.dao

import open.tresorier.model.Budget

interface IBudgetDao {

    // return null if an error occurred (duplicate unique property etc..)
    fun insert(budget: Budget) : Budget?
    fun update(budget: Budget) : Budget?
    fun delete(budget: Budget)
    fun getById(id: String): Budget?
    fun findByPersonId(personId: String) : List<Budget>

}
