package open.tresorier.services

import open.tresorier.dao.IBudgetDao
import open.tresorier.dao.ICategoryDao
import open.tresorier.dao.IMasterCategoryDao
import open.tresorier.model.Budget
import open.tresorier.model.Category
import open.tresorier.model.MasterCategory
import open.tresorier.model.Person

class BudgetService(private val budgetDao: IBudgetDao, private val masterCategoryDao: IMasterCategoryDao,
                    private val categoryDao: ICategoryDao, private val authorizationService: AuthorizationService) {

    fun create(person: Person, name: String): Budget {
        val budget = Budget(name, person.id)
        budgetDao.insert(budget)
        this.initCategoryForBudget(budget)
        return budget
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

    private fun initCategoryForBudget(budget: Budget){
        val masterCategoryVIP = MasterCategory("I18N_vip", budget.id)
        val masterCategoryFixed = MasterCategory("I18N_fixed_expenses", budget.id)
        val masterCategoryVariable = MasterCategory("I18N_variable_expenses", budget.id)
        val masterCategoryProvision = MasterCategory("I18N_provisions", budget.id)

        val masterCategories = listOf(masterCategoryVIP, masterCategoryFixed, masterCategoryVariable, masterCategoryProvision)
        for (masterCategory in masterCategories) {
            masterCategoryDao.insert(masterCategory)
        }

        val categories = listOf(
            Category("I18N_6_month", masterCategoryVIP.id),
            Category("I18N_rent", masterCategoryFixed.id),
            Category("I18N_internet", masterCategoryFixed.id),
            Category("I18N_electricity", masterCategoryFixed.id),
            Category("I18N_groceries", masterCategoryVariable.id),
            Category("I18N_leisure", masterCategoryVariable.id),
            Category("I18N_computer", masterCategoryProvision.id),
            Category("I18N_cell_phone", masterCategoryProvision.id)
        )
        for (category in categories) {
            categoryDao.insert(category)
        }
    }
}
