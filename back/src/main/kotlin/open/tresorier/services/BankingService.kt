package open.tresorier.services

import open.tresorier.banking.IBankingPort
import open.tresorier.dao.*
import open.tresorier.model.Account
import open.tresorier.model.Budget
import open.tresorier.model.Person
import open.tresorier.model.banking.Bank
import open.tresorier.model.banking.BankAccount
import open.tresorier.model.banking.BankAgreement
import open.tresorier.model.banking.PublicBankAccount
import open.tresorier.model.enum.ActionEnum
import open.tresorier.utils.Time
import open.tresorier.exception.TresorierException
import open.tresorier.exception.BankingException


class BankingService (
    private val bankingAdapter: IBankingPort,
    private val authorizationService: AuthorizationService,
    private val operationDao: IOperationDao,
    private val bankAgreementDao: IBankAgreementDao,
    private val accountDao: IAccountDao,
    private val bankAccountDao: IBankAccountDao,
    private val budgetDao: IBudgetDao,
    private val personDao: IPersonDao,
    private val userActivityService: UserActivityService) {

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
        val bankAccounts = bankAccountDao.findByBudget(budget)
        bankAccounts.forEach { it.balance = getBankAccountBalance(it.bankingId) } 
        return bankAccounts
    }

    fun synchronise() {
        val persons = this.personDao.findAll()
        persons.forEach{
            synchronise(it)
        }
    }

    fun synchronise(person: Person) {
        val budgets = this.budgetDao.findByPersonId(person.id)
        budgets.forEach {
            val accounts = this.accountDao.findByBudget(it)
            accounts.forEach {
                val agreement = this.bankAgreementDao.findByAccount(it)
                if (agreement != null && Time.isLessThan89DaysAgo(agreement.timestamp)) {
                    try {
                        synchroniseAccount(it, agreement.timestamp)
                    } catch (e: BankingException) {
                        TresorierException("Can't synchronise account ${it.id} for person ${person.id}", e)
                    }
                }
            }
        }
        userActivityService.create(person, Time.now(), ActionEnum.ACTION_BANK_SYNC)
    }

    fun synchronise(person: Person, account: Account) {
        this.authorizationService.cancelIfUserIsUnauthorized(person, account)
        synchroniseAccount(account)
    }

    private fun synchroniseAccount(account: Account, from: Long? = null) {
        val operations = this.bankingAdapter.getOperations(account, from)
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

    fun getBankAccountBalance(bankAccountBankId: String): Int? {
        return this.bankingAdapter.getBankAccountBalance(bankAccountBankId)
    }

    fun getBankAccountById(person: Person, id: String) : BankAccount {
        val bankAccount = bankAccountDao.getById(id)
        authorizationService.cancelIfUserIsUnauthorized(person, bankAccount)
        return bankAccount
    }
}