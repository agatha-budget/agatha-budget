package open.tresorier.dependenciesinjection

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin

import open.tresorier.dao.IPersonDao
import open.tresorier.dao.IBudgetDao
import open.tresorier.services.PersonService

object ServiceManager : KoinComponent {

    val personDao : IPersonDao by inject()
    val budgetDao : IBudgetDao by inject()
    val personService : PersonService by inject()

    fun start(){
        startKoin{
            printLogger()
            modules(dbAccess_module, app_module)
        }
    }

}
