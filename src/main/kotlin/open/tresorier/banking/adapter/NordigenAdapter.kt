package open.tresorier.banking.adapter

import open.tresorier.banking.IBankingPort
import open.tresorier.dao.IBankAccountDao
import open.tresorier.dao.IBankAgreementDao
import open.tresorier.exception.BankingException
import open.tresorier.model.Account
import open.tresorier.model.Budget
import open.tresorier.model.Day
import open.tresorier.model.Operation
import open.tresorier.model.banking.Bank
import open.tresorier.model.banking.BankAccount
import open.tresorier.model.banking.BankAgreement
import open.tresorier.utils.HTTPConnection
import open.tresorier.utils.Properties
import open.tresorier.utils.PropertiesEnum.*
import open.tresorier.utils.Time
import open.tresorier.exception.TresorierException
import org.json.JSONArray
import org.json.JSONObject

class NordigenAdapter(private val bankAgreementDao: IBankAgreementDao, private val bankAccountDao : IBankAccountDao) : IBankingPort {

    override fun getLinkForUserAgreement(budget: Budget, bankId: String) : String {
        val properties = Properties()
        val successUrl = properties.get(NORDIGEN_SUCCESS_URL)
        val url = "https://ob.nordigen.com/api/v2/requisitions/"
        val bankAgreement = BankAgreement(budget.id, bankId)

        val headerProperties = mapOf(
            "Content-Type" to "application/json",
            "Accept" to "application/json",
            "User-Agent" to "Agatha/1.0",
            "Authorization" to "Bearer ${this.getToken()}"
        )

        val bodyProperties = mapOf(
            "redirect" to "${successUrl}?agreementId=${bankAgreement.id}",
            "institution_id" to bankId,
            "user_language" to "FR"
        )

        val connection = HTTPConnection.sendRequest("POST", url, headerProperties, bodyProperties)

        if (connection.responseCode !in HTTPConnection.validResponseCodes) {
            throw BankingException("could not get AgreementLink : ${connection.errorStream.reader().use { it.readText() }}")
		}

        val response = JSONObject(connection.inputStream.reader().use { it.readText() })
        // create Requisition 
        bankAgreement.nordigenRequisitionId = response.get("id").toString()
        this.bankAgreementDao.insert(bankAgreement)
        return response.get("link").toString()
    }

    override fun getBankAccountList(agreement: BankAgreement) : List<BankAccount> {
        val url = "https://ob.nordigen.com/api/v2/requisitions/${agreement.nordigenRequisitionId}/"

        val headerProperties = mapOf(
            "Content-Type" to "application/json",
            "Accept" to "application/json",
            "User-Agent" to "Agatha/1.0",
            "Authorization" to "Bearer ${this.getToken()}"
        )

        val connection = HTTPConnection.sendRequest("GET", url, headerProperties)

        if (connection.responseCode !in HTTPConnection.validResponseCodes) {
            throw BankingException("could not get Requisition for ${agreement.id} : ${connection.errorStream.reader().use { it.readText() }}")
		}
        val response = JSONObject(connection.inputStream.reader().use { it.readText() })
        val accounts = response.getJSONArray("accounts")
     
        var accountList = listOf<BankAccount>()
        for (i in 1..accounts.length()) {
            val bankId = accounts.getString(i-1);
            val bankAccount = BankAccount(this.getAccountName(bankId), agreement.id, bankId)
            accountList = accountList + bankAccount 
        }
        return accountList
    }

    private fun getAccountName(id: String) : String {
        val url = "https://ob.nordigen.com/api/v2/accounts/${id}/details/"

        val headerProperties = mapOf(
            "Content-Type" to "application/json",
            "Accept" to "application/json",
            "User-Agent" to "Agatha/1.0",
            "Authorization" to "Bearer ${this.getToken()}"
        )

        val connection = HTTPConnection.sendRequest("GET", url, headerProperties)

        if (connection.responseCode !in HTTPConnection.validResponseCodes) {
            throw BankingException("could not get account details for ${id} : ${connection.errorStream.reader().use { it.readText() }}")
		}
        val response = JSONObject(connection.inputStream.reader().use { it.readText() })
        return response.getJSONObject("account").optString("name", "unnamed account")
    }

    override fun getOperations(account: Account, _from: Long?) : List<Operation> {
        var operationList = listOf<Operation>()

        if (account.bankAccountId == null) {
            return operationList
        } else {
            var bankAccountId : String = ""
            account.bankAccountId?.let {bankAccountId = it} 
            val bankAccount = this.bankAccountDao.getById(bankAccountId)

            // date format is 2022-07-07
            val from = if (_from != null) Time.getDateStringFromTimestamp(_from) else "2022-01-01"
            val url = "https://ob.nordigen.com/api/v2/accounts/${bankAccount.bankId}/transactions/?date_from=${from}"

            val headerProperties = mapOf(
                "Content-Type" to "application/json",
                "Accept" to "application/json",
                "User-Agent" to "Agatha/1.0",
                "Authorization" to "Bearer ${this.getToken()}"
            )

            val connection = HTTPConnection.sendRequest("GET", url, headerProperties)

            if (connection.responseCode !in HTTPConnection.validResponseCodes) {
                throw BankingException("could not get account transaction for ${account.id} : ${connection.errorStream.reader().use { it.readText() }}")
            }
            val response = JSONObject(connection.inputStream.reader().use { it.readText() })
            // add new transaction for account

            var nordigenOperations = response.getJSONObject("transactions").getJSONArray("booked")
            operationList = createOperations(account, nordigenOperations, operationList, false)

            nordigenOperations = response.getJSONObject("transactions").getJSONArray("pending")
            operationList = createOperations(account, nordigenOperations, operationList, true)

            return operationList
        }
    }

    private fun createOperations(account: Account, nordigenOperations: JSONArray, operationList: List<Operation>, pending: Boolean) : List<Operation> {
        var newOperationList = operationList
        for (i in 1..nordigenOperations.length()) {
            val nordigenOperation = nordigenOperations.getJSONObject(i-1);
            val operation = this.createOperation(account, pending, nordigenOperation)
            newOperationList = newOperationList + operation 
        }
        return newOperationList
    }

    private fun createOperation(account: Account, pending: Boolean, nordigenOperation: JSONObject) : Operation {
        val date : String? = nordigenOperation.optString("bookingDate")
        val day : Day = date?.let {Day.createFromComparable(it.replace("-","").toInt())} ?: Day.today()
        val bankAmount = nordigenOperation.getJSONObject("transactionAmount").getFloat("amount")
        val centsAmount = (bankAmount * 100).toInt()
        TresorierException("BANK ----- from $bankAmount to $centsAmount")
        val amount = centsAmount
        val orderInDay = Time.now()
        val memo = nordigenOperation.optJSONArray("remittanceInformationUnstructuredArray")?.getString(0)
        val importIdentifier = day.toString() + "__" + amount + "__" + nordigenOperation.optString("entryReference") + "__" + account.id
        return Operation(
            account.id, day, null, amount, orderInDay, 
            memo, pending, false, null, importIdentifier
        )
    }

    override fun getAvailableBanks() : List<Bank> {

        val url = "https://ob.nordigen.com/api/v2/institutions/?country=fr"

        val headerProperties = mapOf(
            "Content-Type" to "application/json",
            "Accept" to "application/json",
            "User-Agent" to "Agatha/1.0",
            "Authorization" to "Bearer ${this.getToken()}"
        )

        val connection = HTTPConnection.sendRequest("GET", url, headerProperties)

        if (connection.responseCode !in HTTPConnection.validResponseCodes) {
            throw BankingException("could not get the institutions")
		}
        val nordigenBanks = JSONArray(connection.inputStream.reader().use { it.readText() })
        // add new transaction for account

        var bankList = listOf<Bank>()

        for (i in 1..nordigenBanks.length()) {
            val nordigenBank = nordigenBanks.getJSONObject(i-1);
            val bank = this.createBank(nordigenBank)
            bankList += bank
        }
        return bankList
    }

    override fun getBankAccountBalance(id: String) : Int? {
        try {
            val url = "https://ob.nordigen.com/api/v2/accounts/${id}/balances/"

            val headerProperties = mapOf(
                "Content-Type" to "application/json",
                "Accept" to "application/json",
                "User-Agent" to "Agatha/1.0",
                "Authorization" to "Bearer ${this.getToken()}"
            )

            val connection = HTTPConnection.sendRequest("GET", url, headerProperties)

            if (connection.responseCode !in HTTPConnection.validResponseCodes) {
                throw BankingException("could not get account details for ${id} : ${connection.errorStream.reader().use { it.readText() }}")
            }
            val response = JSONObject(connection.inputStream.reader().use { it.readText() })
            val stringAmount = (response.getJSONArray("balances")[0] as JSONObject).getJSONObject("balanceAmount").optString("amount", "0")
            return stringAmount.toInt()
        } catch (e: Exception) {
            TresorierException("could not find balance for %id", e)
            return null
        }
    }

    private fun createBank(nordigenBank: JSONObject) : Bank {
        val id : String = nordigenBank.getString("id")
        val name : String = nordigenBank.getString("name")
        val logo : String = nordigenBank.getString("logo")
        return Bank(id, name, logo)
    }

    fun getToken() : String {
        val properties = Properties()

        val url = "https://ob.nordigen.com/api/v2/token/new/"

        val headerProperties = mapOf(
            "Content-Type" to "application/json",
            "Accept" to "application/json",
            "User-Agent" to "Agatha/1.0",
        )

        val bodyProperties = mapOf(
            "secret_id" to properties.get(NORDIGEN_SECRET_ID),
            "secret_key" to properties.get(NORDIGEN_SECRET_KEY)
        )

        val connection = HTTPConnection.sendRequest("POST", url, headerProperties, bodyProperties)

        if (connection.responseCode !in HTTPConnection.validResponseCodes) {
            throw BankingException("could not get Token : ${connection.errorStream.reader().use { it.readText() }}")
		}

        val response = JSONObject(connection.inputStream.reader().use { it.readText() })
        return response.get("access").toString()
    }
}