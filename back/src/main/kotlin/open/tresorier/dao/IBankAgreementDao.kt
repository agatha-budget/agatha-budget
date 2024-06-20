package open.tresorier.dao

import open.tresorier.model.Account
import open.tresorier.model.Budget
import open.tresorier.model.Person
import open.tresorier.model.banking.BankAgreement

interface IBankAgreementDao {
    fun insert(agreement: BankAgreement) : BankAgreement
    fun update(agreement: BankAgreement) : BankAgreement
    fun getById(id: String): BankAgreement
    fun getOwner(agreement: BankAgreement) : Person
    fun findByBudget(budget: Budget): List<BankAgreement>
    fun findByAccount(account: Account) : BankAgreement?
}