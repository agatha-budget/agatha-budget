package open.tresorier.services

import open.tresorier.dao.IBudgetDao
import open.tresorier.dao.ICategoryDao
import open.tresorier.dao.IMasterCategoryDao
import open.tresorier.model.Budget
import open.tresorier.model.Category
import open.tresorier.model.MasterCategory
import open.tresorier.model.Person
import open.tresorier.model.enum.ProfileEnum

class BudgetService(private val budgetDao: IBudgetDao, private val masterCategoryDao: IMasterCategoryDao,
                    private val categoryDao: ICategoryDao, private val authorizationService: AuthorizationService) {

    fun create(person: Person, name: String, profile: ProfileEnum): Budget {
        val budget = Budget(name, person.id, profile)
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
        authorizationService.cancelIfUserIsUnauthorized(person)
        return budgetDao.findByPersonId(person.id)
    }

    private fun initCategoryForBudget(budget: Budget){
        when {
            budget.profile == ProfileEnum.PROFILE_USER -> this.initCategoriesUserProfile(budget)
            budget.profile == ProfileEnum.PROFILE_COMPANY -> this.initCategoriesCompanyProfile(budget)
        }
    }

    private fun initCategoriesUserProfile(budget: Budget) {
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
            Category("Coup d'avance - 1 mois", masterCategoryGoal.id),
            Category("Vacances", masterCategoryGoal.id),
            Category("Sortie entre amis", masterCategoryVIP.id),
            Category("Loyer", masterCategoryFixed.id),
            Category("Internet", masterCategoryFixed.id),
            Category("Forfait téléphone", masterCategoryFixed.id),
            Category("Électricité", masterCategoryFixed.id),
            Category("Agatha-Budget (8€/mois)", masterCategoryFixed.id),
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

    private fun initCategoriesCompanyProfile(budget: Budget) {
        val masterCategoryIncoming = MasterCategory("1 - Entrées", budget.id)
        val masterCategoryGoal = MasterCategory("2 - Projets d'avenir", budget.id)
        val masterCategoryProvision = MasterCategory("3 - Provisions", budget.id)
        val masterCategoryVariable = MasterCategory("4 - Frais variables", budget.id)
        val masterCategoryFixed = MasterCategory("5 - Frais fixes", budget.id)
        val masterCategoryHumanRessources = MasterCategory("6 - Ressources humaines", budget.id)
        val masterCategoryTaxes = MasterCategory("7 - TVA", budget.id)
        
        val masterCategories = listOf(masterCategoryIncoming, masterCategoryGoal, masterCategoryProvision, masterCategoryVariable, masterCategoryFixed, masterCategoryHumanRessources, masterCategoryTaxes)
        for (masterCategory in masterCategories) {
            masterCategoryDao.insert(masterCategory)
        }
        
        val categories = listOf(
            Category("1 - Facturé", masterCategoryIncoming.id),
            Category("2 - Payé", masterCategoryIncoming.id),
            Category("3 - Subventions", masterCategoryIncoming.id),
            Category("4 - Autres", masterCategoryIncoming.id),
            Category("Nouvelle branche d'activité", masterCategoryGoal.id),
            Category("Coup d'avance - 1 mois", masterCategoryProvision.id),
            Category("Fournitures", masterCategoryProvision.id),
            Category("Matériel", masterCategoryProvision.id),
            Category("Site Web", masterCategoryProvision.id),
            Category("Voiture", masterCategoryProvision.id),
            Category("Communication", masterCategoryVariable.id),
            Category("Déplacements", masterCategoryVariable.id),
            Category("Impôts", masterCategoryVariable.id),
            Category("Livraisons", masterCategoryVariable.id),
            Category("Matières premières", masterCategoryVariable.id),
            Category("Abonnements/Cotisations", masterCategoryFixed.id),
            Category("Agatha-Budget (8€/mois)", masterCategoryFixed.id),
            Category("Comptables", masterCategoryFixed.id),
            Category("Divers", masterCategoryFixed.id),
            Category("Eau/Gaz/Électricité", masterCategoryFixed.id),
            Category("Internet/Forfait tel", masterCategoryFixed.id),
            Category("Loyer", masterCategoryFixed.id),
            Category("Dirigeant charges sociales", masterCategoryHumanRessources.id),
            Category("Dirigeant rémunérations", masterCategoryHumanRessources.id),
            Category("Médecine du travail", masterCategoryHumanRessources.id),
            Category("Prime/Intéressement", masterCategoryHumanRessources.id),
            Category("Salariés charges sociales", masterCategoryHumanRessources.id),
            Category("Salariés rémunérations", masterCategoryHumanRessources.id),
            Category("Matelas TVA", masterCategoryTaxes.id),
            Category("TVA due à l'Etat", masterCategoryTaxes.id),
            Category("TVA due par l'Etat", masterCategoryTaxes.id)
        )
        for (category in categories) {
            categoryDao.insert(category)
        }
    }
}
