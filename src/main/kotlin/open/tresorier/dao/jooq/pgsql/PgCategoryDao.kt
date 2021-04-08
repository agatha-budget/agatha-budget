package open.tresorier.dao.jooq.pgsql

import open.tresorier.dao.ICategoryDao
import open.tresorier.exception.TresorierException
import open.tresorier.generated.jooq.main.Tables.*
import open.tresorier.generated.jooq.main.tables.daos.CategoryDao
import open.tresorier.generated.jooq.main.tables.records.PersonRecord
import open.tresorier.model.*
import open.tresorier.model.Person
import org.jooq.Configuration
import org.jooq.impl.DSL
import open.tresorier.generated.jooq.main.tables.pojos.Category as JooqCategory


class PgCategoryDao(val configuration: Configuration) : ICategoryDao {

    private val generatedDao: CategoryDao = CategoryDao(configuration)
    private val query = DSL.using(configuration)

    override fun insert(category: Category): Category {
        val jooqCategory = this.toJooqCategory(category)
        try {
            this.generatedDao.insert(jooqCategory)
        } catch (e: Exception) {
            throw TresorierException("could not insert category : $category", e)
        }
        return category
    }

    override fun update(category: Category): Category {
        val jooqCategory = this.toJooqCategory(category)
        try {
            this.generatedDao.update(jooqCategory)
        } catch (e: Exception) {
            throw TresorierException("could not update category : $category", e)
        }
        return category
    }

    override fun getById(id: String): Category {
        val jooqCategory = this.generatedDao.fetchOneById(id)
        return this.toCategory(jooqCategory) ?: throw TresorierException("no category found for the following id : $id")
    }

    override fun findByMasterCategory(masterCategory: MasterCategory): List<Category> {
        val jooqCategoryList = this.generatedDao.fetchByMasterCategoryId(masterCategory.id)
        val categoryList: MutableList<Category> = mutableListOf()
        for (jooqCategory in jooqCategoryList) {
            val category = this.toCategory(jooqCategory)
            category?.let { categoryList.add(category) }
        }
        return categoryList
    }

    override fun getOwner(category: Category): Person {
        try {
            val owner: PersonRecord = this.query.select().from(PERSON)
                .join(MASTER_CATEGORY).on(MASTER_CATEGORY.ID.eq(category.masterCategoryId))
                .join(BUDGET).on(BUDGET.ID.eq(MASTER_CATEGORY.BUDGET_ID))
                .where(PERSON.ID.eq(BUDGET.PERSON_ID))
                .fetchAny().into(PERSON)
            return PgPersonDao.toPerson(owner)
        } catch (e : Exception) {
            throw TresorierException("the given object appears to have no owner")
        }
    }

    private fun toJooqCategory(category: Category): JooqCategory {
        return JooqCategory(
            category.id,
            category.masterCategoryId,
            category.name,
            category.archived,
            category.deleted
        )
    }

    private fun toCategory(jooqCategory: JooqCategory?): Category? {
        return if (jooqCategory == null)
            null
        else Category(
            jooqCategory.name,
            jooqCategory.masterCategoryId,
            jooqCategory.archived,
            jooqCategory.id,
            jooqCategory.deleted
        )
    }
}
