package open.tresorier.services

import open.tresorier.dao.*
import open.tresorier.exception.TresorierIllegalException
import open.tresorier.model.*

class AuthorizationService(
    private val accountDao: IAccountDao,
    private val budgetDao: IBudgetDao,
    private val allocationDao: IAllocationDao,
    private val categoryDao: ICategoryDao,
    private val masterCategoryDao: IMasterCategoryDao,
    private val operationDao: IOperationDao

) {

    fun cancelIfUserIsUnauthorized(person: Person, account: Account) {
        val owner = accountDao.getOwner(account)
        if (owner.id != person.id) {
            throw TresorierIllegalException("user " + person.id + " isn't allowed to interact with account " + account.id)
        }
    }

    fun cancelIfUserIsUnauthorized(person: Person, allocation: Allocation) {
        val owner = allocationDao.getOwner(allocation)
        if (owner.id != person.id) {
            throw TresorierIllegalException("user " + person.id + " isn't allowed to interact with allocation " + allocation.id)
        }
    }

    fun cancelIfUserIsUnauthorized(person: Person, budget: Budget) {
        val owner = budgetDao.getOwner(budget)
        if (owner.id != person.id) {
            throw TresorierIllegalException("user " + person.id + " isn't allowed to interact with budget " + budget.id)
        }
    }

    fun cancelIfUserIsUnauthorized(person: Person, category: Category) {
        val owner = categoryDao.getOwner(category)
        if (owner.id != person.id) {
            throw TresorierIllegalException("user " + person.id + " isn't allowed to interact with category " + category.id)
        }
    }

    fun cancelIfUserIsUnauthorized(person: Person, masterCategory: MasterCategory) {
        val owner = masterCategoryDao.getOwner(masterCategory)
        if (owner.id != person.id) {
            throw TresorierIllegalException("user " + person.id + " isn't allowed to interact with category " + masterCategory.id)
        }
    }

    fun cancelIfUserIsUnauthorized(person: Person, operation: Operation) {
        val owner = operationDao.getOwner(operation)
        if (owner.id != person.id) {
            throw TresorierIllegalException("user " + person.id + " isn't allowed to interact with operation " + operation.id)
        }
    }
}
