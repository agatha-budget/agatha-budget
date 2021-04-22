package open.tresorier.dao

import open.tresorier.model.Category
import open.tresorier.model.MasterCategory
import open.tresorier.model.Person
import open.tresorier.model.Budget

interface ICategoryDao {
    fun insert(category: Category) : Category
    fun update(category: Category) : Category
    fun getById(id: String): Category
    fun findByMasterCategory(masterCategory: MasterCategory): List<Category>
    fun findByBudget(budget: Budget): List<Category>
    fun getOwner(category: Category) : Person
}