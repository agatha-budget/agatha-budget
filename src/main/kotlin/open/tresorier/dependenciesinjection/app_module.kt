package open.tresorier.dependenciesinjection


import open.tresorier.dao.IAccountDao
import open.tresorier.dao.IBudgetDao
import open.tresorier.dao.IPersonDao
import open.tresorier.dao.jooq.main.JooqAccountDao
import open.tresorier.dao.jooq.main.JooqBudgetDao
import open.tresorier.dao.jooq.main.JooqPersonDao
import open.tresorier.services.BudgetService
import open.tresorier.services.PersonService
import org.koin.dsl.module

val app_module = module {
    single<IPersonDao> { JooqPersonDao((get())) }
    single<IBudgetDao> { JooqBudgetDao((get())) }
    single<IAccountDao> { JooqAccountDao((get())) }
    single<PersonService> { PersonService(get()) }
    single<BudgetService> { BudgetService(get()) }
}
