package open.tresorier.mailing
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import open.tresorier.model.Person

class AweberAdapter() : IMailingPort {
    
    fun create(person: Person) : Person {
        const body = [
            'email' => Person.email,
            'update_existing' => 'true'
            'name' => Person.name,
            'tags' => [
                'new_user',
            ]
        ];
        const url = "https://api.aweber.com/1.0/accounts/{$accountId}/lists/{$listId}/subscribers";

        const connection = (HttpURLConnection) URL(url).openConnection();
		connection.setRequestMethod("POST");
		connection.setRequestProperty("Content-Type", 'application/json');
		connection.setRequestProperty("Accept", 'application/json');
		connection.setRequestProperty("User-Agent", 'Agatha/1.0');

		// For POST only - START
		connection.setDoOutput(true);
		const outStream = connection.getOutputStream();
        const writer = OutputStreamWriter(outStream, "UTF-8");
        writer.write(body);
        writer.flush();
        writer.close();
		outStream.close();
		// For POST only - END

		const responseCode = connection.getResponseCode();
		if (responseCode != HttpURLConnection.HTTP_OK) { //success
            System.out.println("POST request not worked");
		}
	}

    fun getAuthentication(){

    }
}