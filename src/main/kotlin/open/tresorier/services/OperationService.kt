package open.tresorier.services

import open.tresorier.dao.IOperationDao
import open.tresorier.model.*

class OperationService(private val operationDao: IOperationDao, private val authorizationService: AuthorizationService) {

    fun createInitialOperation(person: Person, account: Account, day: Day, amount: Double){
        authorizationService.cancelIfUserIsUnauthorized(person, account)
        val operation = Operation(account.id, day, Category.INCOME_ID, amount, "INITIAL_AMOUNT")
        operationDao.insert(operation)
        authorizationService.cancelIfUserIsUnauthorized(person, operation)
    }

    fun create(person: Person, account: Account, day:Day?, category: Category?, amount: Double?, memo: String?) : Operation{
        authorizationService.cancelIfUserIsUnauthorized(person, account)
        val operation = Operation(account.id, day, category?.id, amount ?: 0.0, memo)
        return operationDao.insert(operation)
    }

    fun getById(person: Person, id: String): Operation {
        val operation = operationDao.getById(id)
        authorizationService.cancelIfUserIsUnauthorized(person, operation)
        return operation
    }

    fun update(person: Person, operation: Operation, account: Account?, day:Day?, category: Category?, amount: Double?, memo: String?) : Operation{
        authorizationService.cancelIfUserIsUnauthorized(person, operation)
        account?.let { operation.accountId = it.id }
        day?.let { operation.day = it }
        category?.let { operation.categoryId = it.id }
        amount?.let { operation.amount = it }
        memo?.let { operation.memo = it }
        return operationDao.update(operation)
    }

    fun delete(person: Person, operation: Operation) {
        authorizationService.cancelIfUserIsUnauthorized(person, operation)
        operationDao.delete(operation)
    }
}
