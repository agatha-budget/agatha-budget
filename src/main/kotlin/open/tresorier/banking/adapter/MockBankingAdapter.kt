package open.tresorier.banking.adapter

import open.tresorier.banking.IBankingPort
import open.tresorier.model.Account
import open.tresorier.model.Budget
import open.tresorier.model.Operation
import open.tresorier.model.banking.Bank
import open.tresorier.model.banking.BankAccount
import open.tresorier.model.banking.BankAgreement

class MockBankingAdapter() : IBankingPort {
    
    override fun getLinkForUserAgreement(budget: Budget, bankId: String) : String {
        // give personId in the redirect after agreement url.
        return "url to redirect and save RequisitionId"
    }

    override fun getBankAccountList(agreement: BankAgreement) : List<BankAccount> {
        // after user go to redirect after giving his agreement
        // for each requisition of person {
           // get accounts 
        //}
        //get bankAccounts with RequisitionId
        return listOf()
    }

    override fun getOperations(account: Account, _from: Long?) : List<Operation> {
        // add new transaction for account
        return listOf()
    }

    override fun getAvailableBanks() : List<Bank> {
        return listOf(
            Bank("CREDIT_COOPERATIF_CCOPFRPPXXX",
             "Crédit Coopératif",
             "https://cdn.nordigen.com/ais/CREDIT_COOPERATIF_CCOPFRPPXXX.png"
            )
        )
    }
}