package open.tresorier.dao

import open.tresorier.model.Person
import open.tresorier.model.banking.BankAccount
import open.tresorier.model.banking.BankAgreement

interface IBankAccountDao {
    fun insert(bankAccount: BankAccount) : BankAccount
    fun update(bankAccount: BankAccount) : BankAccount
    fun findByAgreement(BankAgreement: BankAgreement): List<BankAccount>
    fun getById(id: String): BankAccount
    fun getOwner(bankAccount: BankAccount) : Person
}