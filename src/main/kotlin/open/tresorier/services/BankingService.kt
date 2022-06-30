package open.tresorier.services

import open.tresorier.model.Person
import open.tresorier.model.Account
import open.tresorier.model.banking.Bank
import open.tresorier.model.banking.BankAccount
import open.tresorier.banking.IBankingPort

class BankingService(private val bankingAdapter: IBankingPort, private val authorizationService: AuthorizationService) {

    fun getLinkForUserAgreement(person: Person, bankId: String) : String {
        return this.bankingAdapter.getLinkForUserAgreement(person, bankId)
    }

    fun updateBankAccountList(person: Person) {
        this.bankingAdapter.updateBankAccountList(person)
    }

    fun associate(BankAccount : BankAccount, Account : Account) {
        //associate bankAccounts with Account 
    }

    fun synchronise(person: Person) {
        //this.bankingAdapter.getTransactions(person)
    }

    fun synchronise(person: Person, account: Account) {
        this.authorizationService.cancelIfUserIsUnauthorized(person, account)
        this.bankingAdapter.getTransactions(account)
    }

    fun getAvailableBanks() : List<Bank> {
        return this.bankingAdapter.getAvailableBanks()
    }
}