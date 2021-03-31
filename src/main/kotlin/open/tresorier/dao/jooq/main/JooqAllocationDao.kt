package open.tresorier.dao.jooq.main

import open.tresorier.dao.IAllocationDao
import open.tresorier.exception.TresorierException
import open.tresorier.generated.jooq.main.Tables.*
import open.tresorier.generated.jooq.main.tables.daos.AllocationDao
import open.tresorier.generated.jooq.main.tables.records.PersonRecord
import open.tresorier.model.Allocation
import open.tresorier.model.Person
import org.jooq.Configuration
import org.jooq.impl.DSL
import java.math.BigDecimal
import open.tresorier.generated.jooq.main.tables.pojos.Allocation as JooqAllocation


class JooqAllocationDao(val configuration: Configuration) : IAllocationDao {

    private val generatedDao: AllocationDao = AllocationDao(configuration)
    private val query = DSL.using(configuration)

    override fun insert(allocation: Allocation): Allocation {
        val jooqAllocation = this.toJooqAllocation(allocation)
        try {
            this.generatedDao.insert(jooqAllocation)
        } catch (e: Exception) {
            throw TresorierException("could not insert allocation : $allocation", e)
        }
        return allocation
    }

    override fun update(allocation: Allocation): Allocation {
        val jooqAllocation = this.toJooqAllocation(allocation)
        try {
            this.generatedDao.update(jooqAllocation)
        } catch (e: Exception) {
            throw TresorierException("could not update allocation : $allocation", e)
        }
        return allocation
    }

    override fun getById(id: String): Allocation {
        val jooqAllocation = this.generatedDao.fetchOneById(id)
        return this.toAllocation(jooqAllocation)
            ?: throw TresorierException("no allocation found for the following id : $id")
    }

    override fun getOwner(allocation: Allocation): Person {
        try {
            val owner: PersonRecord = this.query.select().from(PERSON)
                .join(CATEGORY).on(CATEGORY.ID.eq(allocation.categoryId))
                .join(MASTER_CATEGORY).on(MASTER_CATEGORY.ID.eq(CATEGORY.MASTER_CATEGORY_ID))
                .join(BUDGET).on(BUDGET.ID.eq(MASTER_CATEGORY.BUDGET_ID))
                .where(PERSON.ID.eq(BUDGET.PERSON_ID))
                .fetchAny().into(PERSON)
            return JooqPersonDao.toPerson(owner)
        } catch (e: Exception) {
            throw TresorierException("the given object appears to have no owner")
        }
    }

    private fun toJooqAllocation(allocation: Allocation): JooqAllocation {
        return JooqAllocation(
            allocation.id,
            allocation.categoryId,
            allocation.year,
            allocation.month,
            BigDecimal(allocation.amount)
        )
    }

    private fun toAllocation(jooqAllocation: JooqAllocation?): Allocation? {
        return if (jooqAllocation == null)
            null
        else Allocation(
            jooqAllocation.year,
            jooqAllocation.month,
            jooqAllocation.categoryId,
            jooqAllocation.amount.toDouble(),
            jooqAllocation.id
        )
    }
}