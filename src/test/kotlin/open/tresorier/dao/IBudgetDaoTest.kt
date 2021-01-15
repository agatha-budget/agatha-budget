package open.tresorier.dao

import open.tresorier.dependenciesinjection.ITest
import open.tresorier.model.Person
import open.tresorier.model.Budget
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.koin.core.component.inject

class BudgetDaoTest : ITest {

    val budgetDao by inject<IBudgetDao>()
    val personDao by inject<IPersonDao>()

    @Test fun testFindByPersonId() {
        val anne = Person("anne", "ahahahahah", "anna@mail.eu")
        personDao.insert(anne)
        val lucie = Person("lucie", "lululu", "lucie@mail.eu")
        personDao.insert(lucie)
        val budgetAnne1 = Budget("anne-B1", anne.id)
        budgetDao.insert(budgetAnne1)
        val budgetAnne2 = Budget("anne-B2", anne.id)
        budgetDao.insert(budgetAnne2)
        val budgetLucie1 = Budget("lucie-B1", lucie.id)
        budgetDao.insert(budgetLucie1)

        val anneBudgets : List<Budget> = budgetDao.findByPersonId(anne.id)
        val anneBudgetsName : MutableList<String> = mutableListOf()
        for (budget in anneBudgets){
            anneBudgetsName.add(budget.name)
        }
        val expectedAnneBudgetsName : MutableList<String> = mutableListOf("anne-B1", "anne-B2")

        assertEquals(expectedAnneBudgetsName, anneBudgetsName)
        }
}
