package open.tresorier.services

import open.tresorier.dao.IBudgetDao
import open.tresorier.exception.TresorierIllegalException
import open.tresorier.model.Budget
import open.tresorier.model.Person

class BudgetService(private val budgetDao: IBudgetDao) {

    fun createBudget(person: Person, name: String): Budget {
        val budget = Budget(name, person.id)
        return budgetDao.insert(budget)
    }

    fun update(person: Person, budget: Budget, newName: String): Budget {
        cancelIfUserIsUnauthorized(person, budget)
        budget.name = newName
        return budgetDao.update(budget)
    }

    fun getById(person: Person, id: String): Budget {
        val budget = budgetDao.getById(id)
        cancelIfUserIsUnauthorized(person, budget)
        return budget
    }

    fun delete(person: Person, budget: Budget) : Budget {
        cancelIfUserIsUnauthorized(person, budget)
        budget.deleted = true
        return budgetDao.update(budget)
    }

    private fun cancelIfUserIsUnauthorized(person: Person, budget: Budget) {
        if (budget.personId != person.id) {
            throw TresorierIllegalException("user " + person.id + "isn't allowed to interact with budget " + budget.id)
        }
    }

}
