package open.tresorier.services

import open.tresorier.dependenciesinjection.ITest
import open.tresorier.model.Account
import open.tresorier.model.Budget
import open.tresorier.model.Person
import open.tresorier.model.enum.ActionEnum
import open.tresorier.model.enum.ProfileEnum
import open.tresorier.utils.TestData
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.koin.core.component.inject

class UserActivityServiceTest : ITest {

    private val personService by inject<PersonService>()
    private val operationService by inject<OperationService>()
    private val userActivityService by inject<UserActivityService>()
    val budgetService by inject<BudgetService>()
    val accountService by inject<AccountService>()

    @Test
    fun testImportFillUserActivityTable () {
        val josephine: Person = personService.createPerson(
            "Joséphine Baker", "RevueNègre!1925", "josephine@resitance.fr", ProfileEnum.PROFILE_USER
        )
        josephine.billingStatus = true
        val budget: Budget = budgetService.findByUser(josephine)[0]
        val account: Account = accountService.create(
            josephine, budget, "personal account", TestData.jan_14_2022, 1000
        )
        val ofxFile: String = "<STMTTRN><TRNTYPE>CREDIT<DTPOSTED>20220114<TRNAMT>+89,50<FITID>2802202220220228-03.01.56.182263<NAME>PETITE CROIX DE LORRAINE<MEMO>PETITE CROIX DE LORRAINE</STMTTRN>"
        operationService.importOfxFile(josephine, account, ofxFile)
        val listImport = userActivityService.getByUser(josephine)

        Assertions.assertEquals(1, listImport.size)
        Assertions.assertEquals(ActionEnum.ACTION_IMPORT, listImport[0].action)
    }
}
