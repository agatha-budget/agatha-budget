package open.tresorier.dependenciesinjection


import io.mockk.every
import io.mockk.mockk
import open.tresorier.dao.IAccountDao
import open.tresorier.dao.IBudgetDao
import open.tresorier.dao.ICategoryDao
import open.tresorier.dao.IPersonDao
import open.tresorier.dao.jooq.test.JooqTestAccountDao
import open.tresorier.dao.jooq.test.JooqTestBudgetDao
import open.tresorier.dao.jooq.test.JooqTestCategoryDao
import open.tresorier.dao.jooq.test.JooqTestPersonDao
import open.tresorier.exception.TresorierException
import open.tresorier.services.AccountService
import open.tresorier.services.BudgetService
import open.tresorier.services.CategoryService
import open.tresorier.services.PersonService
import org.koin.core.qualifier.named
import org.koin.dsl.module


val test_app_module = module {
    single<IPersonDao> { JooqTestPersonDao(get()) }
    single<IBudgetDao> { JooqTestBudgetDao(get()) }
    single<IAccountDao> { JooqTestAccountDao(get()) }
    single<ICategoryDao> { JooqTestCategoryDao(get()) }
    single<PersonService> { PersonService(get()) }
    single<BudgetService> { BudgetService(get()) }
    single<AccountService> { AccountService(get()) }
    single<CategoryService> { CategoryService(get()) }


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


