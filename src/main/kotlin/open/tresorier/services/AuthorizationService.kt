package open.tresorier.services

import open.tresorier.dao.IAccountDao
import open.tresorier.dao.IAllocationDao
import open.tresorier.dao.ICategoryDao
import open.tresorier.dao.IOperationDao
import open.tresorier.exception.TresorierIllegalException
import open.tresorier.model.*

class AuthorizationService(
    private val accountDao: IAccountDao,
    private val allocationDao: IAllocationDao,
    private val categoryDao: ICategoryDao,
    private val operationDao: IOperationDao

) {

    fun cancelIfUserIsUnauthorized(person: Person, account: Account) {
        val owner = accountDao.getOwner(account)
        if (owner.id != person.id) {
            throw TresorierIllegalException("user " + person.id + "isn't allowed to interact with account " + account.id)
        }
    }

    fun cancelIfUserIsUnauthorized(person: Person, allocation: Allocation) {
        val owner = allocationDao.getOwner(allocation)
        if (owner.id != person.id) {
            throw TresorierIllegalException("user " + person.id + "isn't allowed to interact with allocation " + allocation.id)
        }
    }

    fun cancelIfUserIsUnauthorized(person: Person, budget: Budget) {
        if (budget.personId != person.id) {
            throw TresorierIllegalException("user " + person.id + "isn't allowed to interact with budget " + budget.id)
        }
    }

    fun cancelIfUserIsUnauthorized(person: Person, category: Category) {
        val owner = categoryDao.getOwner(category)
        if (owner.id != person.id) {
            throw TresorierIllegalException("user " + person.id + "isn't allowed to interact with category " + category.id)
        }
    }

    fun cancelIfUserIsUnauthorized(person: Person, operation: Operation) {
        val owner = operationDao.getOwner(operation)
        if (owner.id != person.id) {
            throw TresorierIllegalException("user " + person.id + "isn't allowed to interact with operation " + operation.id)
        }
    }
}
