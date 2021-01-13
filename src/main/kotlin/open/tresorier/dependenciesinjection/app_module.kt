package open.tresorier.dependenciesinjection


import org.koin.dsl.module

import open.tresorier.dao.IPersonDao
import open.tresorier.dao.jooq.JooqPersonDao
import open.tresorier.services.PersonService
import open.tresorier.generated.jooq.tables.daos.PersonDao as GeneratedPersonDao

val app_module = module {
    single<IPersonDao> { JooqPersonDao(GeneratedPersonDao(get())) }
    single<PersonService> { PersonService(get()) }
}
