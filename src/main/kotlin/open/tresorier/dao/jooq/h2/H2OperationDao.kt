package open.tresorier.dao.jooq.h2

import open.tresorier.dao.IOperationDao
import open.tresorier.exception.TresorierException
import open.tresorier.generated.jooq.test.public_.Tables.*
import open.tresorier.generated.jooq.test.public_.tables.Operation as OperationTable
import open.tresorier.generated.jooq.test.public_.tables.daos.OperationDao
import open.tresorier.generated.jooq.test.public_.tables.records.OperationRecord
import open.tresorier.generated.jooq.test.public_.tables.records.PersonRecord
import open.tresorier.model.*
import org.jooq.Configuration
import org.jooq.Record
import org.jooq.Result
import org.jooq.Field
import org.jooq.Record3
import org.jooq.Record11
import org.jooq.Record13
import org.jooq.impl.DSL
import org.jooq.impl.DSL.*
import java.math.BigDecimal
import open.tresorier.generated.jooq.test.public_.tables.pojos.Operation as JooqOperation

typealias OperationWithDaughtersRecord = Record11<String, String, Int, Int, String, Int, Long, String, Boolean, Boolean, Result<OperationRecord>>

class H2OperationDao(val configuration: Configuration) : IOperationDao {

    private val generatedDao: OperationDao = OperationDao(configuration)
    private val query = DSL.using(configuration)

    // ready to use computed Field
    private val spendingSum: Field<BigDecimal>? = sum(OPERATION.AMOUNT).`as`("sum")

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
            .select(OPERATION.CATEGORY_ID, OPERATION.DATE_MONTH , spendingSum )
            .from(MASTER_CATEGORY)
            .join(CATEGORY).on(CATEGORY.MASTER_CATEGORY_ID.eq(MASTER_CATEGORY.ID))
            .join(OPERATION).on(OPERATION.CATEGORY_ID.eq(CATEGORY.ID))
            .where(MASTER_CATEGORY.BUDGET_ID.eq(budget.id))
        maxMonth?.let{ query.and( OPERATION.DATE_MONTH.lessOrEqual(it.comparable))}
        query.groupBy(OPERATION.CATEGORY_ID, OPERATION.DATE_MONTH)
            .orderBy(OPERATION.DATE_MONTH.asc())
        val jooqSpendingList = query.fetch()
        val spendingList: MutableList<Spending> = mutableListOf()
        for (spendingRecord in jooqSpendingList) {
            val spending = this.toSpending(spendingRecord)
            spendingList.add(spending)
        }
        return spendingList
    }

    override fun findAmountByBudget(budget: Budget, month: Month?): Int {
        val query = this.query
            .select(spendingSum)
            .from(OPERATION)
            .join(ACCOUNT).on(OPERATION.ACCOUNT_ID.eq(ACCOUNT.ID))
            .where(ACCOUNT.BUDGET_ID.eq(budget.id))
        //    .and(OPERATION.MOTHER_OPERATION_ID.isNull)
        month?.let{ query.and(OPERATION.DATE_MONTH.lessOrEqual(it.comparable))}
        val rawResult = query.fetchOne()?.get(spendingSum)
        return rawResult?.toInt() ?: 0
    }

    private val operation: OperationTable = OPERATION.`as`("operation")
    private val daughter: OperationTable = OPERATION.`as`("daughter")
    private val categorizedDaughter: OperationTable = OPERATION.`as`("categorized_daughter")

    // ready to use computed Field
    private val daughters = multiset(
        selectFrom(daughter).
        where(daughter.MOTHER_OPERATION_ID.eq(operation.ID)))
        .`as`("daughters")

    override fun findByAccount(account: Account, category: Category?): List<OperationWithDaughters> {
        val query = this.query
            .selectDistinct(
                operation.ID, operation.ACCOUNT_ID, operation.DATE_MONTH, operation.DATE_DAY,
                operation.CATEGORY_ID, operation.AMOUNT, operation.ORDER_IN_DAY, operation.MEMO,
                operation.PENDING, operation.LOCKED, daughters)
            .from(operation)
            .leftJoin(categorizedDaughter).on(categorizedDaughter.MOTHER_OPERATION_ID.eq(operation.ID))
            .where(operation.ACCOUNT_ID.eq(account.id))
            .and(operation.MOTHER_OPERATION_ID.isNull)
        category?.let{
            query.and(operation.CATEGORY_ID.eq(it.id).or(categorizedDaughter.CATEGORY_ID.eq(it.id)))
        }
        query.orderBy(operation.DATE_MONTH.desc(), operation.DATE_DAY.desc(), operation.ORDER_IN_DAY.desc())
        val jooqOperationList = query.fetch()
        val operationList: MutableList<OperationWithDaughters> = mutableListOf()
        for (operationRecord : OperationWithDaughtersRecord in jooqOperationList) {
            val operation = this.toOperationWithDaughter(operationRecord)
            operationList.add(operation)
        }

        return operationList
    }

    override fun findByBudget(budget: Budget, category: Category?): List<OperationWithDaughters> {
        val query = this.query
            .selectDistinct(
                operation.ID, operation.ACCOUNT_ID, operation.DATE_MONTH, operation.DATE_DAY,
                operation.CATEGORY_ID, operation.AMOUNT, operation.ORDER_IN_DAY, operation.MEMO,
                operation.PENDING, operation.LOCKED, daughters)
            .from(operation)
            .leftJoin(categorizedDaughter).on(categorizedDaughter.MOTHER_OPERATION_ID.eq(operation.ID))
            .join(ACCOUNT).on(operation.ACCOUNT_ID.eq(ACCOUNT.ID))
            .where(ACCOUNT.BUDGET_ID.eq(budget.id))
            .and(operation.MOTHER_OPERATION_ID.isNull)
        category?.let{
            query.and(operation.CATEGORY_ID.eq(it.id).or(categorizedDaughter.CATEGORY_ID.eq(it.id)))
        }
        query.orderBy(operation.DATE_MONTH.desc(), operation.DATE_DAY.desc(), operation.ORDER_IN_DAY.desc())
        val jooqOperationList = query.fetch()
        val operationList: MutableList<OperationWithDaughters> = mutableListOf()
        for (operationRecord : OperationWithDaughtersRecord in jooqOperationList) {
            val operation = this.toOperationWithDaughter(operationRecord)
            operationList.add(operation)
        }

        return operationList
    }

    override fun getOwner(operation: Operation): Person {
        val ownerRecord: PersonRecord? = this.query.select().from(PERSON)
                .join(ACCOUNT).on(ACCOUNT.ID.eq(operation.accountId))
                .join(BUDGET).on(BUDGET.ID.eq(ACCOUNT.BUDGET_ID))
                .where(PERSON.ID.eq(BUDGET.PERSON_ID))
                .fetchAny()?.into(PERSON)
        if (ownerRecord == null) {
            throw TresorierException("the given object appears to have no owner")
        } else {
            return H2PersonDao.toPerson(ownerRecord)
        }
    }

    override fun findDaughterOperations(motherOperation: Operation): List<Operation> {
        val query = this.query
            .selectFrom(OPERATION)
            .where(OPERATION.MOTHER_OPERATION_ID.eq(motherOperation.id))
            .orderBy(OPERATION.DATE_MONTH.desc(), OPERATION.DATE_DAY.desc(), OPERATION.ORDER_IN_DAY.desc())
        val jooqOperationList = query.fetch().into(OPERATION)
        val operationList: MutableList<Operation> = mutableListOf()
        for (operationRecord : OperationRecord in jooqOperationList) {
            val operation = this.toOperation(operationRecord)
            operationList.add(operation)
        }
        return operationList
    }

    private fun toJooqOperation(operation: Operation): JooqOperation {
        return JooqOperation(
            operation.id,
            operation.accountId,
            operation.day.month.comparable,
            operation.day.day,
            operation.categoryId,
            operation.memo,
            operation.amount,
            operation.orderInDay,
            operation.pending,
            operation.locked,
            operation.motherOperationId,
            operation.importIdentifier,
            operation.importTimestamp
            )
    }

    private fun toOperation(jooqOperation: JooqOperation?): Operation? {
        return if (jooqOperation == null)
            null
        else Operation(
            jooqOperation.accountId,
            Day(Month.createFromComparable(jooqOperation.dateMonth), jooqOperation.dateDay),
            jooqOperation.categoryId,
            jooqOperation.amount,
            jooqOperation.orderInDay,
            jooqOperation.memo,
            jooqOperation.pending,
            jooqOperation.locked,
            jooqOperation.motherOperationId,
            jooqOperation.importIdentifier,
            jooqOperation.importTimestamp,
            jooqOperation.id,
        )
    }

    private fun toSpending(jooqSpending: Record3<String, Int, BigDecimal>) : Spending {
        return Spending(
            Month.createFromComparable(jooqSpending.get(OPERATION.DATE_MONTH)),
            jooqSpending.get(OPERATION.CATEGORY_ID),
            jooqSpending.get(spendingSum).toInt()
        )
    }

    private fun toOperation(operationRecord: OperationRecord): Operation {
        return Operation(
            operationRecord.accountId,
            Day(Month.createFromComparable(operationRecord.dateMonth), operationRecord.dateDay),
            operationRecord.categoryId,
            operationRecord.amount,
            operationRecord.orderInDay,
            operationRecord.memo,
            operationRecord.pending,
            operationRecord.locked,
            operationRecord.motherOperationId,
            operationRecord.importIdentifier,
            operationRecord.importTimestamp,
            operationRecord.id,
        )
    }

    private fun toOperationWithDaughter(record: OperationWithDaughtersRecord ): OperationWithDaughters {
        val daughtersList: MutableList<Operation> = mutableListOf()
        for (daughter in record.get(daughters)) {
            val operation = this.toOperation(daughter)
            daughtersList.add(operation)
        }
        return OperationWithDaughters(
            record.get(OPERATION.ACCOUNT_ID),
            Day(Month.createFromComparable(record.get(OPERATION.DATE_MONTH)), record.get(OPERATION.DATE_DAY)),
            record.get(OPERATION.CATEGORY_ID),
            record.get(OPERATION.AMOUNT),
            record.get(OPERATION.ORDER_IN_DAY),
            record.get(OPERATION.MEMO),
            record.get(OPERATION.PENDING),
            record.get(OPERATION.LOCKED),
            daughtersList,
            record.get(OPERATION.ID),
        )
    }
}