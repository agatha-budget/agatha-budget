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
}
