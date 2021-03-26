package open.tresorier.services

import open.tresorier.dao.IBudgetDao
import open.tresorier.model.Budget
import open.tresorier.model.Person

class BudgetService(private val budgetDao: IBudgetDao, private val authorizationService: AuthorizationService) {

    fun create(person: Person, name: String): Budget {
        val budget = Budget(name, person.id)
        return budgetDao.insert(budget)
    }

    fun update(person: Person, budget: Budget, newName: String): Budget {
        authorizationService.cancelIfUserIsUnauthorized(person, budget)
        budget.name = newName
        return budgetDao.update(budget)
    }

    fun getById(person: Person, id: String): Budget {
        val budget = budgetDao.getById(id)
        authorizationService.cancelIfUserIsUnauthorized(person, budget)
        return budget
    }

    fun delete(person: Person, budget: Budget) : Budget {
        authorizationService.cancelIfUserIsUnauthorized(person, budget)
        budget.deleted = true
        return budgetDao.update(budget)
    }

    fun findByUser(person: Person) : List<Budget> {
        return budgetDao.findByPersonId(person.id)
    }
}
