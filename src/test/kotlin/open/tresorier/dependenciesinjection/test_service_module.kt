package open.tresorier.dependenciesinjection


import io.mockk.every
import io.mockk.mockk
import open.tresorier.dao.*
import open.tresorier.exception.TresorierException
import open.tresorier.services.*
import org.koin.core.qualifier.named
import org.koin.dsl.module


val test_service_module = module {
    // init default mock behavior
    val mockFactory = MockFactory()
    single<PersonService>(named("PersonServiceInvalidDao")) { PersonService(mockFactory.mockInvalidPersonDao, get(), get())  }
}

class MockFactory {
    val mockInvalidPersonDao: IPersonDao = mockk<IPersonDao>()

    init {
        every { mockInvalidPersonDao.insert(any()) } throws TresorierException("unit testing")
    }
}


