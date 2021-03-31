package open.tresorier.dependenciesinjection

import open.tresorier.dao.*
import open.tresorier.dao.jooq.main.*
import org.koin.dsl.module

val test_integration_dao_module = module {
    single<IPersonDao> { JooqPersonDao(get()) }
    single<IBudgetDao> { JooqBudgetDao(get()) }
    single<IAccountDao> { JooqAccountDao(get()) }
    single<IOperationDao> { JooqOperationDao(get()) }
    single<IAllocationDao> { JooqAllocationDao(get()) }
}