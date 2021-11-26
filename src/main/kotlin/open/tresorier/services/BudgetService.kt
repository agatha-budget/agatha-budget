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
        val masterCategoryGoal = MasterCategory("_Objectifs", budget.id)
        val masterCategoryVIP = MasterCategory("_Priorités au quotidien", budget.id)
        val masterCategoryFixed = MasterCategory("Frais Fixes", budget.id)
        val masterCategoryVariable = MasterCategory("Frais Variables", budget.id)
        val masterCategoryProvision = MasterCategory("Provisions", budget.id)

        val masterCategories = listOf(masterCategoryGoal, masterCategoryVIP, masterCategoryFixed, masterCategoryVariable, masterCategoryProvision)
        for (masterCategory in masterCategories) {
            masterCategoryDao.insert(masterCategory)
        }

        val categories = listOf(
            Category("Filet de sécurité", masterCategoryGoal.id),
            Category("Vacances", masterCategoryGoal.id),
            Category("Sortie entre amis", masterCategoryVIP.id),
            Category("Loyer", masterCategoryFixed.id),
            Category("Internet", masterCategoryFixed.id),
            Category("Forfait téléphone", masterCategoryFixed.id),
            Category("Électricité", masterCategoryFixed.id),
            Category("Dons", masterCategoryFixed.id),
            Category("Assurances", masterCategoryFixed.id),
            Category("Gaz", masterCategoryFixed.id),
            Category("Eau", masterCategoryFixed.id),
            Category("Alimentation/Courses", masterCategoryVariable.id),
            Category("Divers loisirs", masterCategoryVariable.id),
            Category("Tabac", masterCategoryVariable.id),
            Category("Essence", masterCategoryVariable.id),
            Category("Voiture", masterCategoryProvision.id),
            Category("Téléphone", masterCategoryProvision.id),
            Category("Noël", masterCategoryProvision.id)
        )
        for (category in categories) {
            categoryDao.insert(category)
        }
    }
}
