package open.tresorier.services

import open.tresorier.dao.IOperationDao
import open.tresorier.model.*
import open.tresorier.utils.Time

class OperationService(private val operationDao: IOperationDao, private val authorizationService: AuthorizationService) {

    fun createInitialOperation(person: Person, account: Account, day: Day, amount: Int){
        authorizationService.cancelIfUserIsUnauthorized(person, account)
        val operation = Operation(account.id, day, Category.INCOME_ID, amount, "Montant initial", 1)
        operationDao.insert(operation)
        authorizationService.cancelIfUserIsUnauthorized(person, operation)
    }

    fun create(person: Person, account: Account, day:Day, category: Category?, amount: Int?, memo: String?) : Operation{
        authorizationService.cancelIfUserIsUnauthorized(person, account)
        val order = Time.now()
        val operation = Operation(account.id, day, category?.id, amount ?: 0, memo, order)
        return operationDao.insert(operation)
    }

    fun getById(person: Person, id: String): Operation {
        val operation = operationDao.getById(id)
        authorizationService.cancelIfUserIsUnauthorized(person, operation)
        return operation
    }

    fun update(person: Person, operation: Operation, account: Account, day:Day, category: Category?, amount: Int?, memo: String?) : Operation{
        authorizationService.cancelIfUserIsUnauthorized(person, operation)
        if (day != operation.day) {
            val newOrder = Time.now()
            day.let { operation.day = it }
            newOrder.let { operation.orderInDay = it }
        }
        account?.let { operation.accountId = it.id }
        category?.let { operation.categoryId = it.id }
        amount?.let { operation.amount = it }
        memo?.let { operation.memo = it }
        return operationDao.update(operation)
    }

    fun delete(person: Person, operation: Operation) {
        authorizationService.cancelIfUserIsUnauthorized(person, operation)
        operationDao.delete(operation)
    }

    fun findByAccount(person: Person, account: Account) : List<Operation> {
        authorizationService.cancelIfUserIsUnauthorized(person, account)
        return operationDao.findByAccount(account)
    }

    fun findByBudget(person: Person, budget: Budget) : List<Operation> {
        authorizationService.cancelIfUserIsUnauthorized(person, budget)
        return operationDao.findByBudget(budget)
    }
}
