package open.tresorier.banking

import open.tresorier.model.Person
import open.tresorier.model.Operation
import open.tresorier.model.Account
import open.tresorier.model.banking.Bank


interface IBankingPort {
    fun getLinkForUserAgreement(person: Person, bankId: String) : String
    fun updateBankAccountList(person: Person)
    fun getTransactions(account: Account) : List<Operation>
    fun getAvailableBanks() : List<Bank>
}