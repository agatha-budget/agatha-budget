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
import open.tresorier.generated.jooq.main.tables.pojos.Allocation as JooqAllocation


class PgAllocationDao(val configuration: Configuration) : IAllocationDao {

    private val generatedDao: AllocationDao = AllocationDao(configuration)
    private val query = DSL.using(configuration)

    override fun insertOrUpdate(allocation: Allocation): Allocation {
        val jooqAllocation = this.toJooqAllocation(allocation)
        try {
            val existingAllocation : AllocationRecord? = this.getRecordByIdentifiers(allocation.categoryId, allocation.month)
            existingAllocation?.let { this.generatedDao.update(jooqAllocation) } ?: this.generatedDao.insert(jooqAllocation)
        } catch (e: Exception) {
            throw TresorierException("could not insert allocation : $allocation", e)
        }
        return allocation
    }

    override fun getByIdentifiers(category: Category, month: Month): Allocation {
        val allocationRecord : AllocationRecord ? = getRecordByIdentifiers(category.id, month)
        return allocationRecord?.let {this.toAllocation(it)}
            ?: throw TresorierException("no allocation found for the following identifiers : categoryId = ${category.id}, month = $month")
    }

    private fun getRecordByIdentifiers(categoryId: String, month: Month): AllocationRecord ? {
        return this.query
            .select()
            .from(ALLOCATION)
            .where(ALLOCATION.CATEGORY_ID.eq(categoryId))
            .and(ALLOCATION.MONTH.eq(month.comparable))
            .fetchAny()?.into(ALLOCATION)
    }

    override fun getOwner(allocation: Allocation): Person {
        val ownerRecord: PersonRecord? = this.query.select().from(PERSON)
            .join(CATEGORY).on(CATEGORY.ID.eq(allocation.categoryId))
            .join(MASTER_CATEGORY).on(MASTER_CATEGORY.ID.eq(CATEGORY.MASTER_CATEGORY_ID))
            .join(BUDGET).on(BUDGET.ID.eq(MASTER_CATEGORY.BUDGET_ID))
            .where(PERSON.ID.eq(BUDGET.PERSON_ID))
            .fetchAny()?.into(PERSON)
        if (ownerRecord == null) {
            throw TresorierException("the given allocation (${allocation}) appears to have no owner")
        } else {
            return PgPersonDao.toPerson(ownerRecord)
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
            allocation.amount
        )
    }

    private fun toAllocation(allocationRecord: AllocationRecord): Allocation {
        return Allocation(
            Month.createFromComparable(allocationRecord.month),
            allocationRecord.categoryId,
            allocationRecord.amount,
        )
    }
}