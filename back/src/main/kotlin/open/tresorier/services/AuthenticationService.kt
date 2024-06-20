package open.tresorier.services

import de.mkammerer.argon2.Argon2
import de.mkammerer.argon2.Argon2Factory

object AuthenticationService {

    /**
     if login is valid and time limit ok : person
     else null
     */
    fun hashPassword(password : String ) : String {
        val argon2 : Argon2 = Argon2Factory.create()
        return argon2.hash(10, 65555, 1, password.toCharArray())

    }

    fun passwordMatch(hashedPassword: String, submittedPassword: String): Boolean {
        val argon2 : Argon2 = Argon2Factory.create();
        return argon2.verify(hashedPassword, submittedPassword.toCharArray() )
    }

}
