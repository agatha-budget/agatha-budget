package open.tresorier.services

import open.tresorier.model.Person
import open.tresorier.model.Account
import open.tresorier.model.Budget
import open.tresorier.exception.TresorierException
import open.tresorier.model.banking.Bank
import open.tresorier.model.banking.BankAccount
import open.tresorier.model.banking.BankAgreement
import open.tresorier.banking.IBankingPort
import open.tresorier.dao.IOperationDao
import open.tresorier.dao.IBankAgreementDao
import open.tresorier.dao.IBankAccountDao
import open.tresorier.dao.IAccountDao


class BankingService (
    private val bankingAdapter: IBankingPort,
    private val authorizationService: AuthorizationService,
    private val operationDao: IOperationDao,
    private val bankAgreementDao: IBankAgreementDao,
    private val accountDao: IAccountDao,
    private val bankAccountDao: IBankAccountDao) {

    fun getLinkForUserAgreement(person: Person, bankId: String) : String {
        return this.bankingAdapter.getLinkForUserAgreement(person, bankId)
    }

    fun updateBankAccountList(person: Person, agreement: BankAgreement) {
        this.authorizationService.cancelIfUserIsUnauthorized(person, agreement)
        val bankAccounts = this.bankingAdapter.getBankAccountList(agreement)
        bankAccounts.forEach { this.bankAccountDao.insert(it) }
    }

    fun synchronise(person: Person, budget: Budget) {
        this.authorizationService.cancelIfUserIsUnauthorized(person, budget)
        val accounts = this.accountDao.findByBudget(budget)
        accounts.forEach {
            val operations = this.bankingAdapter.getOperations(it)
            operations.forEach { this.operationDao.insert(it) }
        }
    }

    fun synchronise(person: Person, account: Account) {
        this.authorizationService.cancelIfUserIsUnauthorized(person, account)
        val operations = this.bankingAdapter.getOperations(account)
        operations.forEach { this.operationDao.insert(it) }
    }

    fun getAvailableBanks() : List<Bank> {
        return this.bankingAdapter.getAvailableBanks()
    }

    fun getAgreementById(id: String) : BankAgreement {
        return this.bankAgreementDao.getById(id)
    }
}