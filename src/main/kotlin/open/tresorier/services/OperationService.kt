package open.tresorier.services

import open.tresorier.dao.IOperationDao
import open.tresorier.model.Account
import open.tresorier.model.Category
import open.tresorier.model.Operation
import open.tresorier.model.Person
import open.tresorier.utils.Time

class OperationService(private val operationDao: IOperationDao, private val authorizationService: AuthorizationService) {

    fun createInitialOperation(person: Person, account: Account, amount: Double){
        authorizationService.cancelIfUserIsUnauthorized(person, account)
        val operation = Operation(Time.now(), account.id, Category.INCOME_ID, amount, "INITIAL_AMOUNT")
        operationDao.insert(operation)
        authorizationService.cancelIfUserIsUnauthorized(person, operation)
    }

    fun create(person: Person, account: Account, date: Long, category: Category, amount: Double, memo: String){
        authorizationService.cancelIfUserIsUnauthorized(person, account)
        val operation = Operation(date, account.id, category.id, amount, memo)
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
