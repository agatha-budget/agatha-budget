package open.tresorier.dependenciesinjection

import open.tresorier.banking.IBankingPort
import open.tresorier.banking.adapter.NordigenAdapter
import open.tresorier.dao.*
import open.tresorier.dao.jooq.pgsql.*
import open.tresorier.mailing.IMailingPort
import open.tresorier.mailing.adapter.AweberAdapter
import open.tresorier.mailing.adapter.MockMailingAdapter
import open.tresorier.utils.Properties
import open.tresorier.utils.PropertiesEnum.ENVIRONMENT
import org.koin.dsl.module

val properties = Properties()
val adapter_module = module {
    single<IMailingPort> { if (properties.get(ENVIRONMENT) == "prod") AweberAdapter() else MockMailingAdapter() }
    single<IBankingPort> {NordigenAdapter(get(), get())}
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
