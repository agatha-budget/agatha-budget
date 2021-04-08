package open.tresorier.services

import open.tresorier.dao.IOperationDao
import open.tresorier.model.*

class OperationService(private val operationDao: IOperationDao, private val authorizationService: AuthorizationService) {

    fun createInitialOperation(person: Person, account: Account, day: Day, amount: Double){
        authorizationService.cancelIfUserIsUnauthorized(person, account)
        val operation = Operation(day, account.id, Category.INCOME_ID, amount, "INITIAL_AMOUNT")
        operationDao.insert(operation)
        authorizationService.cancelIfUserIsUnauthorized(person, operation)
    }

    fun create(person: Person, account: Account, day:Day, category: Category, amount: Double, memo: String){
        authorizationService.cancelIfUserIsUnauthorized(person, account)
        val operation = Operation(day, account.id, category.id, amount, memo)
        operationDao.insert(operation)
    }

    fun getById(person: Person, id: String): Operation {
        val operation = operationDao.getById(id)
        authorizationService.cancelIfUserIsUnauthorized(person, operation)
        return operation
    }

    fun delete(person: Person, operation: Operation) {
        authorizationService.cancelIfUserIsUnauthorized(person, operation)
        operationDao.delete(operation)
    }
}
