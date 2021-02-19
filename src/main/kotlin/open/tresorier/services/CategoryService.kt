package open.tresorier.services

import open.tresorier.dao.ICategoryDao
import open.tresorier.exception.TresorierIllegalException
import open.tresorier.model.Category
import open.tresorier.model.Person

class CategoryService(private val categoryDao: ICategoryDao) {

    fun create(person: Person, name: String): Category {
        val category = Category(name, person.id)
        return categoryDao.insert(category)
    }

    fun update(person: Person, category: Category, newName: String): Category {
        cancelIfUserIsUnauthorized(person, category)
        category.name = newName
        return categoryDao.update(category)
    }

    fun getById(person: Person, id: String): Category {
        val category = categoryDao.getById(id)
        cancelIfUserIsUnauthorized(person, category)
        return category
    }

    fun delete(person: Person, category: Category) : Category {
        cancelIfUserIsUnauthorized(person, category)
        category.deleted = true
        return categoryDao.update(category)
    }

    private fun cancelIfUserIsUnauthorized(person: Person, category: Category) {
        val owner = categoryDao.getOwner(category)
        if (owner.id != person.id) {
            throw TresorierIllegalException("user " + person.id + "isn't allowed to interact with category " + category.id)
        }
    }
}
