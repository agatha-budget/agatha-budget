package open.tresorier.dependenciesinjection

import open.tresorier.mailing.*
import open.tresorier.mailing.adapter.*
import open.tresorier.dao.*
import open.tresorier.dao.jooq.pgsql.*
import org.koin.dsl.module

val adapter_module = module {
    single<IMailingPort> { AweberAdapter() }
    single<IPersonDao> { PgPersonDao((get())) }
    single<IBudgetDao> { PgBudgetDao((get())) }
    single<IAccountDao> { PgAccountDao((get())) }
    single<ICategoryDao> { PgCategoryDao((get())) }
    single<IMasterCategoryDao> { PgMasterCategoryDao((get())) }
    single<IOperationDao> { PgOperationDao((get())) }
    single<IAllocationDao> { PgAllocationDao(get()) }
}
