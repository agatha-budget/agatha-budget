package open.tresorier.services

import open.tresorier.dependenciesinjection.ITest
import open.tresorier.model.Category
import open.tresorier.model.Person
import org.junit.jupiter.api.Test
import org.koin.core.component.inject
import open.tresorier.model.enum.ActionEnum
import open.tresorier.model.enum.ProfileEnum
import org.junit.jupiter.api.Assertions
import open.tresorier.dao.*
import open.tresorier.model.*
import open.tresorier.utils.TestData

class UserActivityServiceTest : ITest {

    private val personService by inject<PersonService>()
    private val operationService by inject<OperationService>()
    private val userActivityService by inject<UserActivityService>()
    val budgetService by inject<BudgetService>()
    val accountService by inject<AccountService>()

    @Test
    fun testLoginFillUserActivityTable () {
        val lucie: Person = personService.createPerson(
            "Lucie Aubrac", "DernièreColonne!1940", "lucie@resitance.fr", ProfileEnum.PROFILE_USER
        )
        personService.login("lucie@resitance.fr", "DernièreColonne!1940")
        val listLogin = userActivityService.getByAction(ActionEnum.ACTION_LOGIN)
        
        Assertions.assertEquals(listLogin[0].userId, lucie.id)
        Assertions.assertEquals(listLogin.size, 1)
    }

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
        val listImport = userActivityService.getByAction(ActionEnum.ACTION_IMPORT)

        Assertions.assertEquals(listImport[0].userId, josephine.id)
    }
}
