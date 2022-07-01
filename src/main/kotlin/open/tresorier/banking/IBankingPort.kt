package open.tresorier.banking

import open.tresorier.model.Person
import open.tresorier.model.Operation
import open.tresorier.model.Account
import open.tresorier.model.banking.Bank
import open.tresorier.model.banking.BankAgreement

interface IBankingPort {
    fun getLinkForUserAgreement(person: Person, bankId: String) : String
    fun revokeAgreement(person: Person, agreement: BankAgreement)
    fun updateBankAccountList(agreement: BankAgreement)
    fun getOperations(account: Account) : List<Operation>
    fun getAvailableBanks() : List<Bank>
}