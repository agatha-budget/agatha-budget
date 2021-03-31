package open.tresorier.dao

import open.tresorier.model.MasterCategory
import open.tresorier.model.Person

interface IMasterCategoryDao {
    fun insert(masterCategory: MasterCategory) : MasterCategory
    fun update(masterCategory: MasterCategory) : MasterCategory
    fun getById(id: String): MasterCategory
    fun findByBudgetId(budgetId: String) : List<MasterCategory>
    fun getOwner(masterCategory: MasterCategory) : Person
}