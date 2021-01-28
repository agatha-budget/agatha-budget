package open.tresorier.dependenciesinjection


import org.koin.dsl.module

import open.tresorier.dao.IPersonDao
import open.tresorier.dao.IBudgetDao
import open.tresorier.dao.jooq.JooqPersonDao
import open.tresorier.dao.jooq.JooqBudgetDao
import open.tresorier.services.*
import open.tresorier.generated.jooq.tables.daos.PersonDao as GeneratedPersonDao
import open.tresorier.generated.jooq.tables.daos.BudgetDao as GeneratedBudgetDao

val app_module = module {
    single<IPersonDao> { JooqPersonDao(GeneratedPersonDao(get())) }
    single<IBudgetDao> { JooqBudgetDao(GeneratedBudgetDao(get())) }
    single<PersonService> { PersonService(get()) }
    single<BudgetService> { BudgetService(get()) }
}
