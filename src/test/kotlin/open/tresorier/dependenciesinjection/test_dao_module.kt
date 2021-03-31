package open.tresorier.dependenciesinjection


import io.mockk.every
import io.mockk.mockk
import open.tresorier.dao.*
import open.tresorier.dao.jooq.test.*
import open.tresorier.exception.TresorierException
import open.tresorier.services.*
import org.koin.core.qualifier.named
import org.koin.dsl.module


val test_dao_module = module {
    single<IPersonDao> { JooqTestPersonDao(get()) }
    single<IBudgetDao> { JooqTestBudgetDao(get()) }
    single<IAccountDao> { JooqTestAccountDao(get()) }
    single<ICategoryDao> { JooqTestCategoryDao(get()) }
    single<IMasterCategoryDao> { JooqTestMasterCategoryDao(get()) }
    single<IOperationDao> { JooqTestOperationDao(get()) }
    single<IAllocationDao> { JooqTestAllocationDao(get()) }
}

