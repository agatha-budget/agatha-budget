package open.tresorier.dao.jooq.h2

import open.tresorier.dao.ICategoryIdentifierDao
import open.tresorier.exception.TresorierException
import open.tresorier.generated.jooq.test.public_.Tables.*
import open.tresorier.generated.jooq.test.public_.tables.daos.CategoryIdentifierDao
import open.tresorier.generated.jooq.test.public_.tables.records.CategoryIdentifierRecord
import open.tresorier.model.CategoryIdentifier
import org.jooq.Configuration
import org.jooq.impl.DSL
import open.tresorier.generated.jooq.test.public_.tables.pojos.CategoryIdentifier as JooqCategoryIdentifier

class H2CategoryIdentifierDao(val configuration: Configuration): ICategoryIdentifierDao {

    private val generatedDao: CategoryIdentifierDao = CategoryIdentifierDao(configuration)
    private val query = DSL.using(configuration)

    override fun insert(categoryIdentifier: CategoryIdentifier): CategoryIdentifier {
        val jooqCategoryIdentifier = this.toJooqCategoryIdentifier(categoryIdentifier)

        try {
            this.generatedDao.insert(jooqCategoryIdentifier)
            return categoryIdentifier

        } catch (e: Exception) {
            throw TresorierException("could not create new categoryIdentifier", e)
        }
    }

    override fun update(categoryIdentifier: CategoryIdentifier): CategoryIdentifier {
        val jooqCategoryIdentifier = this.toJooqCategoryIdentifier(categoryIdentifier)
        this.generatedDao.update(jooqCategoryIdentifier)
        return categoryIdentifier
    }

    override fun delete(categoryIdentifier: CategoryIdentifier) {
        val jooqCategoryIdentifier = this.toJooqCategoryIdentifier(categoryIdentifier)
        this.generatedDao.delete(jooqCategoryIdentifier)
    }

    override fun getByBudgetId(budgetId: String): List<CategoryIdentifier> {
        val jooqCategoryIdentifierList = this.query
            .select()
            .from(CATEGORY_IDENTIFIER)
            .where(CATEGORY_IDENTIFIER.BUDGET_ID.eq(budgetId))
            .fetch().into(CATEGORY_IDENTIFIER)

        val categoryIdentifierList: MutableList<CategoryIdentifier> = mutableListOf()
        for (categoryIdentifierRecord : CategoryIdentifierRecord in jooqCategoryIdentifierList) {
            val categoryIdentifier = this.toCategoryIdentifier(categoryIdentifierRecord)
            categoryIdentifierList.add(categoryIdentifier)
        }

        return categoryIdentifierList
    }

    override fun getById(id: String): CategoryIdentifier {
        val jooqCategoryIdentifier = this.generatedDao.fetchOneById(id)
        return this.toCategoryIdentifier(jooqCategoryIdentifier) ?: throw TresorierException("no categoryIdentifier found for the following id : $id")
    }

    private fun toJooqCategoryIdentifier(categoryIdentifier: CategoryIdentifier): JooqCategoryIdentifier {
        return JooqCategoryIdentifier(
            categoryIdentifier.id,
            categoryIdentifier.budgetId,
            categoryIdentifier.pattern,
            categoryIdentifier.categoryId
        )
    }

    fun toCategoryIdentifier(jooqCategoryIdentifier: JooqCategoryIdentifier?): CategoryIdentifier? {
        return if (jooqCategoryIdentifier == null)
            null
        else CategoryIdentifier(
            jooqCategoryIdentifier.budgetId,
            jooqCategoryIdentifier.pattern,
            jooqCategoryIdentifier.categoryId,
            jooqCategoryIdentifier.id
        )
    }
    fun toCategoryIdentifier(recordCategoryIdentifier: CategoryIdentifierRecord): CategoryIdentifier {
        return CategoryIdentifier(
            recordCategoryIdentifier.budgetId,
            recordCategoryIdentifier.pattern,
            recordCategoryIdentifier.categoryId,
            recordCategoryIdentifier.id
        )
    }
}