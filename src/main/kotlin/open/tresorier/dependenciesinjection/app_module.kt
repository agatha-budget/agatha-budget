package open.tresorier.dependenciesinjection


import open.tresorier.dao.*
import open.tresorier.dao.jooq.main.*
import open.tresorier.services.*
import org.koin.dsl.module

val app_module = module {
    single<IPersonDao> { JooqPersonDao((get())) }
    single<IBudgetDao> { JooqBudgetDao((get())) }
    single<IAccountDao> { JooqAccountDao((get())) }
    single<ICategoryDao> { JooqCategoryDao((get())) }
    single<IMasterCategoryDao> { JooqMasterCategoryDao((get())) }
    single<IOperationDao> { JooqOperationDao((get())) }
    single<IAllocationDao> { JooqAllocationDao(get()) }

    single { AuthorizationService(get(), get(), get(), get(), get()) }
    single { PersonService(get(), get()) }
    single { BudgetService(get(), get()) }
    single { AccountService(get(), get(), get()) }
    single { CategoryService(get(), get()) }
    single { OperationService(get(), get()) }
    single { AllocationService(get(), get()) }

}
