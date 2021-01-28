package open.tresorier.dependenciesinjection

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin

import open.tresorier.dao.*
import open.tresorier.services.*

object ServiceManager : KoinComponent {

    val personDao : IPersonDao by inject()
    val budgetDao : IBudgetDao by inject()
    val personService : PersonService by inject()
    val budgetService : BudgetService by inject()

    fun start(){
        startKoin{
            printLogger()
            modules(dbAccess_module, app_module)
        }
    }

}
