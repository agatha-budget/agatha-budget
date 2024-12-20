package open.tresorier.banking

import open.tresorier.model.Account
import open.tresorier.model.Budget
import open.tresorier.model.Operation
import open.tresorier.model.banking.Bank
import open.tresorier.model.banking.BankAccount
import open.tresorier.model.banking.BankAgreement

interface IBankingPort {
    fun getLinkForUserAgreement(budget: Budget, bankId: String) : String
    fun getBankAccountList(agreement: BankAgreement) : List<BankAccount>
    fun getOperations(account: Account, _from: Long? = null) : List<Operation>
    fun getAvailableBanks() : List<Bank>
    fun getBankAccountBalance(id: String) : Int?
}