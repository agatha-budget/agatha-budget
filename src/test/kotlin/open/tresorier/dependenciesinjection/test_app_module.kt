package open.tresorier.dependenciesinjection


import io.mockk.every
import io.mockk.mockk
import open.tresorier.dao.*
import open.tresorier.dao.jooq.test.*
import open.tresorier.exception.TresorierException
import open.tresorier.services.*
import org.koin.core.qualifier.named
import org.koin.dsl.module


val test_app_module = module {
    single<IPersonDao> { JooqTestPersonDao(get()) }
    single<IBudgetDao> { JooqTestBudgetDao(get()) }
    single<IAccountDao> { JooqTestAccountDao(get()) }
    single<ICategoryDao> { JooqTestCategoryDao(get()) }
    single<IOperationDao> { JooqTestOperationDao(get()) }
    single<IAllocationDao> { JooqTestAllocationDao(get()) }

    single { AuthorizationService(get(), get(), get(), get()) }
    single { PersonService(get(), get()) }
    single { BudgetService(get(), get()) }
    single { AccountService(get(), get(), get()) }
    single { CategoryService(get(), get()) }
    single { OperationService(get(), get()) }
    single { AllocationService(get(), get()) }

    // init default mock behavior
    val mockFactory = MockFactory()
    single<PersonService>(named("PersonServiceInvalidDao")) { PersonService(mockFactory.mockInvalidPersonDao, get()) }
}

class MockFactory {
    val mockInvalidPersonDao: IPersonDao = mockk<IPersonDao>()

    init {
        every { mockInvalidPersonDao.insert(any()) } throws TresorierException("unit testing")
    }
}


