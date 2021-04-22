package open.tresorier.services

import open.tresorier.dao.ICategoryDao
import open.tresorier.model.Budget
import open.tresorier.model.Category
import open.tresorier.model.MasterCategory
import open.tresorier.model.Person

class CategoryService(private val categoryDao: ICategoryDao, private val authorizationService: AuthorizationService) {

    fun create(person: Person, masterCategory: MasterCategory, name: String): Category {
        authorizationService.cancelIfUserIsUnauthorized(person, masterCategory)
        val category = Category(name, masterCategory.id)
        return categoryDao.insert(category)
    }

    fun update(person: Person, category: Category, newName: String?, newMasterCategory: MasterCategory?): Category {
        authorizationService.cancelIfUserIsUnauthorized(person, category)
        newName?.let {category.name = it}
        newMasterCategory?.let {category.masterCategoryId = it.id}
        return categoryDao.update(category)
    }

    fun getById(person: Person, id: String): Category {
        val category = categoryDao.getById(id)
        authorizationService.cancelIfUserIsUnauthorized(person, category)
        return category
    }

    fun delete(person: Person, category: Category) : Category {
        authorizationService.cancelIfUserIsUnauthorized(person, category)
        category.deleted = true
        return categoryDao.update(category)
    }

    fun findByBudget(person: Person, budget: Budget): List<Category> {
        authorizationService.cancelIfUserIsUnauthorized(person, budget)
        return categoryDao.findByBudget(budget)
    }
}
