package open.tresorier.services

import open.tresorier.dao.ICategoryIdentifierDao
import open.tresorier.model.*

class CategoryIdentifierService(private val categoryIdentifierDao: ICategoryIdentifierDao, private val authorizationService: AuthorizationService, private val budgetService: BudgetService) {

    fun create(person: Person, budget: Budget, pattern: String, categoryId: String): CategoryIdentifier {
        authorizationService.cancelIfUserIsUnauthorized(person, budget)
        val categoryIdentifier = CategoryIdentifier(budget.id, pattern, categoryId)
        return categoryIdentifierDao.insert(categoryIdentifier)
    }

    fun update(person: Person, categoryIdentifier: CategoryIdentifier, newPattern: String?, newCategoryId: String?): CategoryIdentifier {
        val budget = budgetService.getById(person, categoryIdentifier.budgetId)
        authorizationService.cancelIfUserIsUnauthorized(person, budget)
        newPattern?.let { categoryIdentifier.pattern = it }
        newCategoryId?.let { categoryIdentifier.categoryId = it }
        return categoryIdentifierDao.update(categoryIdentifier)
    }

    fun delete(person: Person, categoryIdentifier: CategoryIdentifier) {
        val budget = budgetService.getById(person, categoryIdentifier.budgetId)
        authorizationService.cancelIfUserIsUnauthorized(person, budget)
        categoryIdentifierDao.delete(categoryIdentifier)
    }

    fun getById(person: Person, id: String): CategoryIdentifier {
        val categoryIdentifier = categoryIdentifierDao.getById(id)
        val budget = budgetService.getById(person, categoryIdentifier.budgetId)
        authorizationService.cancelIfUserIsUnauthorized(person, budget)
        return categoryIdentifier
    }

    fun getByBudget(person: Person, budget: Budget): List<CategoryIdentifier> {
        authorizationService.cancelIfUserIsUnauthorized(person, budget)
        return categoryIdentifierDao.getByBudgetId(budget.id)
    }

}
