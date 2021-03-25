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

    single<AuthorizationService> { (get()) }
    single<PersonService> { PersonService(get()) }
    single<BudgetService> { BudgetService(get(), get()) }
    single<AccountService> { AccountService(get(), get(), get()) }
    single<CategoryService> { CategoryService(get(), get()) }


    // init default mock behavior
    val mockFactory = MockFactory()
    single<PersonService>(named("PersonServiceInvalidDao")) { PersonService(mockFactory.mockInvalidPersonDao) }
}

class MockFactory {
    val mockInvalidPersonDao: IPersonDao = mockk<IPersonDao>()

    init {
        every { mockInvalidPersonDao.insert(any()) } throws TresorierException("unit testing")
    }
}


