package open.tresorier.dao.jooq.h2

import open.tresorier.dao.IOperationDao
import open.tresorier.exception.TresorierException
import open.tresorier.generated.jooq.test.public_.Tables.*
import open.tresorier.generated.jooq.test.public_.tables.daos.OperationDao
import open.tresorier.generated.jooq.test.public_.tables.records.PersonRecord
import open.tresorier.model.*
import org.jooq.Configuration
import org.jooq.*
import org.jooq.impl.*
import org.jooq.impl.DSL
import org.jooq.impl.DSL.*
import java.math.BigDecimal
import open.tresorier.generated.jooq.test.public_.tables.pojos.Operation as JooqOperation


class H2OperationDao(val configuration: Configuration) : IOperationDao {

    private val generatedDao: OperationDao = OperationDao(configuration)
    private val query = DSL.using(configuration)

    // ready to use computed Field
    private val spendingSum: Field<BigDecimal> = sum(OPERATION.AMOUNT).`as`("sum")

    override fun insert(operation: Operation): Operation {
        val jooqOperation = this.toJooqOperation(operation)
        try {
            this.generatedDao.insert(jooqOperation)
        } catch (e: Exception) {
            throw TresorierException("could not insert operation : $operation", e)
        }
        return operation
    }

    override fun update(operation: Operation): Operation {
        val jooqOperation = this.toJooqOperation(operation)
        try {
            this.generatedDao.update(jooqOperation)
        } catch (e: Exception) {
            throw TresorierException("could not update operation : $operation", e)
        }
        return operation
    }

    override fun delete(operation: Operation) {
        val jooqOperation = this.toJooqOperation(operation)
        try {
            this.generatedDao.delete(jooqOperation)
        } catch (e: Exception) {
            throw TresorierException("could not delete operation : $operation", e)
        }
    }

    override fun getById(id: String): Operation {
        val jooqOperation = this.generatedDao.fetchOneById(id)
        return this.toOperation(jooqOperation)
                ?: throw TresorierException("no operation found for the following id : $id")
    }

    override fun findTotalSpendingByMonth(budget: Budget, maxMonth: Month?) : List<Spending> {
        val query = this.query
                .select(OPERATION.CATEGORY_ID, OPERATION.MONTH , spendingSum )
                .from(MASTER_CATEGORY)
                .join(CATEGORY).on(CATEGORY.MASTER_CATEGORY_ID.eq(MASTER_CATEGORY.ID))
                .join(OPERATION).on(OPERATION.CATEGORY_ID.eq(CATEGORY.ID))
                .where(MASTER_CATEGORY.BUDGET_ID.eq(budget.id))
        maxMonth?.let{ query.and( OPERATION.MONTH.lessOrEqual(it.comparable))}
        query.groupBy(OPERATION.CATEGORY_ID, OPERATION.MONTH)
                .orderBy(OPERATION.MONTH.asc())
        val jooqSpendingList = query.fetch()
        val spendingList: MutableList<Spending> = mutableListOf()
        for (spendingRecord in jooqSpendingList) {
            val allocation = this.toSpending(spendingRecord)
            spendingList.add(allocation)
        }
        return spendingList
    }

    override fun getOwner(operation: Operation): Person {
        try {
            val owner: PersonRecord = this.query.select().from(PERSON)
                    .join(ACCOUNT).on(ACCOUNT.ID.eq(operation.accountId))
                    .join(BUDGET).on(BUDGET.ID.eq(ACCOUNT.BUDGET_ID))
                    .where(PERSON.ID.eq(BUDGET.PERSON_ID))
                    .fetchAny().into(PERSON)
            return H2PersonDao.toPerson(owner)
        } catch (e: Exception) {
            throw TresorierException("the given object appears to have no owner")
        }
    }

    private fun toJooqOperation(operation: Operation): JooqOperation {
        return JooqOperation(
                operation.id,
                operation.day.month.comparable,
                operation.day.day,
                operation.accountId,
                operation.categoryId,
                BigDecimal(operation.amount),
                operation.memo
        )
    }

    private fun toOperation(jooqOperation: JooqOperation?): Operation? {
        return if (jooqOperation == null)
            null
        else Operation(
                Day(Month.createFromComparable(jooqOperation.month), jooqOperation.day),
                jooqOperation.accountId,
                jooqOperation.categoryId,
                jooqOperation.amount.toDouble(),
                jooqOperation.memo,
                jooqOperation.id,
        )
    }

    private fun toSpending(jooqSpending: Record3<String, Int, BigDecimal>) : Spending {
        return Spending(
                Month.createFromComparable(jooqSpending.get(OPERATION.MONTH)),
                jooqSpending.get(OPERATION.CATEGORY_ID),
                jooqSpending.get(spendingSum).toDouble()
        )
    }
}
