package open.tresorier.dao.jooq.main

import open.tresorier.dao.IOperationDao
import open.tresorier.exception.TresorierException
import open.tresorier.generated.jooq.main.Tables.*
import open.tresorier.generated.jooq.main.tables.daos.OperationDao
import open.tresorier.generated.jooq.main.tables.records.PersonRecord
import open.tresorier.model.Operation
import open.tresorier.model.Person
import org.jooq.Configuration
import org.jooq.impl.DSL
import java.math.BigDecimal
import open.tresorier.generated.jooq.main.tables.pojos.Operation as JooqOperation


class JooqOperationDao(val configuration: Configuration) : IOperationDao {

    private val generatedDao: OperationDao = OperationDao(configuration)
    private val query = DSL.using(configuration)

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
            jooqOperation.memo,
            jooqOperation.amount.toDouble(),
            jooqOperation.id,
        )
    }
}
