package open.tresorier.mailing.adapter

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStream
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import open.tresorier.utils.Properties
import open.tresorier.utils.PropertiesEnum.*
import open.tresorier.mailing.IMailingPort

import open.tresorier.model.Person

class AweberAdapter() : IMailingPort {
    
    override fun add(person: Person) {
        System.out.println("Try to add to aweber !!")
        val properties = Properties()
        val body = 
        """{
            'email' : ${person.email},
            'update_existing' : 'true'
            'name' : ${person.name},
            'tags' : ['new_user']
        }""".trimIndent()

        val url = "https://api.aweber.com/1.0/accounts/${properties.get(AWEBER_ACCOUNT_ID)}/lists/${properties.get(AWEBER_LIST_ID)}/subscribers"

        // HEADER
        System.out.println(url)
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

        System.out.println("Message Sent")

		val responseCode = connection.responseCode
        System.out.println(connection.responseMessage)
		if (responseCode != HttpURLConnection.HTTP_OK) { 
            System.out.println("POST request not worked")
		} else {
            System.out.println("seems to have worked")

        }
	}
}