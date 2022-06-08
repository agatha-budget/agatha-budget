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
        val properties = Properties()
        val bodyAsJsonString = 
        """{
            'email' : ${person.email},
            'update_existing' : 'true'
            'name' : ${person.name},
            'tags' : ['new_user']
        }"""
        val url = 
        """
            https://api.aweber.com/1.0/accounts/" 
            ${properties.get(AWEBER_ACCOUNT_ID)}
            /lists/"
            ${properties.get(AWEBER_LIST_ID)}
            /subscribers
        """

        // HEADER
        val connection = URL(url).openConnection() as HttpURLConnection
		connection.setRequestMethod("POST")
		connection.setRequestProperty("Content-Type", "application/json")
		connection.setRequestProperty("Accept", "application/json")
		connection.setRequestProperty("User-Agent", "Agatha/1.0")
        connection.setRequestProperty("Authorization",
         "Bearer ${properties.get(AWEBER_ACCESS_TOKEN)}"
        )

		// BODY
		connection.setDoOutput(true);
		val outStream = connection.getOutputStream()
        val body = bodyAsJsonString.toByteArray(Charsets.UTF_8)
        outStream.write(body, 0, body.size)
		outStream.close()

		val responseCode = connection.getResponseCode()
		if (responseCode != HttpURLConnection.HTTP_OK) { 
            System.out.println("POST request not worked")
		}
	}
}