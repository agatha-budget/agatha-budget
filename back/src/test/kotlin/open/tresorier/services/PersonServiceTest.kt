package open.tresorier.services

import open.tresorier.dependenciesinjection.ITest
import org.koin.core.component.inject

class PersonServiceTest : ITest {

    private val personService by inject<PersonService>()
}
