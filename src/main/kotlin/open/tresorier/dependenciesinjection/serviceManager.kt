package open.tresorier.dependenciesinjection

import open.tresorier.services.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin

object ServiceManager : KoinComponent {

    val personService : PersonService by inject()
    val budgetService : BudgetService by inject()
    val accountService : AccountService by inject()
    val budgetDataService : BudgetDataService by inject()
    val categoryService : CategoryService by inject()
    val masterCategoryService: MasterCategoryService by inject()
    val allocationService : AllocationService by inject()
    val operationService : OperationService by inject()
    val billingService: BillingService by inject()
    val mailingService: MailingService by inject()
    val bankingService: BankingService by inject()
    val userActivityService: UserActivityService by inject()
    val postItService: PostItService by inject()

    fun start(){
        startKoin{
            printLogger()
            modules(dbAccess_module, adapter_module, service_module)
        }
    }

}
