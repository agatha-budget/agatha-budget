package open.tresorier.dao

import open.tresorier.dependenciesinjection.ITest
import open.tresorier.exception.TresorierException
import open.tresorier.model.Budget
import open.tresorier.model.Person
import open.tresorier.utils.TestData
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.koin.core.component.inject
import open.tresorier.model.enum.ProfileEnum

open class BudgetDaoTest : ITest {

    val budgetDao by inject<IBudgetDao>()
    val personDao by inject<IPersonDao>()

    @Test
    fun getOwner() {
        val budget = budgetDao.getById(TestData.budget1Id)
        val expectedOwner = personDao.getById(TestData.person1Id)
        val owner = budgetDao.getOwner(budget)
        assertEquals(expectedOwner.email, owner.email)
    }

    @Test
    fun getOwnerForJustAdded() {
        val budget = Budget("wellAllocatedBudget", TestData.person1Id, ProfileEnum.PROFILE_USER)
        budgetDao.insert(budget)
        val expectedOwner = personDao.getById(TestData.person1Id)
        val owner = budgetDao.getOwner(budget)
        assertEquals(expectedOwner.email, owner.email)
    }

    @Test
    fun getOwnerForUnstored() {
        val budget = Budget("budget", TestData.person1Id, ProfileEnum.PROFILE_USER)
        val exception = Assertions.assertThrows(TresorierException::class.java) {
            budgetDao.getOwner(budget)
        }
        assertEquals("the given object appears to have no owner", exception.message)
    }

    @Test fun testFindByPersonId() {
        val anne = Person("anne", "ahahahahah", "anna@mail.eu")
        personDao.insert(anne)
        val lucie = Person("lucie", "lululu", "lucie@mail.eu")
        personDao.insert(lucie)
        val budgetAnne1 = Budget("anne-B1", anne.id, ProfileEnum.PROFILE_USER)
        budgetDao.insert(budgetAnne1)
        val budgetAnne2 = Budget("anne-B2", anne.id, ProfileEnum.PROFILE_USER)
        budgetDao.insert(budgetAnne2)
        val budgetLucie1 = Budget("lucie-B1", lucie.id, ProfileEnum.PROFILE_USER)
        budgetDao.insert(budgetLucie1)
        val anneBudgets : List<Budget> = budgetDao.findByPersonId(anne.id)
        val anneBudgetsName : MutableList<String> = mutableListOf()
        for (budget in anneBudgets){
            anneBudgetsName.add(budget.name)
        }
        val expectedAnneBudgetsName : MutableList<String> = mutableListOf("anne-B1", "anne-B2")

        assertEquals(expectedAnneBudgetsName, anneBudgetsName)
    }

    @Test fun testInsertWithInvalidPersonId() {
        val invalidBudget = Budget("anne-B3", "not_a_real_id", ProfileEnum.PROFILE_USER)
        val exception = Assertions.assertThrows(TresorierException::class.java) {
            budgetDao.insert(invalidBudget)
        }
        assertEquals("could not insert budget : $invalidBudget", exception.message)

    }

    @Test fun testUpdateWithInvalidPersonId() {
        val celine = Person("celine", "cecece", "cecile@mail.eu")
        personDao.insert(celine)
        val budgetCeline = Budget("celine-B1", celine.id, ProfileEnum.PROFILE_USER)
        budgetDao.insert(budgetCeline)
        budgetCeline.personId = "not_a_real_id"
        val exception = Assertions.assertThrows(TresorierException::class.java) {
            budgetDao.update(budgetCeline)
        }
        assertEquals("could not update budget : $budgetCeline", exception.message)

    }
}
