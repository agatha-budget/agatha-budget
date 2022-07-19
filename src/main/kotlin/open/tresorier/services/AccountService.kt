package open.tresorier.services

import open.tresorier.dao.IAccountDao
import open.tresorier.dao.IBankAccountDao
import open.tresorier.model.*
import open.tresorier.model.banking.*


class AccountService(private val accountDao: IAccountDao, 
        private val bankAccountDao: IBankAccountDao,
        private val authorizationService: AuthorizationService,
        private val operationService: OperationService) {

    fun create(person: Person, budget: Budget, name: String, day: Day, amount: Int): Account {
        authorizationService.cancelIfUserIsUnauthorized(person, budget)
        val account = Account(name, budget.id)
        accountDao.insert(account)
        operationService.createInitialOperation(person, account, day, amount)
        return account
    }

    fun update(person: Person, account: Account, name: String?, bankAccount: BankAccount?): Account {
        authorizationService.cancelIfUserIsUnauthorized(person, account)
        name?.let {
            account.name = it
        }
        bankAccount?.let { 
            authorizationService.cancelIfUserIsUnauthorized(person, it) 
            account.bankAccountId = it.id
        }
        return accountDao.update(account)
    }

    fun getById(person: Person, id: String): Account {
        val account = accountDao.getById(id)
        authorizationService.cancelIfUserIsUnauthorized(person, account)
        return account
    }

    fun delete(person: Person, account: Account) : Account {
        authorizationService.cancelIfUserIsUnauthorized(person, account)
        account.deleted = true
        return accountDao.update(account)
    }

    fun findByBudget(person: Person, budget: Budget) : List<AccountWithAmount> {
        authorizationService.cancelIfUserIsUnauthorized(person, budget)
        return accountDao.findByBudget(budget)
    }

    fun getBankAccountById(person: Person, id: String) : BankAccount {
        val bankAccount = bankAccountDao.getById(id)
        authorizationService.cancelIfUserIsUnauthorized(person, bankAccount)
        return bankAccount
    }
}
