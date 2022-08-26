package open.tresorier.dao

import open.tresorier.dependenciesinjection.ITest
import open.tresorier.exception.TresorierException
import open.tresorier.model.*
import open.tresorier.model.banking.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.koin.core.component.inject
import open.tresorier.utils.Time
import open.tresorier.utils.TestData

open class BankAccountDaoTest : ITest {

    val bankAgreementDao by inject<IBankAgreementDao>()
    val bankAccountDao by inject<IBankAccountDao>()
    val budgetDao by inject<IBudgetDao>()


    @Test fun testGetBankAccountIgnoreExpired() {
        val bankAgreement = BankAgreement(TestData.budget1Id, "bankId", Time.now())
        this.bankAgreementDao.insert(bankAgreement)
        val bankAccount = BankAccount("compte", bankAgreement.id)
        this.bankAccountDao.insert(bankAccount)

        val oldAgreement = BankAgreement(TestData.budget1Id, "bankId", TestData.oldTimestamp)
        this.bankAgreementDao.insert(oldAgreement)
        val oldAccount = BankAccount("epargne", oldAgreement.id)
        this.bankAccountDao.insert(oldAccount)

        val bankAccounts = bankAccountDao.findByBudget(budgetDao.getById(TestData.budget1Id))
        assertEquals(1, bankAccounts.size)
        assertEquals(bankAccount.id, bankAccounts[0].id)
    }
}