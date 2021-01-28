package open.tresorier.dao

import open.tresorier.dependenciesinjection.ITest
import open.tresorier.exception.TresorierException
import open.tresorier.model.Person
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.koin.core.component.inject

class PersonDaoTest : ITest {

    val personDao by inject<IPersonDao>()

    @Test fun testInsertTwoIdenticalPerson() {
        val person1 = Person("Camille", "mimimi", "camille@mail.eu")
        val storedPerson = personDao.insert(person1)
        assertEquals(person1.id, storedPerson.id)
        val exception = Assertions.assertThrows(TresorierException::class.java) {
            personDao.insert(person1)
        }
        assertEquals("could not create new person", exception.message)
    }
}
