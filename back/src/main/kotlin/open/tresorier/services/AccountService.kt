package open.tresorier.services

import open.tresorier.dao.IAccountDao
import open.tresorier.dao.IBankAccountDao
import open.tresorier.model.*
import open.tresorier.model.banking.BankAccount
import open.tresorier.utils.Time


class AccountService(private val accountDao: IAccountDao, 
        private val bankAccountDao: IBankAccountDao,
        private val authorizationService: AuthorizationService,
        private val operationService: OperationService,
        private val bankingService: BankingService) {

    fun create(person: Person, budget: Budget, name: String, day: Day, amount: Int): Account {
        authorizationService.cancelIfUserIsUnauthorized(person, budget)
        val account = Account(name, budget.id)
        accountDao.insert(account)
        operationService.createInitialOperation(person, account, day, amount)
        return account
    }

    fun update(person: Person, account: Account, name: String): Account {
        authorizationService.cancelIfUserIsUnauthorized(person, account)
        account.name = name
        return accountDao.update(account)
    }

    fun updateBankAssociation(person: Person, account: Account, bankAccount: BankAccount?, importHistory: Boolean): Account {
        authorizationService.cancelIfUserIsUnauthorized(person, account)
        if (bankAccount != null) {
            authorizationService.cancelIfUserIsUnauthorized(person, bankAccount)
        }
        account.bankAccountId = bankAccount?.id
        accountDao.update(account)
        if (importHistory) {
            bankingService.synchronise(person, account)
        }
        return account
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

    fun findByBudget(person: Person, budget: Budget) : List<AccountWithMetadata> {
        authorizationService.cancelIfUserIsUnauthorized(person, budget)
        var accountList = accountDao.findByBudget(budget)
        accountList.forEach { 
            it.syncedUntil = getSyncedUntil(it)
        } 
        return accountList
    }
    
    // TODO move as part of the DAO process to improve performance
    fun getSyncedUntil(account: AccountWithMetadata): Long {
        val bankAccountId = account.bankAccountId
        if ( bankAccountId != null) {
            val bankAccount = bankAccountDao.getById(bankAccountId)
            val bankAgreement = bankingService.getAgreementById(bankAccount.agreementId)
            return Time.ninetyDaysLater(bankAgreement.timestamp)
        } else {
            return 0
        }
    }
}
