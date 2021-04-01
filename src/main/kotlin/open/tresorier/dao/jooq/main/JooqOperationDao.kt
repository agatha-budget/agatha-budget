package open.tresorier.dao.jooq.main

import open.tresorier.dao.IOperationDao
import open.tresorier.exception.TresorierException
import open.tresorier.generated.jooq.main.Tables.*
import open.tresorier.generated.jooq.main.tables.daos.OperationDao
import open.tresorier.generated.jooq.main.tables.records.OperationRecord
import open.tresorier.generated.jooq.main.tables.records.PersonRecord
import open.tresorier.model.*
import open.tresorier.utils.Time
import org.jooq.Configuration
import org.jooq.*
import org.jooq.impl.*
import org.jooq.impl.DSL
import org.jooq.impl.DSL.*
import java.math.BigDecimal
import open.tresorier.generated.jooq.main.tables.pojos.Operation as JooqOperation


class JooqOperationDao(val configuration: Configuration) : IOperationDao {

    private val generatedDao: OperationDao = OperationDao(configuration)
    private val query = DSL.using(configuration)

    // ready to use computed Field
    private val month: Field<Int> = extractDatePartFromEpoch(OPERATION.OPERATION_DATE, DatePart.MONTH).`as`("month")
    private val year: Field<Int> = extractDatePartFromEpoch(OPERATION.OPERATION_DATE, DatePart.YEAR).`as`("year")
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
                .select(OPERATION.CATEGORY_ID, month , year, spendingSum )
                .from(MASTER_CATEGORY)
                .join(CATEGORY).on(CATEGORY.MASTER_CATEGORY_ID.eq(MASTER_CATEGORY.ID))
                .join(OPERATION).on(OPERATION.CATEGORY_ID.eq(CATEGORY.ID))
                .where(MASTER_CATEGORY.BUDGET_ID.eq(budget.id))
        maxMonth?.let{ query.and(OPERATION.OPERATION_DATE.lessOrEqual(Time.getMaxTimestamp(maxMonth)))}
        query.groupBy(OPERATION.CATEGORY_ID, month, year)
                .orderBy(year.asc(), month.asc())
        val jooqSpendingList = query.fetch()
        val spendingList: MutableList<Spending> = mutableListOf()
        for (spendingRecord in jooqSpendingList) {
            val allocation = this.toSpending(spendingRecord)
            spendingList.add(allocation)
        }
        return spendingList
    }

    private fun extractDatePartFromEpoch(operationEpoch: TableField<OperationRecord, Long>, datePart: DatePart) : Field<Int> {
        val epochStart = date("1970-01-01")
        val operationDate = dateAdd(epochStart, operationEpoch, DatePart.SECOND)
        return extract(operationDate, datePart)
    }

    override fun getOwner(operation: Operation): Person {
        try {
            val owner: PersonRecord = this.query.select().from(PERSON)
                .join(ACCOUNT).on(ACCOUNT.ID.eq(operation.accountId))
                .join(BUDGET).on(BUDGET.ID.eq(ACCOUNT.BUDGET_ID))
                .where(PERSON.ID.eq(BUDGET.PERSON_ID))
                .fetchAny().into(PERSON)
            return JooqPersonDao.toPerson(owner)
        } catch (e: Exception) {
            throw TresorierException("the given object appears to have no owner")
        }
    }

    private fun toJooqOperation(operation: Operation): JooqOperation {
        return JooqOperation(
            operation.id,
            operation.date,
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
            jooqOperation.operationDate,
            jooqOperation.accountId,
            jooqOperation.categoryId,
            jooqOperation.amount.toDouble(),
            jooqOperation.memo,
            jooqOperation.id,
        )
    }

    private fun toSpending(jooqSpending: Record4<String, Int, Int, BigDecimal>) : Spending {
        return Spending(
                Month(jooqSpending.get(month), jooqSpending.get(year)),
                jooqSpending.get(OPERATION.CATEGORY_ID),
                jooqSpending.get(spendingSum).toDouble()
        )
    }
}
