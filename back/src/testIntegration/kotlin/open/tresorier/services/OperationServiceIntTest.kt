package open.tresorier.services

import open.tresorier.dependenciesinjection.IIntegrationTest
import open.tresorier.exception.TresorierException
import open.tresorier.model.Account
import open.tresorier.model.Budget
import open.tresorier.model.Operation
import open.tresorier.model.Person
import open.tresorier.model.enum.ProfileEnum
import open.tresorier.utils.TestData
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.koin.core.component.inject

class OperationServiceIntTest : IIntegrationTest {

    
    val operationService by inject<OperationService>()
    val personService by inject<PersonService>()
    val budgetService by inject<BudgetService>()
    val accountService by inject<AccountService>()

    @Test
    fun testDeleteOperation() {
        val elizabeth: Person = personService.createPerson(
            "Elizabeth Blackwell", "abolitionniste", "elizabeth@medecin.uk", ProfileEnum.PROFILE_USER
        )
        elizabeth.billingStatus = true
        val budget: Budget = budgetService.findByUser(elizabeth)[0]
        val account: Account = accountService.create(
            elizabeth, budget, "personal account", TestData.jan_14_2022, 1875
        )
        val operation1: Operation = operationService.create(
            elizabeth, account, TestData.jan_14_2022, null, 1869, "The Moral Education of the Young", null, null
        )
        val operation2: Operation = operationService.create(
            elizabeth, account, TestData.jan_14_2022, null, 1849, "major médecine", null, null
            )

        var operationList = operationService.findByAccount(elizabeth, account, null)

        // an initial operation was created during account creation
        Assertions.assertEquals(3, operationList.size)

        operationService.delete(elizabeth, operation1)
        operationList = operationService.findByAccount(elizabeth, account, null)

        Assertions.assertEquals(2, operationList.size)
        Assertions.assertTrue(operationList[0].isEquals(operation2))
    }
    
    @Test
    fun testDeleteOperationWithDaughters() {
        val dorothy: Person = personService.createPerson(
            "Dorothy Crowfoot-Hodgkin", "chimie1964", "dorothy@nobel.uk", ProfileEnum.PROFILE_USER
        )
        dorothy.billingStatus = true
        val budget: Budget = budgetService.findByUser(dorothy)[0]
        val account: Account = accountService.create(
            dorothy, budget, "personal account", TestData.jan_14_2022, 1875
        )
        var motherOperation1: Operation = operationService.create(
            dorothy, account, TestData.jan_14_2022, null, 0, "Royal Society", null, null
        )
        var motherOperation2: Operation = operationService.create(
            dorothy, account, TestData.jan_14_2022, null, 0, "Oxford", null, null
        )
        var daughterOperation = operationService.create(
            dorothy, account, TestData.jan_14_2022, null, 0, "cristallographie", null, motherOperation1
        )
        operationService.create(
            dorothy, account, TestData.jan_14_2022, null, 0, "insuline", null, motherOperation1
        )
        operationService.create(
            dorothy, account, TestData.jan_14_2022, null, 0, "Margaret Thatcher", null, motherOperation2
        )

        val operationList = operationService.findByAccount(dorothy, account, null)

        // an initial operation was created during account creation
        Assertions.assertEquals(3, operationList.size)
        operationService.getById(dorothy,daughterOperation.id)

        operationService.delete(dorothy, motherOperation1)

        Assertions.assertThrows(TresorierException::class.java) {
            operationService.getById(dorothy,motherOperation1.id)
        }
        Assertions.assertThrows(TresorierException::class.java) {
            operationService.getById(dorothy,daughterOperation.id)
        }
    }

}
