package open.tresorier.dao.jooq.pgsql

import open.tresorier.dao.IPersonDao
import open.tresorier.exception.TresorierException
import open.tresorier.generated.jooq.main.tables.daos.PersonDao
import open.tresorier.generated.jooq.main.tables.records.PersonRecord
import open.tresorier.model.Person
import org.jooq.Configuration
import open.tresorier.generated.jooq.main.tables.pojos.Person as JooqPerson

class PgPersonDao(val configuration: Configuration) : IPersonDao {

    private val generatedDao: PersonDao = PersonDao(configuration)

    override fun insert(person: Person): Person {
        val jooqPerson = this.toJooqPerson(person)
        try {

            this.generatedDao.insert(jooqPerson)
            return person

        } catch (e: Exception) {
            throw TresorierException("could not create new person", e)
        }
    }

    override fun update(person: Person) {
        val jooqPerson = this.toJooqPerson(person)
        this.generatedDao.update(jooqPerson)
    }

    override fun getByEmail(email: String): Person? {
        val jooqPerson = this.generatedDao.fetchOneByEmail(email)
        return toPerson(jooqPerson)
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
            person.billingStatus
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
                jooqPerson.unlockingdate,
                jooqPerson.loginattemptcount,
                jooqPerson.id,
                jooqPerson.deleted
            )
        }

        fun toPerson(recordPerson: PersonRecord): Person {
            return Person(
                recordPerson.name,
                recordPerson.password,
                recordPerson.email,
                recordPerson.billingId,
                recordPerson.billingStatus,
                recordPerson.unlockingdate,
                recordPerson.loginattemptcount,
                recordPerson.id,
                recordPerson.deleted
            )
        }
    }
}
