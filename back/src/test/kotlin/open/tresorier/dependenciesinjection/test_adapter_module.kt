package open.tresorier.dependenciesinjection

import open.tresorier.banking.IBankingPort
import open.tresorier.banking.adapter.MockBankingAdapter
import open.tresorier.dao.*
import open.tresorier.dao.jooq.pgsql.*
import open.tresorier.mailing.IMailingPort
import open.tresorier.mailing.adapter.MockMailingAdapter
import org.koin.dsl.module


val test_adapter_module = module {
    single<IMailingPort> { MockMailingAdapter() }
    single<IBankingPort> { MockBankingAdapter() }
    single<IPersonDao> { PgPersonDao((get())) }
    single<IBudgetDao> { PgBudgetDao((get())) }
    single<IAccountDao> { PgAccountDao((get())) }
    single<ICategoryDao> { PgCategoryDao((get())) }
    single<IMasterCategoryDao> { PgMasterCategoryDao((get())) }
    single<IOperationDao> { PgOperationDao((get())) }
    single<IAllocationDao> { PgAllocationDao(get()) }
    single<IUserActivityDao> { PgUserActivityDao(get()) }
    single<IBankAgreementDao> { PgBankAgreementDao(get()) }
    single<IBankAccountDao> { PgBankAccountDao(get()) }
}