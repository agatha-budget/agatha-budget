package open.tresorier.mailing.adapter

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL
import open.tresorier.mailing.IMailingPort

import open.tresorier.model.Person

class MockAdapter() : IMailingPort {
    
    override fun add(person: Person) {
        System.out.println("Let's pretend we did it");
	}
}