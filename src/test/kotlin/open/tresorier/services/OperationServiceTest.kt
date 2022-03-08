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
            mileva, budget, "personal account", TestData.jan_14_2022, 10000
        )
        val masterCategory = MasterCategory("Fixed expense", budget.id)
        masterCategoryDao.insert(masterCategory)
        val category = Category("oftenAllocatedCategory", masterCategory.id)
        categoryDao.insert(category)
        val operationCreated = operationService.create(mileva, account, TestData.jan_16_2022, category, -1000, "encas")
        val orderInDayAtCreation = operationCreated.orderInDay              // needed for prevent side effect

        val operationModified = operationService.update(mileva, operationCreated, account, TestData.jan_16_2022, category, -1200, "encas")

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

    @Test
    fun testOfxOperationDebit() {
        val rosalind: Person = personService.createPerson(
            "Rosalind Franklin", "Cliché_51", "rosalind@adn.uk", ProfileEnum.PROFILE_USER
        )
        rosalind.billingStatus = true
        val budget: Budget = budgetService.findByUser(rosalind)[0]
        val account: Account = accountService.create(
            rosalind, budget, "personal account", TestData.jan_14_2022, 1000
        )
        val ofxDebitOperation: String = "<STMTTRN><TRNTYPE>DEBIT<DTPOSTED>20220114<TRNAMT>-536,50<FITID>2802202220220228-03.01.56.182263<NAME>PRIX LOUISA GROSS HORWITZ<MEMO>PRIX LOUISA GROSS HORWITZ</STMTTRN>"

        val newOperation: Operation = operationService.createOperationFromOFX(rosalind, account, ofxDebitOperation)

        Assertions.assertTrue(newOperation.day.comparable == 20220114 && newOperation.amount == -53650 && newOperation.memo == "PRIX LOUISA GROSS HORWITZ")
    }

    @Test
    fun testOfxOperationCredit() {
        val ada: Person = personService.createPerson(
            "Ada Lovelace", "Programme@1815", "ada@dev.uk", ProfileEnum.PROFILE_USER
        )
        ada.billingStatus = true
        val budget: Budget = budgetService.findByUser(ada)[0]
        val account: Account = accountService.create(
            ada, budget, "personal account", TestData.jan_14_2022, 1000
        )
        val ofxDebitOperation: String = "<STMTTRN><TRNTYPE>CREDIT<DTPOSTED>20220114<TRNAMT>+89,50<FITID>2802202220220228-03.01.56.182263<NAME>VIR SEPA Babbage<MEMO>VIR SEPA Babbage</STMTTRN>"

        val newOperation = operationService.createOperationFromOFX(ada, account, ofxDebitOperation)

        Assertions.assertTrue(newOperation.day.comparable == 20220114 && newOperation.amount == 8950 && newOperation.memo == "VIR SEPA Babbage")
    }

    @Test
    fun testCuttingOperationsFromOfx() {
        val irene: Person = personService.createPerson(
            "Irène Joliot-Curie", "Nobel_1935", "irene@chimie.fr", ProfileEnum.PROFILE_USER
        )
        irene.billingStatus = true
        val budget: Budget = budgetService.findByUser(irene)[0]
        val account: Account = accountService.create(
            irene, budget, "personal account", TestData.jan_14_2022, 1000
        )
        val ofxFile: String ="<OFXHEADER:100DATA:OFXSGMLVERSION:102SECURITY:NONEENCODING:USASCIICHARSET:1252COMPRESSION:NONEOLDFILEUID:NONENEWFILEUID:NONE<OFX><SIGNONMSGSRSV1><SONRS><STATUS><CODE>0<SEVERITY>INFO</STATUS><DTSERVER>20220302<LANGUAGE>FRA<DTPROFUP>20220302<DTACCTUP>20220302</SONRS></SIGNONMSGSRSV1><BANKMSGSRSV1><STMTTRNRS><TRNUID>00<STATUS><CODE>0<SEVERITY>INFO</STATUS><STMTRS><CURDEF>EUR<BANKACCTFROM><BANKID>42559<BRANCHID>10000<ACCTID>04065601113<ACCTTYPE>SAVINGS</BANKACCTFROM><BANKTRANLIST><DTSTART>20220215<DTEND>20220228<STMTTRN><TRNTYPE>DEBIT<DTPOSTED>20220228<TRNAMT>-8,85<FITID>2802202220220228-17.02.34.872431<NAME>VIR SEPA DENISE EVE<MEMO>VIR SEPA DENISE EVE</STMTTRN><STMTTRN><TRNTYPE>CREDIT<DTPOSTED>20220215<TRNAMT>+40,00<FITID>1502202220220215-05.39.35.662478<NAME>POLONIUM<MEMO>POLONIUM</STMTTRN></BANKTRANLIST><LEDGERBAL><BALAMT>5693,5<DTASOF>20220302</LEDGERBAL></STMTRS></STMTTRNRS></BANKMSGSRSV1></OFX>"
        val listOperations: List<String> = operationService.separateOperationsOfOfxFile(irene, account, ofxFile)
        
        Assertions.assertTrue(listOperations[0] == "<STMTTRN><TRNTYPE>DEBIT<DTPOSTED>20220228<TRNAMT>-8,85<FITID>2802202220220228-17.02.34.872431<NAME>VIR SEPA DENISE EVE<MEMO>VIR SEPA DENISE EVE</STMTTRN>" && listOperations[1] == "<STMTTRN><TRNTYPE>CREDIT<DTPOSTED>20220215<TRNAMT>+40,00<FITID>1502202220220215-05.39.35.662478<NAME>POLONIUM<MEMO>POLONIUM</STMTTRN>" && listOperations.size == 2)
    }

    @Test
    fun testOfxFileWithoutOperation() {
        val emmanuelle: Person = personService.createPerson(
            "Emmanuelle Carpentier", "Nobel_2020", "emmanuelle@chimie.fr", ProfileEnum.PROFILE_USER
        )
        emmanuelle.billingStatus = true
        val budget: Budget = budgetService.findByUser(emmanuelle)[0]
        val account: Account = accountService.create(
            emmanuelle, budget, "personal account", TestData.jan_14_2022, 1000
        )
        val ofxFile: String ="<OFXHEADER:100DATA:OFXSGMLVERSION:102SECURITY:NONEENCODING:USASCIICHARSET:1252COMPRESSION:NONEOLDFILEUID:NONENEWFILEUID:NONE<OFX><SIGNONMSGSRSV1><SONRS><STATUS><CODE>0<SEVERITY>INFO</STATUS><DTSERVER>20220302<LANGUAGE>FRA<DTPROFUP>20220302<DTACCTUP>20220302</SONRS></SIGNONMSGSRSV1><BANKMSGSRSV1><STMTTRNRS><TRNUID>00<STATUS><CODE>0<SEVERITY>INFO</STATUS><STMTRS><CURDEF>EUR<BANKACCTFROM><BANKID>42559<BRANCHID>10000<ACCTID>04065601113<ACCTTYPE>SAVINGS</BANKACCTFROM><BANKTRANLIST><DTSTART>20220215<DTEND>20220228</BANKTRANLIST><LEDGERBAL><BALAMT>5693,5<DTASOF>20220302</LEDGERBAL></STMTRS></STMTTRNRS></BANKMSGSRSV1></OFX>"
        val listOperations: List<String> = operationService.separateOperationsOfOfxFile(emmanuelle, account, ofxFile)

        Assertions.assertEquals(listOperations.size, 0)
    }

    @Test
    fun testOfxFileWithOnlyOneOperation() {
        val katherine: Person = personService.createPerson(
            "Katherine Johnson", "NASA_1953", "katherine@fusée.com", ProfileEnum.PROFILE_USER
        )
        katherine.billingStatus = true
        val budget: Budget = budgetService.findByUser(katherine)[0]
        val account: Account = accountService.create(
            katherine, budget, "personal account", TestData.jan_14_2022, 1000
        )
        val ofxFile: String ="<OFXHEADER:100DATA:OFXSGMLVERSION:102SECURITY:NONEENCODING:USASCIICHARSET:1252COMPRESSION:NONEOLDFILEUID:NONENEWFILEUID:NONE<OFX><SIGNONMSGSRSV1><SONRS><STATUS><CODE>0<SEVERITY>INFO</STATUS><DTSERVER>20220302<LANGUAGE>FRA<DTPROFUP>20220302<DTACCTUP>20220302</SONRS></SIGNONMSGSRSV1><BANKMSGSRSV1><STMTTRNRS><TRNUID>00<STATUS><CODE>0<SEVERITY>INFO</STATUS><STMTRS><CURDEF>EUR<BANKACCTFROM><BANKID>42559<BRANCHID>10000<ACCTID>04065601113<ACCTTYPE>SAVINGS</BANKACCTFROM><BANKTRANLIST><DTSTART>20220215<DTEND>20220228<STMTTRN><TRNTYPE>DEBIT<DTPOSTED>20220228<TRNAMT>-8,85<FITID>2802202220220228-17.02.34.872431<NAME>MERCURY REDSTONE 3<MEMO>MERCURY REDSTONE 3</STMTTRN></BANKTRANLIST><LEDGERBAL><BALAMT>5693,5<DTASOF>20220302</LEDGERBAL></STMTRS></STMTTRNRS></BANKMSGSRSV1></OFX>"
        val listOperations: List<String> = operationService.separateOperationsOfOfxFile(katherine, account, ofxFile)

        Assertions.assertTrue(listOperations.size == 1 && listOperations[0] == "<STMTTRN><TRNTYPE>DEBIT<DTPOSTED>20220228<TRNAMT>-8,85<FITID>2802202220220228-17.02.34.872431<NAME>MERCURY REDSTONE 3<MEMO>MERCURY REDSTONE 3</STMTTRN>")

    }

 }