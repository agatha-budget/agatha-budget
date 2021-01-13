package open.tresorier.dao

import open.tresorier.dependenciesinjection.ITest
import open.tresorier.model.Person
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.koin.core.component.inject

class PersonDaoTest : ITest {

    val personDao by inject<IPersonDao>()

    @Test fun testInsertTwoIdenticalPerson() {
        val person1 = Person("anne", "ahahahahah", "anna@mail.eu")
        val storedPerson = personDao.insert(person1)
        assertEquals(person1.id, storedPerson?.id)
        val shouldBeNull = personDao.insert(person1)
        assertEquals(null, shouldBeNull)
    }
}
