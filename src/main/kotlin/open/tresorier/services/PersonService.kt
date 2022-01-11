package open.tresorier.services

import open.tresorier.dao.IPersonDao
import open.tresorier.model.Budget
import open.tresorier.model.Person
import open.tresorier.utils.Time
import open.tresorier.exception.TresorierException
import open.tresorier.model.enum.ProfileEnum


class PersonService(private val personDao: IPersonDao, private val budgetService: BudgetService) {

    fun createPerson(name: String, password: String, email: String, profile: ProfileEnum): Person {
        val hashedPassword = AuthenticationService.hashPassword(password)
        var person = Person(name, hashedPassword, email)
        person = personDao.insert(person)
        budgetService.create(person, Budget.DEFAULT_BUDGET_NAME, profile)
        return person
    }

    /* return either the person or null if the login failed
     ex : email doesn't exist
     ex : invalid password
     ex : account is still locked
     */
    fun login(email: String, password: String): Person? {
        var person : Person? = personDao.getByEmail(email)
        person?.let {
            val correctPassword = AuthenticationService.passwordMatch(it.hashedPassword, password)
            val unlockedAccount = (it.unlockingDate <= Time.now())
            updateLoginAttempt(it, correctPassword)
            if (!(correctPassword && unlockedAccount)){
                person = null
            }
        }
        return person
    }

    private fun updateLoginAttempt(person : Person, correctPassword: Boolean){
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

    fun getByBillingId(billingId: String) : Person {
        return personDao.getByBillingId(billingId)
    }

    fun update(person: Person) {
        return personDao.update(person)
    }

    fun getUnlockingDateForEmail(email : String) : Long? {
        var potentialPerson: Person?
        try {
            potentialPerson = personDao.getByEmail(email)
        } catch (e: TresorierException) {
            potentialPerson = null
        }
        return potentialPerson?.unlockingDate
    }

    fun delete(person: Person) {
        person.deleted = true
        return update(person)
    }

}
