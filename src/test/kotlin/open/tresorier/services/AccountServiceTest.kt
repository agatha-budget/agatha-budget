package open.tresorier.services

import open.tresorier.dao.ICategoryDao
import open.tresorier.dependenciesinjection.ITest
import open.tresorier.model.*
import open.tresorier.model.banking.*
import open.tresorier.model.enum.ProfileEnum
import open.tresorier.model.Person
import org.junit.jupiter.api.Test
import open.tresorier.utils.Time
import org.koin.core.component.inject
import org.junit.jupiter.api.Assertions
import open.tresorier.dao.*

class AccountServiceTest : ITest {

    val personService by inject<PersonService>()
    val budgetService by inject<BudgetService>()
    val accountService by inject<AccountService>()
    val bankAgreementDao by inject<IBankAgreementDao>()
    val bankAccountDao by inject<IBankAccountDao>()
    val accountDao by inject<IAccountDao>()

    @Test
    fun getAccountWithNoCurrentSyncForBudget() {
        val cleopatre: Person = personService.createPerson(
            "UnSynced Cleo", ProfileEnum.PROFILE_USER
        )
        val budget = budgetService.findByUser(cleopatre)[0]

        val account = accountService.create(cleopatre, budget, "Banque de Thonis", Day(Month(12, 2023), 12), 0)


        val accountList = accountService.findByBudget(cleopatre, budget)
        val expectedList = listOf(
            AccountWithMetadata.createFromAccount(account, 0)
        )
        Assertions.assertEquals(expectedList.toString(), accountList.toString())
    }

    @Test
    fun getAccountWithSyncForBudget() {

        val nefertiti: Person = personService.createPerson(
            "Synced Nefer", ProfileEnum.PROFILE_USER
        )
        val budget = budgetService.findByUser(nefertiti)[0]

        val time = Time.now()
        val bankAgreement = BankAgreement(budget.id, "BestEthicalBankId", time)
        bankAgreementDao.insert(bankAgreement)
        val bankAccount = BankAccount("courant", bankAgreement.id, "id")
        bankAccountDao.insert(bankAccount)
        val account = Account("account", budget.id, false, bankAccount.id)
        accountDao.insert(account)

        val accountList = accountService.findByBudget(nefertiti, budget)
        val expectedList = listOf(
            AccountWithMetadata.createFromAccount(account, 0, Time.ninetyDaysLater(time))
        )
        Assertions.assertEquals(expectedList.toString(), accountList.toString())

    }
}