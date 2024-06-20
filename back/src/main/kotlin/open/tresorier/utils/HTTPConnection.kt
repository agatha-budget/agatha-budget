package open.tresorier.utils

import org.json.JSONObject
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

object HTTPConnection {

    fun sendRequest(type: String, url: String, headerProperties: Map<String, String>, bodyProperties: Map<String, Any>? = null) : HttpURLConnection {
        // HEADER
        val connection = URL(url).openConnection() as HttpURLConnection
		connection.requestMethod = type
        headerProperties.forEach{ k, v -> connection.setRequestProperty(k, v)}
        connection.doInput = true;
		connection.doOutput = true;

		// BODY (for post only)
        bodyProperties?.let {
            val outputStreamWriter = OutputStreamWriter(connection.outputStream)
            val jsonBody = JSONObject()
            it.forEach{ k, v -> jsonBody.put(k, v)}
            outputStreamWriter.write(jsonBody.toString())
            outputStreamWriter.flush()
        }
        return connection
    }

    val validResponseCodes = arrayOf(HttpURLConnection.HTTP_OK, HttpURLConnection.HTTP_CREATED)

}
