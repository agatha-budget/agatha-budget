package open.tresorier.dependenciesinjection

import open.tresorier.dao.*
import open.tresorier.dao.jooq.main.*
import open.tresorier.services.*
import org.koin.dsl.module

val dao_module = module {
    single<IPersonDao> { JooqPersonDao((get())) }
    single<IBudgetDao> { JooqBudgetDao((get())) }
    single<IAccountDao> { JooqAccountDao((get())) }
    single<ICategoryDao> { JooqCategoryDao((get())) }
    single<IMasterCategoryDao> { JooqMasterCategoryDao((get())) }
    single<IOperationDao> { JooqOperationDao((get())) }
    single<IAllocationDao> { JooqAllocationDao(get()) }
}
