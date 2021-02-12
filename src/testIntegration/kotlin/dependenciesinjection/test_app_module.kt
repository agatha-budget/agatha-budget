package open.tresorier.dependenciesinjection


import io.mockk.every
import io.mockk.mockk
import open.tresorier.dao.IBudgetDao
import open.tresorier.dao.IPersonDao
import open.tresorier.dao.jooq.JooqBudgetDao
import open.tresorier.dao.jooq.JooqPersonDao
import open.tresorier.exception.TresorierException
import open.tresorier.services.BudgetService
import open.tresorier.services.PersonService
import org.koin.core.qualifier.named
import org.koin.dsl.module
import open.tresorier.generated.jooq.tables.daos.BudgetDao as GeneratedBudgetDao
import open.tresorier.generated.jooq.tables.daos.PersonDao as GeneratedPersonDao


val test_app_module = module {
    single<IPersonDao> { JooqPersonDao(GeneratedPersonDao(get())) }
    single<IBudgetDao> { JooqBudgetDao(GeneratedBudgetDao(get())) }

    // init default mock behavior
    val mockFactory = MockFactory()

    single<PersonService>{ PersonService(mockFactory.mockPersonDao)}
    single<PersonService>(named("PersonServiceInvalidDao")) { PersonService(mockFactory.mockInvalidPersonDao) }
    single<BudgetService> { BudgetService(mockk<IBudgetDao>()) }
}

class MockFactory {
    val mockPersonDao: IPersonDao = mockk<IPersonDao>()
    val mockInvalidPersonDao : IPersonDao = mockk<IPersonDao>()

    init {
        every { mockPersonDao.insert(any()) } returnsArgument(0)
        every { mockInvalidPersonDao.insert(any()) } throws TresorierException("unit testing")
    }
}


