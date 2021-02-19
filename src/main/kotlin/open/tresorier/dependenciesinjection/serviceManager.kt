package open.tresorier.dependenciesinjection

import open.tresorier.services.AccountService
import open.tresorier.services.BudgetService
import open.tresorier.services.CategoryService
import open.tresorier.services.PersonService
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin

object ServiceManager : KoinComponent {

    val personService : PersonService by inject()
    val budgetService : BudgetService by inject()
    val accountService : AccountService by inject()
    val categoryService : CategoryService by inject()

    fun start(){
        startKoin{
            printLogger()
            modules(dbAccess_module, app_module)
        }
    }

}
