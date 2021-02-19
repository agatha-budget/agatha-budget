package open.tresorier.dao


import open.tresorier.dependenciesinjection.IIntegrationTest
import open.tresorier.exception.TresorierException
import open.tresorier.model.Account
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.koin.core.component.inject

class AccountDaoIntTest : IIntegrationTest {

    val accountDao by inject<IAccountDao>()
    val personDao by inject<IPersonDao>()

    @Test
    fun getAccountOwner() {
        val account = accountDao.getById("1")
        val expectedOwner = personDao.getById("1")
        val owner = accountDao.getOwner(account)
        Assertions.assertEquals(expectedOwner.email, owner.email)
    }

    @Test
    fun getOwnerForUnstoredAccount() {
        val account = Account("courant", "id_of_an_inexisting_budget")
        val exception = Assertions.assertThrows(TresorierException::class.java) {
            accountDao.getOwner(account)
        }
        Assertions.assertEquals("the given object appears to have no owner", exception.message)
    }
}
