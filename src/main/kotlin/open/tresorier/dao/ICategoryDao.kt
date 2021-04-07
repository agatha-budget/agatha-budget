package open.tresorier.dao

import open.tresorier.model.Category
import open.tresorier.model.MasterCategory
import open.tresorier.model.Person

interface ICategoryDao {
    fun insert(category: Category) : Category
    fun update(category: Category) : Category
    fun getById(id: String): Category
    fun findByMasterCategory(masterCategory: MasterCategory): List<Category>
    fun getOwner(category: Category) : Person
}