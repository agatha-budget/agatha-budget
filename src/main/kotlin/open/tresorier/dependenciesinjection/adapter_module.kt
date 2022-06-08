package open.tresorier.dependenciesinjection

import open.tresorier.mailing.*
import open.tresorier.mailing.adapter.*
import org.koin.dsl.module

val adapter_module = module {
    single<IMailingPort> { AweberAdapter() }
}
