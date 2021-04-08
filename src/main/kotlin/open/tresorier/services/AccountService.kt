package open.tresorier.services

import open.tresorier.dao.IAccountDao
import open.tresorier.model.Account
import open.tresorier.model.Budget
import open.tresorier.model.Day
import open.tresorier.model.Person

class AccountService(private val accountDao: IAccountDao, private val authorizationService: AuthorizationService, private val operationService: OperationService) {

    fun create(person: Person, budget: Budget, name: String, day: Day, amount: Double): Account {
        authorizationService.cancelIfUserIsUnauthorized(person, budget)
        val account = Account(name, budget.id)
        accountDao.insert(account)
        operationService.createInitialOperation(person, account, day, amount)
        return account
    }

    fun update(person: Person, account: Account, newName: String): Account {
        authorizationService.cancelIfUserIsUnauthorized(person, account)
        account.name = newName
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

    fun findByBudget(person: Person, budget: Budget) : List<Account> {
        authorizationService.cancelIfUserIsUnauthorized(person, budget)
        return accountDao.findByBudgetId(budget.id)
    }
}
