package open.tresorier.dao.jooq.test

import open.tresorier.dao.ICategoryDao
import open.tresorier.exception.TresorierException
import open.tresorier.generated.jooq.test.public_.Tables.BUDGET
import open.tresorier.generated.jooq.test.public_.Tables.PERSON
import open.tresorier.generated.jooq.test.public_.tables.daos.CategoryDao
import open.tresorier.generated.jooq.test.public_.tables.records.PersonRecord
import open.tresorier.model.Category
import open.tresorier.model.Person
import org.jooq.Configuration
import org.jooq.impl.DSL
import open.tresorier.generated.jooq.test.public_.tables.pojos.Category as JooqCategory

class JooqTestCategoryDao(val configuration: Configuration) : ICategoryDao {

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

    override fun findByBudgetId(budgetId: String): List<Category> {
        val jooqCategoryList = this.generatedDao.fetchByBudgetId(budgetId)
        val categoryList: MutableList<Category> = mutableListOf()
        for (jooqCategory in jooqCategoryList) {
            val category = this.toCategory(jooqCategory)
            category?.let { categoryList.add(category) }
        }
        return categoryList
    }

    override fun getCategoryOwner(category: Category): Person {
        try {
            val owner: PersonRecord = this.query.select().from(PERSON)
                .join(BUDGET).on(BUDGET.ID.eq(category.budgetId))
                .where(PERSON.ID.eq(BUDGET.PERSON_ID))
                .fetchAny().into(PERSON)
            return JooqTestPersonDao.toPerson(owner)
        } catch (e : Exception) {
            throw TresorierException("the given object appears to have no owner")
        }
    }

    private fun toJooqCategory(category: Category): JooqCategory {
        return JooqCategory(
            category.id,
            category.budgetId,
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
            jooqCategory.budgetId,
            jooqCategory.archived,
            jooqCategory.id,
            jooqCategory.deleted
        )
    }
}
