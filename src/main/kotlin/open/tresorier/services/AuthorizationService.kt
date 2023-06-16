package open.tresorier.services

import open.tresorier.dao.*
import open.tresorier.exception.TresorierIllegalException
import open.tresorier.model.*
import open.tresorier.model.banking.*

class AuthorizationService(
    private val accountDao: IAccountDao,
    private val budgetDao: IBudgetDao,
    private val allocationDao: IAllocationDao,
    private val categoryDao: ICategoryDao,
    private val masterCategoryDao: IMasterCategoryDao,
    private val operationDao: IOperationDao,
    private val bankAgreementDao: IBankAgreementDao,
    private val bankAccountDao: IBankAccountDao

) {

    fun cancelIfUserIsUnauthorized(person: Person) {
        BillingService.checkIfUserSubscriptionIsActive(person)
    }

    fun cancelIfUserIsUnauthorized(person: Person, account: Account) {
        val owner = accountDao.getOwner(account)
        if (owner.id != person.id) {
            throw TresorierIllegalException("user " + person.id + " isn't allowed to interact with account " + account.id)
        }
        BillingService.checkIfUserSubscriptionIsActive(person)
    }

    fun cancelIfUserIsUnauthorized(person: Person, allocation: Allocation) {
        val owner = allocationDao.getOwner(allocation)
        if (owner.id != person.id) {
            throw TresorierIllegalException("user " + person.id + " isn't allowed to interact with allocation " + allocation.toString())
        }
        BillingService.checkIfUserSubscriptionIsActive(person)
    }

    fun cancelIfUserIsUnauthorized(person: Person, budget: Budget) {
        val owner = budgetDao.getOwner(budget)
        if (owner.id != person.id) {
            throw TresorierIllegalException("user " + person.id + " isn't allowed to interact with budget " + budget.id)
        }
        BillingService.checkIfUserSubscriptionIsActive(person)
    }

    fun cancelIfUserIsUnauthorized(person: Person, category: Category) {
        if (category.masterCategoryId != null){ // if category is universal don't check 
            val owner = categoryDao.getOwner(category)
            if (owner != null && owner.id != person.id) {
                throw TresorierIllegalException("user " + person.id + " isn't allowed to interact with category " + category.id)
            }
        }
        BillingService.checkIfUserSubscriptionIsActive(person)
    }

    fun cancelIfUserIsUnauthorized(person: Person, masterCategory: MasterCategory) {
        val owner = masterCategoryDao.getOwner(masterCategory)
        if (owner.id != person.id) {
            throw TresorierIllegalException("user " + person.id + " isn't allowed to interact with category " + masterCategory.id)
        }
        BillingService.checkIfUserSubscriptionIsActive(person)
    }

    fun cancelIfUserIsUnauthorized(person: Person, operation: Operation) {
        val owner = operationDao.getOwner(operation)
        if (owner.id != person.id) {
            throw TresorierIllegalException("user " + person.id + " isn't allowed to interact with operation " + operation.id)
        }
        BillingService.checkIfUserSubscriptionIsActive(person)
    }

    fun cancelIfUserIsUnauthorized(person: Person, bankAgreement: BankAgreement) {
        val owner = bankAgreementDao.getOwner(bankAgreement)
        if (owner.id != person.id) {
            throw TresorierIllegalException("user " + person.id + " isn't allowed to interact with bankAgreement " + bankAgreement.id)
        }
    }

    fun cancelIfUserIsUnauthorized(person: Person, bankAccount: BankAccount) {
        val owner = bankAccountDao.getOwner(bankAccount)
        if (owner.id != person.id) {
            throw TresorierIllegalException("user " + person.id + " isn't allowed to interact with bankAccount " + bankAccount.id)
        }
    }
}
