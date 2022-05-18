 package open.tresorier.services

import open.tresorier.dao.IOperationDao
import open.tresorier.model.*
import open.tresorier.utils.Time

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.regex.*
import java.io.File
import java.io.BufferedReader

class OperationService(private val operationDao: IOperationDao, private val authorizationService: AuthorizationService) {

    fun createInitialOperation(person: Person, account: Account, day: Day, amount: Int){
        authorizationService.cancelIfUserIsUnauthorized(person, account)
        val operation = Operation(account.id, day, Category.INCOME_ID, amount, 1, "Montant initial")
        operationDao.insert(operation)
        authorizationService.cancelIfUserIsUnauthorized(person, operation)
    }

    fun create(person: Person, account: Account, day: Day, category: Category?, amount: Int?, memo: String?) : Operation {
        authorizationService.cancelIfUserIsUnauthorized(person, account)
        val order = Time.now()
        val operation = Operation(account.id, day, category?.id, amount ?: 0, order, memo)
        return operationDao.insert(operation)
    }

    fun getById(person: Person, id: String): Operation {
        val operation = operationDao.getById(id)
        authorizationService.cancelIfUserIsUnauthorized(person, operation)
        return operation
    }

    fun update(person: Person, operation: Operation, account: Account?, newDay: Day?, category: Category?, amount: Int?, memo: String?) : Operation {
        authorizationService.cancelIfUserIsUnauthorized(person, operation)
        newDay?.let {
            if (!it.isEquals(operation.day)) {
                val newOrder = Time.now()
                operation.day = it
                operation.orderInDay = newOrder
            }
        }
        account?.let { operation.accountId = it.id }
        category?.let { operation.categoryId = it.id }
        amount?.let { operation.amount = it }
        memo?.let { operation.memo = it }
        return operationDao.update(operation)
    }

    fun delete(person: Person, operation: Operation) {
        authorizationService.cancelIfUserIsUnauthorized(person, operation)
        operationDao.delete(operation)
    }

    fun findByAccount(person: Person, account: Account) : List<Operation> {
        authorizationService.cancelIfUserIsUnauthorized(person, account)
        return operationDao.findByAccount(account)
    }

    fun findByBudget(person: Person, budget: Budget) : List<Operation> {
        authorizationService.cancelIfUserIsUnauthorized(person, budget)
        return operationDao.findByBudget(budget)
    }
    fun importOfxFile(person: Person, account: Account, fileOfx: String): Int {
        authorizationService.cancelIfUserIsUnauthorized(person, account)
        val formattedFile: String = fileOfx.replace("\n", "").replace("\r", "").replace("\t", "")
        val ofxOperationList = this.splitOfxOperations(formattedFile)
        var nbOperation = 0
        ofxOperationList.forEach {
            val operationCreated = this.createOperationFromOFX(account, it)
            nbOperation ++
            operationDao.insert(operationCreated)
        }
        return nbOperation
    }
    fun splitOfxOperations(ofxFile: String) : List<String> {
        var ofxOperationList = mutableListOf<String>()
        var startTag = ofxFile.indexOf("<STMTTRN>")
        var closeTag = ofxFile.indexOf("</STMTTRN>") + 10
        var lastTag = ofxFile.lastIndexOf("</STMTTRN>") + 10
        while (closeTag != lastTag) {
            ofxOperationList.add(ofxFile.substring(startTag, closeTag))
            startTag = closeTag
            closeTag = ofxFile.indexOf("</STMTTRN>", startTag) + 10
        }
        if (startTag != -1) {
            ofxOperationList.add(ofxFile.substring(startTag, closeTag))
        }
        return ofxOperationList
    }
    fun createOperationFromOFX(account: Account, operation: String): Operation {
        // <TRNTYPE> balise de la nature de l'opération
        var startElement = operation.indexOf("<TRNTYPE>") + 9
        var endElement = operation.indexOf("<", startElement)
        val type = operation.substring(startElement, endElement)
        // <DTPOSTED> balise de la date
        startElement = operation.indexOf("<DTPOSTED>") + 10
        endElement = operation.indexOf("<", startElement)
        var date = operation.substring(startElement, endElement)
        // <TRNAMT> balise du montant
        startElement = operation.indexOf("<TRNAMT>") + 9
        endElement = operation.indexOf("<", startElement)
        val euro = operation.substring(startElement, endElement)
        // <NAME> balise du mémo
        startElement = operation.indexOf("<NAME>") + 6
        endElement = operation.indexOf("<", startElement)
        var memo = operation.substring(startElement, endElement)
        // formatage des données récupérées
        if (!Day.checkComparableIsValid(Integer.parseInt(date))) { // si la date est non valide on met à la date du jour
            date = SimpleDateFormat("yyyyMMdd").format( Date())    // ainsi les opérations où il y a des problèmes sont visibles en premier
            memo = "problème de date " + memo
        }
        val day = Day.createFromComparable(Integer.parseInt(date))
        val cent: String = euro.replace(",", "").replace(".", "")
        var amount: Int = Integer.parseInt(cent)
        if (type == "DEBIT") {
            amount *= -1
        }
        val operationCreated = Operation(account.id, day, null, amount,Time.now(), memo)    // créer une opération sans la mettre dans la base de donnée
        return operationCreated
    }
 }
