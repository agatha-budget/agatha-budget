package open.tresorier.dao

import open.tresorier.model.CategoryIdentifier

interface ICategoryIdentifierDao {
    fun insert(categoryIdentifier: CategoryIdentifier): CategoryIdentifier
    fun update(categoryIdentifier: CategoryIdentifier): CategoryIdentifier
    fun delete(categoryIdentifier: CategoryIdentifier)
    fun getByBudgetId(budgetId: String): List<CategoryIdentifier>
    fun getById(id: String): CategoryIdentifier
}