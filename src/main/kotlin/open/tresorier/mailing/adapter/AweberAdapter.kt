package open.tresorier.mailing.adapter

import open.tresorier.exception.MailingException
import open.tresorier.mailing.IMailingPort
import open.tresorier.model.Person
import open.tresorier.utils.HTTPConnection
import open.tresorier.utils.Properties
import open.tresorier.utils.PropertiesEnum.*
import org.json.JSONArray
import org.json.JSONObject

class AweberAdapter() : IMailingPort {
    override fun add(person: Person) {
        refreshToken()
        val properties = Properties()

        val url = "https://api.aweber.com/1.0/accounts/${properties.get(AWEBER_ACCOUNT_ID)}/lists/${properties.get(AWEBER_LIST_ID)}/subscribers"

        val headerProperties = mapOf(
            "Content-Type" to "application/json",
            "Accept" to "application/json",
            "User-Agent" to "Agatha/1.0",
            "Authorization" to "Bearer ${properties.get(AWEBER_ACCESS_TOKEN)}"
        )

        val bodyProperties = mapOf(
            "email" to person.email,
            "update_existing" to true,
            "name" to person.name,
            "tags" to JSONArray().put("new_user")
        )

        val connection = HTTPConnection.sendRequest("POST", url, headerProperties, bodyProperties)

        if (connection.responseCode !in HTTPConnection.validResponseCodes) {
            throw MailingException("could not insert mail : ${person.email}, ${connection.errorStream.reader().use { it.readText() }}")
		}
	}

    private fun refreshToken() {
        val properties = Properties()

        val url = "https://auth.aweber.com/oauth2/token"

        val headerProperties = mapOf(
            "Content-Type" to "application/json",
            "Accept" to "application/json",
            "User-Agent" to "Agatha/1.0"
        )

        val bodyProperties = mapOf(
            "client_id" to properties.get(AWEBER_CLIENT_ID),
            "grant_type" to "refresh_token",
            "refresh_token" to properties.get(AWEBER_REFRESH_TOKEN)
        )

        val connection = HTTPConnection.sendRequest("POST", url, headerProperties, bodyProperties)

        if (connection.responseCode !in HTTPConnection.validResponseCodes) {
            throw MailingException("could not refresh token, ${connection.errorStream.reader().use { it.readText() }}")
		}

        val response = JSONObject(connection.inputStream.reader().use { it.readText() })
        Properties.set(AWEBER_ACCESS_TOKEN, response.get("access_token").toString())
        Properties.set(AWEBER_REFRESH_TOKEN,response.get("refresh_token").toString())

    }
}