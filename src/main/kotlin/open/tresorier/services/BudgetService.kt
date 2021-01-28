package open.tresorier.services

import open.tresorier.dao.IBudgetDao
import open.tresorier.model.Budget
import open.tresorier.model.Person

class BudgetService(private val budgetDao: IBudgetDao) {

    /* return either the created person or null if the creation failed
     ex : person does not exist already
     */
    fun createBudget(name: String, person: Person): Budget? {
        val budget = Budget(name, person.id)
        return budgetDao.insert(budget)
    }

    fun update(budget: Budget) : Budget?{
        return budgetDao.update(budget)
    }

}
