package open.tresorier.dao

import open.tresorier.dependenciesinjection.ITest
import open.tresorier.model.Budget
import open.tresorier.model.banking.BankAccount
import open.tresorier.model.banking.BankAgreement
import open.tresorier.model.enum.ProfileEnum
import open.tresorier.utils.TestData
import open.tresorier.utils.Time
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.koin.core.component.inject

open class BankAccountDaoTest : ITest {

    val bankAgreementDao by inject<IBankAgreementDao>()
    val bankAccountDao by inject<IBankAccountDao>()
    val budgetDao by inject<IBudgetDao>()


    @Test fun testGetBankAccountIgnoreExpired() {
        val budget = Budget("Perso", TestData.person1Id, ProfileEnum.PROFILE_USER)
        budgetDao.insert(budget)

        val bankAgreement = BankAgreement(budget.id, "bankId", Time.now())
        this.bankAgreementDao.insert(bankAgreement)
        val bankAccount = BankAccount("compte", bankAgreement.id)
        this.bankAccountDao.insert(bankAccount)

        val oldAgreement = BankAgreement(budget.id, "bankId", TestData.oldTimestamp)
        this.bankAgreementDao.insert(oldAgreement)
        val oldAccount = BankAccount("epargne", oldAgreement.id)
        this.bankAccountDao.insert(oldAccount)

        val bankAccounts = bankAccountDao.findByBudget(budget)
        assertEquals(1, bankAccounts.size)
        assertEquals(bankAccount.id, bankAccounts[0].id)
    }
}