package open.tresorier.dao.jooq.h2

import open.tresorier.dao.IPersonDao
import open.tresorier.exception.TresorierException
import open.tresorier.generated.jooq.test.public_.tables.daos.PersonDao
import open.tresorier.generated.jooq.test.public_.tables.records.PersonRecord
import open.tresorier.model.Person
import org.jooq.Configuration
import open.tresorier.generated.jooq.test.public_.tables.pojos.Person as JooqPerson

class H2PersonDao(val configuration: Configuration) : IPersonDao {

    private val generatedDao: PersonDao = PersonDao(configuration)

    override fun insert(person: Person): Person {
        val jooqPerson = this.toJooqPerson(person)
        try {

            this.generatedDao.insert(jooqPerson)
            return person

        } catch (e: Exception) {
            throw TresorierException("could not create new person : " + person.toString() + "TO" + jooqPerson.toString() + e.message , e)
        }
    }

    override fun update(person: Person) : Person {
        val jooqPerson = this.toJooqPerson(person)
        this.generatedDao.update(jooqPerson)
        return person
    }

    override fun getByEmail(email: String): Person {
        val jooqPerson = this.generatedDao.fetchOneByEmail(email)
        return toPerson(jooqPerson)?: throw TresorierException("no person found for the following email : $email")
    }

    override fun getByBillingId(billingId: String): Person {
        val jooqPerson = this.generatedDao.fetchOneByBillingId(billingId)
        return toPerson(jooqPerson)?: throw TresorierException("no person found for the following billing id : $billingId")
    }

    override fun getById(id: String): Person {
        val jooqPerson = this.generatedDao.fetchOneById(id)
        return toPerson(jooqPerson) ?: throw TresorierException("no person found for the following id : $id")
    }


    private fun toJooqPerson(person: Person): JooqPerson {
        return JooqPerson(
            person.id,
            person.email,
            person.name,
            person.hashedPassword,
            person.unlockingDate,
            person.loginAttemptCount,
            person.deleted,
            person.billingId,
            person.billingStatus,
            person.creationDate,
            person.style,
            person.dyslexia
        )
    }

    companion object {
        fun toPerson(jooqPerson: JooqPerson?): Person? {
            return if (jooqPerson == null)
                null
            else Person(
                jooqPerson.name,
                jooqPerson.password,
                jooqPerson.email,
                jooqPerson.billingId,
                jooqPerson.billingStatus,
                jooqPerson.style,
                jooqPerson.dyslexia,
                jooqPerson.unlockingdate,
                jooqPerson.loginattemptcount,
                jooqPerson.id,
                jooqPerson.deleted,
                jooqPerson.creationDate
            )
        }

        fun toPerson(recordPerson: PersonRecord): Person {
            return Person(
                recordPerson.name,
                recordPerson.password,
                recordPerson.email,
                recordPerson.billingId,
                recordPerson.billingStatus,
                recordPerson.style,
                recordPerson.dyslexia,
                recordPerson.unlockingdate,
                recordPerson.loginattemptcount,
                recordPerson.id,
                recordPerson.deleted,
                recordPerson.creationDate
            )
        }
    }
}
