 package open.tresorier.services

import open.tresorier.dao.IOperationDao
import open.tresorier.model.*
import open.tresorier.utils.Time

import java.util.regex.*

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
                val old = operation.orderInDay
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
    fun separateOperationsOfOfxFile(person: Person, account: Account, ofxFile: String) : List<String> {
        authorizationService.cancelIfUserIsUnauthorized(person, account)
        var operationList = mutableListOf<String>()
        var startTag = ofxFile.indexOf("<STMTTRN>")
        var closeTag = ofxFile.indexOf("</STMTTRN>") + 10
        var lastTag = ofxFile.lastIndexOf("</STMTTRN>") + 10
        while (closeTag == lastTag) {
            operationList.add(ofxFile.substring(startTag, closeTag))
            startTag = closeTag
            closeTag = ofxFile.indexOf("</STMTTRN>", startTag) + 10
        }
        return operationList    // ce sont les opérations en entier, balises comprises
        //OperationService.createOperationsFromOFX(person, account, operationList)
    }
    fun passInEachOperation(person: Person, account: Account, operationList: List<String>) {
        operationList.forEach {
            this.createOperationFromOFX(person, account, it)
        }
    }
    fun createOperationFromOFX(person: Person, account: Account, operation: String): Operation {
        // récupérer les données grâce aux balises respectives (date, montant, mémo) et les formater correctement
        // <TRNTYP> balise de la nature de l'opération
        var startElement = 19
        var endElement = operation.indexOf("<", 11)
        val type = operation.substring(startElement, endElement)
        // <DTPOSTED> balise de la date
        startElement = endElement + 10
        endElement += 8
        val date = operation.substring(startElement, endElement)
        // <TRNAMT> balise du montant (il y a un + ou un - juste devant le montant en fonction débit ou crédit)
        startElement = endElement + 9
        endElement = operation.indexOf("<", startElement)
        val euro = operation.substring(startElement, endElement)
        // <MEMO> balise du mémo
        startElement = operation.indexOf("<MEMO>") + 6
        endElement = operation.indexOf("<", startElement)
        val memo = operation.substring(startElement, endElement) // déjà au bon format -> pas de tranformation à faire
        // formatage des données récupérées
        val day = Day.createFromComparable(Integer.parseInt(date)) // récupère la date au bon format // date est un string et je veux un int
        val cent: String = euro.replace(",", "")
        var amount: Int = cent.toInt() // récupère le montant au bon format
        if (type == "DEBIT") { // modifie la valeur du montant en fonction de la nature de la transaction
            amount *= -1
        }
        // créer les nouvelles opérations à partir de ces informations
        val operationCreated = this.create(person, account, day, null, amount, memo)
        return operationCreated
    }
 }
