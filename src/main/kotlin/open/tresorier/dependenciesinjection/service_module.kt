package open.tresorier.dependenciesinjection

import open.tresorier.services.*
import org.koin.dsl.module

val service_module = module {
    single { AuthorizationService(get(), get(), get(), get(), get(), get(), get()) }
    single { PersonService(get(), get(), get(), get()) }
    single { BillingService(get()) }
    single { BudgetService(get(), get(), get(), get()) }
    single { AccountService(get(), get(), get()) }
    single { CategoryService(get(), get()) }
    single { MasterCategoryService(get(), get(), get()) }
    single { OperationService(get(), get(), get()) }
    single { AllocationService(get(), get()) }
    single { MailingService(get()) }
    single { BudgetDataService(get(), get(), get()) }
    single { UserActivityService(get()) }
    single { BankingService(get(), get(), get(), get(), get()) }

}
