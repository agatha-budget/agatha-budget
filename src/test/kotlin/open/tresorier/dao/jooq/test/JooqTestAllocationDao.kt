package open.tresorier.dao.jooq.test

import open.tresorier.dao.IAllocationDao
import open.tresorier.exception.TresorierException
import open.tresorier.generated.jooq.test.public_.Tables.*
import open.tresorier.generated.jooq.test.public_.tables.daos.AllocationDao
import open.tresorier.generated.jooq.test.public_.tables.records.AllocationRecord
import open.tresorier.generated.jooq.test.public_.tables.records.PersonRecord
import open.tresorier.model.*
import org.jooq.Configuration
import org.jooq.Field
import org.jooq.impl.DSL
import java.math.BigDecimal
import open.tresorier.generated.jooq.test.public_.tables.pojos.Allocation as JooqAllocation


class JooqTestAllocationDao(val configuration: Configuration) : IAllocationDao {

    private val generatedDao: AllocationDao = AllocationDao(configuration)
    private val query = DSL.using(configuration)

    private val yearMonth: Field<Int> = ALLOCATION.YEAR.times(100).plus(ALLOCATION.MONTH)

    private fun getYearMonth(month: Month?) : Int? {
        month?.let {return month.year*100+month.month}
        return null
    }

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
            return JooqTestPersonDao.toPerson(owner)
        } catch (e: Exception) {
            throw TresorierException("the given object appears to have no owner")
        }
    }

    override fun findByBudget(budget: Budget, maxMonth: Month?): List<Allocation> {
        val maxMonthDate = getYearMonth(maxMonth)
        val query = this.query
                .select()
                .from(ALLOCATION)
                .join(CATEGORY).on(ALLOCATION.CATEGORY_ID.eq(CATEGORY.ID))
                .join(MASTER_CATEGORY).on(CATEGORY.MASTER_CATEGORY_ID.eq(MASTER_CATEGORY.ID))
                .where(MASTER_CATEGORY.BUDGET_ID.eq(budget.id))
        maxMonthDate?.let {
            query.and(yearMonth.lessOrEqual(maxMonthDate))
        }
        query.orderBy(yearMonth.asc())

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
                allocation.id,
                allocation.categoryId,
                allocation.month.year,
                allocation.month.month,
                BigDecimal(allocation.amount)
        )
    }

    private fun toAllocation(jooqAllocation: JooqAllocation?): Allocation? {
        return if (jooqAllocation == null)
            null
        else Allocation(
                Month(jooqAllocation.month,jooqAllocation.year),
                jooqAllocation.categoryId,
                jooqAllocation.amount.toDouble(),
                jooqAllocation.id
        )
    }

    private fun toAllocation(allocationRecord: AllocationRecord): Allocation {
        return Allocation(
                Month(allocationRecord.month,allocationRecord.year),
                allocationRecord.categoryId,
                allocationRecord.amount.toDouble(),
                allocationRecord.id
        )
    }
}