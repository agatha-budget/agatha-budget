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

open class BankAgreementDaoTest : ITest {

    val bankAgreementDao by inject<IBankAgreementDao>()
    val bankAccountDao by inject<IBankAccountDao>()
    val accountDao by inject<IAccountDao>()

    @Test fun testGetForAccountWithAgreement() {
        val bankAgreement = BankAgreement(TestData.budget1Id, "bankId", Time.now())
        this.bankAgreementDao.insert(bankAgreement)
        val bankAccount = BankAccount("compte", bankAgreement.id)
        this.bankAccountDao.insert(bankAccount)
        val account = Account("compte", TestData.budget1Id, false, bankAccount.id)
        this.accountDao.insert(account)
        assertEquals(bankAgreement.id, this.bankAgreementDao.findByAccount(account)?.id)
    }

    @Test fun testGetForAccountWithExpiredAgreement() {
        val bankAgreement = BankAgreement(TestData.budget1Id, "bankId", TestData.oldTimestamp)
        this.bankAgreementDao.insert(bankAgreement)
        val bankAccount = BankAccount("compte", bankAgreement.id)
        this.bankAccountDao.insert(bankAccount)
        val account = Account("compte", TestData.budget1Id, false, bankAccount.id)
        this.accountDao.insert(account)
        assertNull(this.bankAgreementDao.findByAccount(account))
    }

    @Test fun testGetForAccountWithNoAgreement() {
        val account = this.accountDao.getById(TestData.account1Id)
        assertNull(this.bankAgreementDao.findByAccount(account))
    }
}
