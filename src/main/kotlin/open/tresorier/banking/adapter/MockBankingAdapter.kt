package open.tresorier.banking.adapter

import open.tresorier.banking.IBankingPort

import open.tresorier.model.Person
import open.tresorier.model.Operation
import open.tresorier.model.Account
import open.tresorier.model.banking.Bank


class MockBankingAdapter() : IBankingPort {
    
    override fun getLinkForUserAgreement(person: Person, bankId: String) : String {
        // give personId in the redirect after agreement url.
        return "url to redirect and save RequisitionId"
    }

    override fun updateBankAccountList(person: Person) {
        // after user go to redirect after giving his agreement
        // for each requisition of person {
           // get accounts 
        //}
        //get bankAccounts with RequisitionId
    }

    override fun getOperations(account: Account) : List<Operation> {
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