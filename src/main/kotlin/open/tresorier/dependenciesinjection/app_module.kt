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
    single<IOperationDao> { JooqOperationDao((get())) }
    single<IAllocationDao> { JooqAllocationDao(get()) }

    single<AuthorizationService> { (get()) }
    single<PersonService> { PersonService(get()) }
    single<BudgetService> { BudgetService(get(), get()) }
    single<AccountService> { AccountService(get(), get(), get()) }
    single<CategoryService> { CategoryService(get(), get()) }
    single<OperationService> { OperationService(get(), get()) }
    single<AllocationService> { AllocationService(get(), get()) }

}
