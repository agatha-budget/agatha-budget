package open.tresorier.dao


import open.tresorier.dependenciesinjection.ITest
import open.tresorier.exception.TresorierException
import open.tresorier.model.Person
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.koin.core.component.inject

open class PersonDaoTest : ITest {

    val personDao by inject<IPersonDao>()

    @Test fun testInsertTwoIdenticalPerson() {
        val christine = Person("Christine de Pisan", "CiteDesDames", "no@adress.yet")
        val storedPerson = personDao.insert(christine)
        assertEquals(christine.id, storedPerson.id)
        val exception = Assertions.assertThrows(TresorierException::class.java) {
            personDao.insert(christine)
        }
        assertEquals("could not create new person", exception.message)
    }

    @Test fun testGetAllNonDeleted() {
        val sizeBefore = personDao.findAll().size

        val louise = Person("Louise Weiss", "Vote!1944", "louise@suffragette.fr")
        val leodile = Person("Léodile Champseix", "Egalité!", "leodile@champseix.eu")
        val julie = Person("Julie Daubié", "Bacheliere!1861", "julie@diplomee.com", deleted = true)

        arrayOf(louise, leodile, julie).forEach {
            personDao.insert(it)
        }

        assertEquals(2, personDao.findAll().size - sizeBefore)
    }
}
