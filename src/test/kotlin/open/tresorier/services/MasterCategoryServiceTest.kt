package open.tresorier.services

import open.tresorier.dependenciesinjection.ITest
import open.tresorier.dao.*
import open.tresorier.model.*
import open.tresorier.model.enum.ProfileEnum
import open.tresorier.utils.TestData

import org.koin.core.component.inject
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions

class MasterCategoryServiceTest : ITest {

    val personService by inject<PersonService>()
    val budgetService by inject<BudgetService>()
    val accountService by inject<AccountService>()
    val masterCategoryService by inject<MasterCategoryService>()
    val masterCategoryDao by inject<IMasterCategoryDao>()

    @Test
    fun testUpdateOperationWithoutChangingDate() {
        val sita: Person = personService.createPerson(
            "Sita Lakshmi", "WhyDontYouTrustMe!", "sita@ramayana.in", ProfileEnum.PROFILE_USER
        )
        sita.billingStatus = true
        val budget: Budget = budgetService.findByUser(sita)[0]
        val account: Account = accountService.create(
            sita, budget, "personal account", TestData.jan_14_2022, 10000
        )
        val masterCategory = MasterCategory("Fixed expense", budget.id, null)
        masterCategoryDao.insert(masterCategory)
        
        masterCategoryService.update(sita, masterCategory, null, "red", null, null)
        var masterCategoryInDb = masterCategoryDao.getById(masterCategory.id)
        Assertions.assertEquals("red", masterCategoryInDb.color)
        
        masterCategoryService.update(sita, masterCategory, null, "null", null, null)
        masterCategoryInDb = masterCategoryDao.getById(masterCategory.id)
        Assertions.assertEquals(null, masterCategoryInDb.color)
    }

}
