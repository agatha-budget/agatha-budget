package open.tresorier.dao.jooq

import open.tresorier.dao.IPersonDao
import open.tresorier.exception.TresorierException
import open.tresorier.generated.jooq.tables.daos.PersonDao
import open.tresorier.model.Person
import open.tresorier.generated.jooq.tables.pojos.Person as JooqPerson

class JooqPersonDao(val generatedDao: PersonDao) : IPersonDao {

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

    override fun delete(person: Person) {
        val jooqPerson = this.toJooqPerson(person)
        this.generatedDao.delete(jooqPerson)
    }

    override fun getByEmail(email: String): Person? {
        val jooqPerson = this.generatedDao.fetchOneByEmail(email)
        return this.toPerson(jooqPerson)
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
            person.deleted
        )
    }

    private fun toPerson(jooqPerson: JooqPerson?): Person? {
        return if (jooqPerson == null)
            null
        else Person(
            jooqPerson.name,
            jooqPerson.password,
            jooqPerson.email,
            jooqPerson.unlockingdate,
            jooqPerson.loginattemptcount,
            jooqPerson.id,
            jooqPerson.deleted
        )
    }
}
