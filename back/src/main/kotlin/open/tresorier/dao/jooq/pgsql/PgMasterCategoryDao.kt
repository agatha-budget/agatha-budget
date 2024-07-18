package open.tresorier.dao.jooq.pgsql

import open.tresorier.dao.IMasterCategoryDao
import open.tresorier.exception.TresorierException
import open.tresorier.generated.jooq.main.Tables.BUDGET
import open.tresorier.generated.jooq.main.Tables.PERSON
import open.tresorier.generated.jooq.main.tables.daos.MasterCategoryDao
import open.tresorier.generated.jooq.main.tables.records.PersonRecord
import open.tresorier.model.Budget
import open.tresorier.model.MasterCategory
import open.tresorier.model.Person
import org.jooq.Configuration
import org.jooq.impl.DSL
import open.tresorier.generated.jooq.main.tables.pojos.MasterCategory as JooqMasterCategory


class PgMasterCategoryDao(val configuration: Configuration) : IMasterCategoryDao {

    private val generatedDao: MasterCategoryDao = MasterCategoryDao(configuration)
    private val query = DSL.using(configuration)

    override fun insert(masterCategory: MasterCategory): MasterCategory {
        val jooqMasterCategory = this.toJooqCategory(masterCategory)
        try {
            this.generatedDao.insert(jooqMasterCategory)
        } catch (e: Exception) {
            throw TresorierException("could not insert category : $masterCategory", e)
        }
        return masterCategory
    }

    override fun update(masterCategory: MasterCategory): MasterCategory {
        val jooqMasterCategory = this.toJooqCategory(masterCategory)
        try {
            this.generatedDao.update(jooqMasterCategory)
        } catch (e: Exception) {
            throw TresorierException("could not update category : $masterCategory", e)
        }
        return masterCategory
    }

    override fun getById(id: String): MasterCategory {
        val jooqMasterCategory = this.generatedDao.fetchOneById(id)
        return this.toCategory(jooqMasterCategory) ?: throw TresorierException("no category found for the following id : $id")
    }

    override fun findByBudget(budget: Budget): List<MasterCategory> {
        val jooqMasterCategoryList = this.generatedDao.fetchByBudgetId(budget.id)
        val categoryList: MutableList<MasterCategory> = mutableListOf()
        for (jooqMasterCategory in jooqMasterCategoryList) {
            val category = this.toCategory(jooqMasterCategory)
            category?.let { categoryList.add(category) }
        }
        return categoryList
    }

    override fun getOwner(masterCategory: MasterCategory): Person {
        val ownerRecord: PersonRecord? = this.query.select().from(PERSON)
            .join(BUDGET).on(BUDGET.ID.eq(masterCategory.budgetId))
            .where(PERSON.ID.eq(BUDGET.PERSON_ID))
            .fetchAny()?.into(PERSON)
        if (ownerRecord == null) {
            throw TresorierException("the given masterCategory (${masterCategory}) appears to have no owner")
        } else {
            return PgPersonDao.toPerson(ownerRecord)
        }
    }

    private fun toJooqCategory(masterCategory: MasterCategory): JooqMasterCategory {
        return JooqMasterCategory(
            masterCategory.id,
            masterCategory.budgetId,
            masterCategory.name,
            masterCategory.deleted,
            masterCategory.color
        )
    }

    private fun toCategory(jooqMasterCategory: JooqMasterCategory?): MasterCategory? {
        return if (jooqMasterCategory == null)
            null
        else MasterCategory(
            jooqMasterCategory.name,
            jooqMasterCategory.budgetId,
            jooqMasterCategory.color,
            jooqMasterCategory.id,
            jooqMasterCategory.deleted
        )
    }
}
