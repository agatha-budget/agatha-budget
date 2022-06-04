package open.tresorier.dependenciesinjection

import open.tresorier.dao.*
import open.tresorier.dao.jooq.pgsql.*
import open.tresorier.services.*
import org.koin.dsl.module

val adapter_module = module {
    single<IMailingPort> { AweberAdapter((get())) }
}
