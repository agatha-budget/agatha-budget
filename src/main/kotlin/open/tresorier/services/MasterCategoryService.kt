package open.tresorier.services

import open.tresorier.dao.ICategoryDao
import open.tresorier.dao.IMasterCategoryDao
import open.tresorier.model.Budget
import open.tresorier.model.Category
import open.tresorier.model.MasterCategory
import open.tresorier.model.Person

class MasterCategoryService(private val masterCategoryDao: IMasterCategoryDao, private val categoryDao: ICategoryDao, private val authorizationService: AuthorizationService) {

    fun create(person: Person, budget: Budget, name: String): MasterCategory {
        authorizationService.cancelIfUserIsUnauthorized(person, budget)
        val masterCategory = MasterCategory(name, budget.id, null)
        val inserted = masterCategoryDao.insert(masterCategory)
        val category = Category(Category.NEW_CATEGORY_NAME, inserted.id)
        categoryDao.insert(category)
        return inserted
    }

    fun update(person: Person, masterCategory: MasterCategory, newName: String?, newColor: String?, newArchived: Boolean?, newDeleted: Boolean?): MasterCategory {
        authorizationService.cancelIfUserIsUnauthorized(person, masterCategory)
        newArchived?.let{ this.archiveCategories(masterCategory, it) }
        newName?.let{ masterCategory.name = it }
        newDeleted?.let{ masterCategory.deleted = it }
        newColor?.let{ masterCategory.color = if (it === "null") null else it }
        return masterCategoryDao.update(masterCategory)
    }

    private fun archiveCategories(masterCategory: MasterCategory, newArchivedStatus: Boolean){
        categoryDao.updateArchivedStatusByMasterCategory(masterCategory, newArchivedStatus)
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

    fun findByBudget(person: Person, budget: Budget): List<MasterCategory> {
        authorizationService.cancelIfUserIsUnauthorized(person, budget)
        return masterCategoryDao.findByBudget(budget)
    }
}
