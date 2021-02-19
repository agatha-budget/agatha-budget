package open.tresorier.services

import open.tresorier.dao.IOperationDao
import open.tresorier.exception.TresorierIllegalException
import open.tresorier.model.Operation
import open.tresorier.model.Person

class OperationService(private val operationDao: IOperationDao) {

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
