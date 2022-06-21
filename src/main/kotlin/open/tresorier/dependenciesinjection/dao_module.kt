package open.tresorier.dependenciesinjection

import open.tresorier.dao.*
import open.tresorier.dao.jooq.pgsql.*
import open.tresorier.services.*
import org.koin.dsl.module

val dao_module = module {
    single<IPersonDao> { PgPersonDao((get())) }
    single<IBudgetDao> { PgBudgetDao((get())) }
    single<IAccountDao> { PgAccountDao((get())) }
    single<ICategoryDao> { PgCategoryDao((get())) }
    single<IMasterCategoryDao> { PgMasterCategoryDao((get())) }
    single<IOperationDao> { PgOperationDao((get())) }
    single<IAllocationDao> { PgAllocationDao(get()) }
    single<IUserActivityDao> { PgUserActivityDao(get()) }
}
