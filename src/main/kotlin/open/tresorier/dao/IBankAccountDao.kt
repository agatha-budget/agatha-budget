package open.tresorier.dao

import open.tresorier.model.Person
import open.tresorier.model.Budget
import open.tresorier.model.banking.BankAccount
import open.tresorier.model.banking.PublicBankAccount
import open.tresorier.model.banking.BankAgreement

interface IBankAccountDao {
    fun insert(bankAccount: BankAccount) : BankAccount
    fun update(bankAccount: BankAccount) : BankAccount
    fun findByAgreement(agreement: BankAgreement): List<BankAccount>
    fun findByBudget(budget: Budget): List<PublicBankAccount>
    fun getById(id: String): BankAccount
    fun getOwner(bankAccount: BankAccount) : Person
}