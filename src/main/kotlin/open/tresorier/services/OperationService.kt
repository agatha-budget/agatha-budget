package open.tresorier.services

import open.tresorier.dao.IOperationDao
import open.tresorier.exception.TresorierIllegalException
import open.tresorier.model.Account
import open.tresorier.model.Category
import open.tresorier.model.Operation
import open.tresorier.model.Person
import open.tresorier.utils.Time

class OperationService(private val operationDao: IOperationDao, private  val accountService: AccountService) {

    fun createInitialOperation(person: Person, account: Account, amount: Double){
        this.accountService.cancelIfUserIsUnauthorized(person, account)
        val operation = Operation(Time.now(), account.id, Category.INCOME_ID, memo, amount)
        operationDao.insert(operation)
        cancelIfUserIsUnauthorized(person, operation)
    }

    fun create(person: Person, account: Account, date: Long, category: Category, memo: String, amount: Double){
        this.accountService.cancelIfUserIsUnauthorized(person, account)
        val operation = Operation(date, account.id, category.id, memo, amount)
        operationDao.insert(operation)
        cancelIfUserIsUnauthorized(person, operation)
    }

    fun getById(person: Person, id: String): Operation {
        val operation = operationDao.getById(id)
        cancelIfUserIsUnauthorized(person, operation)
        return operation
    }

    fun delete(person: Person, operation: Operation) {
        cancelIfUserIsUnauthorized(person, operation)
        operationDao.delete(operation)
    }

    private fun cancelIfUserIsUnauthorized(person: Person, operation: Operation) {
        val owner = operationDao.getOwner(operation)
        if (owner.id != person.id) {
            throw TresorierIllegalException("user " + person.id + "isn't allowed to interact with operation " + operation.id)
        }
    }
}
