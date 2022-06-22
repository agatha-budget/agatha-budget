package open.tresorier.mailing.adapter

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStream
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import org.json.JSONObject
import org.json.JSONArray
import open.tresorier.utils.Properties
import open.tresorier.utils.PropertiesEnum.*
import open.tresorier.mailing.IMailingPort

import open.tresorier.model.Person

class AweberAdapter() : IMailingPort {
    
    override fun add(person: Person) {
        val properties = Properties()
        val jsonObject = JSONObject()
        jsonObject.put("email", person.email)
        jsonObject.put("update_existing", true)
        jsonObject.put("name", person.name)        
        jsonObject.put("tags", JSONArray().put("new_user"))
        val body = jsonObject.toString()

        val url = "https://api.aweber.com/1.0/accounts/${properties.get(AWEBER_ACCOUNT_ID)}/lists/${properties.get(AWEBER_LIST_ID)}/subscribers"

        // HEADER
        val connection = URL(url).openConnection() as HttpURLConnection
		connection.requestMethod = "POST"
		connection.setRequestProperty("Content-Type", "application/json")
		connection.setRequestProperty("Accept", "application/json")
		connection.setRequestProperty("User-Agent", "Agatha/1.0")
        connection.setRequestProperty("Authorization",
         "Bearer ${properties.get(AWEBER_ACCESS_TOKEN)}"
        )
        connection.doInput = true;
		connection.doOutput = true;

		// BODY
        val outputStreamWriter = OutputStreamWriter(connection.outputStream)
        outputStreamWriter.write(body)
		outputStreamWriter.flush()

        if (connection.responseCode != HttpURLConnection.HTTP_OK) { 
            throw MailingException("could not insert mail : ${person.email}, ${connection.errorStream.reader().use { it.readText() }}")
		}
	}
}