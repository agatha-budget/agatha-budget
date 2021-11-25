package open.tresorier.services

import open.tresorier.dao.ICategoryDao
import open.tresorier.dependenciesinjection.ITest
import open.tresorier.model.Category
import open.tresorier.model.Person
import org.junit.jupiter.api.Test
import org.koin.core.component.inject

class AuthorizationServiceTest : ITest {

    private val categoryDao by inject<ICategoryDao>()
    private val authorizationService by inject<AuthorizationService>()

    @Test
    fun testCheckUniversalCategory() {
        val universalCategory = categoryDao.getById(Category.INCOME_ID)
        val person = Person("a","a","a","a", true)
        // no exception is thrown
        authorizationService.cancelIfUserIsUnauthorized(person, universalCategory)
    }
}
