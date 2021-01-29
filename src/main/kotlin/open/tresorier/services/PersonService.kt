package open.tresorier.services

import open.tresorier.dao.IPersonDao
import open.tresorier.model.Person
import open.tresorier.utils.Time

class PersonService(val personDao: IPersonDao) {

    /* return either the created person or null if the creation failed
     ex : email already used
     */
    fun createPerson(name: String, password: String, email: String): Person {
        val hashedPassword = AuthenticationService.hashPassword(password)
        val person = Person(name, hashedPassword, email)
        return personDao.insert(person)
    }

    /* return either the authentication token or null if the login failed
     ex : email doesn't exist
     ex : invalid password
     ex : account is still locked
     */
    fun login(email: String, password: String): Person? {
        val person = personDao.getByEmail(email)
        if (person == null) {
            return null
        } else {
            val correctPassword = AuthenticationService.passwordMatch(person.hashedPassword, password)
            val unlockedAccount = (person.unlockingDate <= Time.now())
            updateLoginAttempt(person, correctPassword)

            return if (correctPassword && unlockedAccount) person else null
        }
    }

    fun updateLoginAttempt(person : Person, correctPassword: Boolean){
        if (!correctPassword){
            person.loginAttemptCount += 1
            person.unlockingDate = addIncrementalDelay(person.unlockingDate, person.loginAttemptCount)
        } else {
            person.loginAttemptCount = 0
        }
        personDao.update(person)
    }

    fun addIncrementalDelay(currentUnlockingDate : Long, loginAttemptCount: Int) : Long {
        if (loginAttemptCount < 3) {return Time.now()}

        val baseUnlockingDate = maxOf(currentUnlockingDate, Time.now())
        val incrementedDate = baseUnlockingDate + ((loginAttemptCount-2) * Time.getDuration(minutes = 5))

        return incrementedDate
    }

    fun getById(id: String) : Person {
        return personDao.getById(id)
    }

    fun update(person: Person) {
        return personDao.update(person)
    }

    fun getUnlockingDateForEmail(email : String) : Long? {
        val potentialPerson = personDao.getByEmail(email)
        return potentialPerson?.unlockingDate
    }

}
