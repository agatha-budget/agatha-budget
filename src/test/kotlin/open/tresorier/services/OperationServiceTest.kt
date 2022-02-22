package open.tresorier.services

import open.tresorier.dependenciesinjection.ITest
import open.tresorier.model.Operation
import open.tresorier.model.*
import org.junit.jupiter.api.Test
import org.koin.core.component.inject
import open.tresorier.utils.TestData
import open.tresorier.dao.*
import org.junit.jupiter.api.Assertions
import open.tresorier.model.enum.ProfileEnum

class OperationServiceTest : ITest {

    val masterCategoryDao by inject<IMasterCategoryDao>()
    val categoryDao by inject<ICategoryDao>()
    val operationService by inject<OperationService>()
    val personService by inject<PersonService>()
    val budgetService by inject<BudgetService>()
    val accountService by inject<AccountService>()

    @Test
    fun testUpdateOperationWithoutChangingDate() {
        val mileva: Person = personService.createPerson(
            "Mileva Maric", "EPFZ!1896", "mileva@relativite.ch", ProfileEnum.PROFILE_USER
        )
        mileva.billingStatus = true
        val budget: Budget = budgetService.findByUser(mileva)[0]
        val account: Account = accountService.create(
            mileva, budget, "personal account", TestData.jan_14_2022, 1000
        )
        val masterCategory = MasterCategory("Fixed expense", budget.id)
        masterCategoryDao.insert(masterCategory)
        val category = Category("oftenAllocatedCategory", masterCategory.id)
        categoryDao.insert(category)
        val operationCreated = operationService.create(mileva, account, TestData.jan_16_2022, category, 1000, "")
        val orderInDayAtCreation = operationCreated.orderInDay              // needed for prevent side effect

        val operationModified = operationService.update(mileva, operationCreated, account, TestData.jan_16_2022, category, 2000, "memo")

        Assertions.assertEquals(orderInDayAtCreation, operationModified.orderInDay)
    }

    @Test
    fun testUpdateOperationWithChangingDate() {
        val emilie: Person = personService.createPerson(
            "Emilie du Châtelet", "1739_LaNatureDuFeu", "emilie@leibniz.fr", ProfileEnum.PROFILE_USER
        )
        emilie.billingStatus = true
        val budget: Budget = budgetService.findByUser(emilie)[0]
        val account: Account = accountService.create(
            emilie, budget, "personal account", TestData.jan_14_2022, 1000
        )
        val masterCategory = MasterCategory("Fixed expense", budget.id)
        masterCategoryDao.insert(masterCategory)
        val category = Category("oftenAllocatedCategory", masterCategory.id)
        categoryDao.insert(category)
        val operationCreated = operationService.create(emilie, account, TestData.jan_16_2022, category, 1000, "")
        val orderInDayAtCreation = operationCreated.orderInDay              // needed for prevent side effect
        
        val operationModified = operationService.update(emilie, operationCreated, account, TestData.jan_15_2022, null, null, null)

        Assertions.assertTrue(orderInDayAtCreation < operationModified.orderInDay)
    }

}