package open.tresorier.mailing
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import open.tresorier.model.Person

class AweberAdapter() : IMailingPort {
    
    fun add(person: Person) : Person {
        System.out.println("Let's pretend we did it");
	}
}