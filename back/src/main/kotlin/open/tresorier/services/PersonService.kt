package open.tresorier.services

import open.tresorier.dao.IPersonDao
import open.tresorier.exception.TresorierException
import open.tresorier.model.Budget
import open.tresorier.model.Person
import open.tresorier.model.PublicPerson
import open.tresorier.model.enum.ActionEnum
import open.tresorier.model.enum.ProfileEnum
import open.tresorier.utils.Time
import java.util.*


class PersonService(private val personDao: IPersonDao, 
        private val budgetService: BudgetService,
        private val mailingService: MailingService,
        private val bankingService: BankingService,
        private val userActivityService: UserActivityService) {

    fun createPerson(name: String, profile: ProfileEnum = ProfileEnum.PROFILE_USER, id: String? = null): Person {
        // keeping password and uuid email for now until the transition to keycloak is sure
        var person = Person(name, "hashedPassword", UUID.randomUUID().toString(), id=id)
        mailingService.addPersonToMailingList(person)
        person = personDao.insert(person)
        budgetService.create(person, Budget.DEFAULT_BUDGET_NAME, profile)
        return person
    }

    fun createPerson(name: String, password: String, email: String, profile: ProfileEnum = ProfileEnum.PROFILE_USER, id: String? = null): Person {
        // legacy function used for test
        var person = Person(name, password, email, id=id)
        mailingService.addPersonToMailingList(person)
        person = personDao.insert(person)
        budgetService.create(person, Budget.DEFAULT_BUDGET_NAME, profile)
        return person
    }

    fun getById(id: String) : Person {
        var person = personDao.getById(id)
        userActivityService.create(person, Time.now(), ActionEnum.ACTION_REQUEST)
        return person
    }

    fun getByBillingId(billingId: String) : Person {
        return personDao.getByBillingId(billingId)
    }

    fun updatePublicPerson(person: Person, newName: String?, newStyle: String?, newDyslexia: Boolean?) : PublicPerson {
        newName?.let{person.name = it}
        newStyle?.let{person.style = it}
        newDyslexia?.let{person.dyslexia = it}
        return this.update(person).toPublicPerson()
    }

    fun update(person: Person) : Person {
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
        update(person)
    }

}
