package open.tresorier.services

import open.tresorier.model.Person
import open.tresorier.model.Account
import open.tresorier.model.Budget
import open.tresorier.exception.TresorierException
import open.tresorier.model.banking.Bank
import open.tresorier.model.banking.BankAccount
import open.tresorier.model.banking.PublicBankAccount
import open.tresorier.model.banking.BankAgreement
import open.tresorier.banking.IBankingPort
import open.tresorier.dao.IOperationDao
import open.tresorier.dao.IBankAgreementDao
import open.tresorier.dao.IBankAccountDao
import open.tresorier.dao.IAccountDao
import open.tresorier.dao.IBudgetDao

class BankingService (
    private val bankingAdapter: IBankingPort,
    private val authorizationService: AuthorizationService,
    private val operationDao: IOperationDao,
    private val bankAgreementDao: IBankAgreementDao,
    private val accountDao: IAccountDao,
    private val bankAccountDao: IBankAccountDao,
    private val budgetDao: IBudgetDao) {

    fun getLinkForUserAgreement(person: Person, budget: Budget, bankId: String) : String {
        this.authorizationService.cancelIfUserIsUnauthorized(person, budget)
        return this.bankingAdapter.getLinkForUserAgreement(budget, bankId)
    }

    fun updateBankAccountList(person: Person, agreement: BankAgreement) {
        this.authorizationService.cancelIfUserIsUnauthorized(person, agreement)
        val bankAccounts = this.bankingAdapter.getBankAccountList(agreement)
        bankAccounts.forEach { this.bankAccountDao.insert(it) }
    }

    fun findBankAccountByBudget(person: Person, budget: Budget) : List<PublicBankAccount>{
        this.authorizationService.cancelIfUserIsUnauthorized(person, budget)
        return bankAccountDao.findByBudget(budget)
    }

    fun synchronise(person: Person) {
        val budgets = this.budgetDao.findByPersonId(person.id)
        budgets.forEach {
            val accounts = this.accountDao.findByBudget(it)
            accounts.forEach {
                synchroniseAccount(it)
            }
        }
    }

    fun synchronise(person: Person, account: Account) {
        this.authorizationService.cancelIfUserIsUnauthorized(person, account)
        synchroniseAccount(account)
        
    }

    fun synchroniseAccount(account: Account) {
        val operations = this.bankingAdapter.getOperations(account)
        operations.forEach { 
            try {
                this.operationDao.insert(it) 
            } catch (e: Throwable) {
                 /* ignore exception, enough for now to handle not importing the same operation twice
                it will return an exception for not unique import identifier and ignored  */ 
            }
        }
    }

    fun getAvailableBanks() : List<Bank> {
        return this.bankingAdapter.getAvailableBanks()
    }

    fun getAgreementById(id: String) : BankAgreement {
        return this.bankAgreementDao.getById(id)
    }

    fun getBankAccountById(person: Person, id: String) : BankAccount {
        val bankAccount = bankAccountDao.getById(id)
        authorizationService.cancelIfUserIsUnauthorized(person, bankAccount)
        return bankAccount
    }
}