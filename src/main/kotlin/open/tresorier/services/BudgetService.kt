package open.tresorier.services

import open.tresorier.dao.IBudgetDao
import open.tresorier.exception.TresorierIllegalException
import open.tresorier.model.Budget
import open.tresorier.model.Person

class BudgetService(private val budgetDao: IBudgetDao) {

    /* return either the created person or null if the creation failed
     ex : person does not exist already
     */
    fun createBudget(name: String, person: Person): Budget {
        val budget = Budget(name, person.id)
        return budgetDao.insert(budget)
    }

    fun update(person: Person, budget: Budget, newName: String): Budget {
        cancelIfUserIsUnauthorized(person, budget)
        budget.name = newName
        return budgetDao.update(budget)
    }

    fun getById(id: String): Budget {
        return budgetDao.getById(id)
    }

    fun cancelIfUserIsUnauthorized(person: Person, budget: Budget) {
        if (budget.personId != person.id) {
            throw TresorierIllegalException("user " + person.id + "isn't allowed to interact with budget " + budget.id)
        }
    }

}
