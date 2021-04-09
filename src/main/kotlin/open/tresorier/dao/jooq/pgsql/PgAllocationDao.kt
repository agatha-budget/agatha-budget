package open.tresorier.dao.jooq.pgsql

import open.tresorier.dao.IAllocationDao
import open.tresorier.exception.TresorierException
import open.tresorier.generated.jooq.main.Tables.*
import open.tresorier.generated.jooq.main.tables.daos.AllocationDao
import open.tresorier.generated.jooq.main.tables.records.AllocationRecord
import open.tresorier.generated.jooq.main.tables.records.PersonRecord
import open.tresorier.model.*
import org.jooq.Configuration
import org.jooq.impl.DSL
import java.math.BigDecimal
import open.tresorier.generated.jooq.main.tables.pojos.Allocation as JooqAllocation


class PgAllocationDao(val configuration: Configuration) : IAllocationDao {

    private val generatedDao: AllocationDao = AllocationDao(configuration)
    private val query = DSL.using(configuration)

    override fun insertOrUpdate(allocation: Allocation): Allocation {
        val jooqAllocation = this.toJooqAllocation(allocation)
        try {
            this.generatedDao.update(jooqAllocation)
        } catch (e: Exception) {
            throw TresorierException("could not insert allocation : $allocation", e)
        }
        return allocation
    }

    override fun getByIdentifiers(category: Category, month: Month): Allocation {
        val allocationRecord: AllocationRecord? = this.query
            .select()
            .from(ALLOCATION)
            .where(ALLOCATION.CATEGORY_ID.eq(category.id))
            .and(ALLOCATION.MONTH.eq(month.comparable))
            .fetchAny().into(ALLOCATION)

        return allocationRecord?.let {this.toAllocation(it)} ?:
        throw TresorierException("no allocation found for the following identifiers : categoryId = ${category.id}, month = $month")
    }


    override fun getOwner(allocation: Allocation): Person {
        try {
            val owner: PersonRecord = this.query.select().from(PERSON)
                .join(CATEGORY).on(CATEGORY.ID.eq(allocation.categoryId))
                .join(MASTER_CATEGORY).on(MASTER_CATEGORY.ID.eq(CATEGORY.MASTER_CATEGORY_ID))
                .join(BUDGET).on(BUDGET.ID.eq(MASTER_CATEGORY.BUDGET_ID))
                .where(PERSON.ID.eq(BUDGET.PERSON_ID))
                .fetchAny().into(PERSON)
            return PgPersonDao.toPerson(owner)
        } catch (e: Exception) {
            throw TresorierException("the given object appears to have no owner")
        }
    }

    override fun findByBudget(budget: Budget, maxMonth: Month?): List<Allocation> {
        val query = this.query
            .select()
            .from(ALLOCATION)
            .join(CATEGORY).on(ALLOCATION.CATEGORY_ID.eq(CATEGORY.ID))
            .join(MASTER_CATEGORY).on(CATEGORY.MASTER_CATEGORY_ID.eq(MASTER_CATEGORY.ID))
            .where(MASTER_CATEGORY.BUDGET_ID.eq(budget.id))
        maxMonth?.let {
            query.and(ALLOCATION.MONTH.lessOrEqual(it.comparable))
        }
        query.orderBy(ALLOCATION.MONTH.asc())

        val jooqAllocationList = query.fetch().into(ALLOCATION)

        val allocationList: MutableList<Allocation> = mutableListOf()
        for (allocationRecord : AllocationRecord in jooqAllocationList) {
            val allocation = this.toAllocation(allocationRecord)
            allocationList.add(allocation)
        }

        return allocationList
    }


    private fun toJooqAllocation(allocation: Allocation): JooqAllocation {
        return JooqAllocation(
            allocation.categoryId,
            allocation.month.comparable,
            BigDecimal(allocation.amount)
        )
    }

    private fun toAllocation(allocationRecord: AllocationRecord): Allocation {
        return Allocation(
            Month.createFromComparable(allocationRecord.month),
            allocationRecord.categoryId,
            allocationRecord.amount.toDouble(),
        )
    }
}