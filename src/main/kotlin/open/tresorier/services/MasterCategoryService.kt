package open.tresorier.services

import open.tresorier.dao.IMasterCategoryDao
import open.tresorier.model.Budget
import open.tresorier.model.MasterCategory
import open.tresorier.model.Person

class MasterCategoryService(private val masterCategoryDao: IMasterCategoryDao, private val authorizationService: AuthorizationService) {

    fun create(person: Person, budget: Budget, name: String): MasterCategory {
        authorizationService.cancelIfUserIsUnauthorized(person, budget)
        val category = MasterCategory(name, budget.id)
        return masterCategoryDao.insert(category)
    }

    fun update(person: Person, masterCategory: MasterCategory, newName: String): MasterCategory {
        authorizationService.cancelIfUserIsUnauthorized(person, masterCategory)
        masterCategory.name = newName
        return masterCategoryDao.update(masterCategory)
    }

    fun getById(person: Person, id: String): MasterCategory {
        val masterCategory = masterCategoryDao.getById(id)
        authorizationService.cancelIfUserIsUnauthorized(person, masterCategory)
        return masterCategory
    }

    fun delete(person: Person, masterCategory: MasterCategory) : MasterCategory {
        authorizationService.cancelIfUserIsUnauthorized(person, masterCategory)
        masterCategory.deleted = true
        return masterCategoryDao.update(masterCategory)
    }
}
