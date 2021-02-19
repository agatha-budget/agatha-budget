package open.tresorier.dependenciesinjection


import io.mockk.every
import io.mockk.mockk
import open.tresorier.dao.IAccountDao
import open.tresorier.dao.IBudgetDao
import open.tresorier.dao.IPersonDao
import open.tresorier.dao.jooq.main.JooqAccountDao
import open.tresorier.dao.jooq.main.JooqBudgetDao
import open.tresorier.dao.jooq.main.JooqPersonDao
import open.tresorier.exception.TresorierException
import open.tresorier.services.BudgetService
import open.tresorier.services.PersonService
import org.koin.core.qualifier.named
import org.koin.dsl.module


val test_integration_app_module = module {
    single<IPersonDao> { JooqPersonDao(get()) }
    single<IBudgetDao> { JooqBudgetDao(get()) }
    single<IAccountDao> { JooqAccountDao(get()) }
    single<PersonService>{ PersonService(get())}
    single<BudgetService> { BudgetService(get()) }

    // init default mock behavior
    val mockFactory = MockFactory()
    single<PersonService>(named("PersonServiceInvalidDao")) { PersonService(mockFactory.mockInvalidPersonDao) }
}

class MockFactory {
    val mockInvalidPersonDao : IPersonDao = mockk<IPersonDao>()

    init {
        every { mockInvalidPersonDao.insert(any()) } throws TresorierException("integration testing")
    }
}