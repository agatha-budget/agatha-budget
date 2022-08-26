import { PersonApi, AccountApi, BudgetApi, AllocationApi, BudgetDataApi, OperationApi, CategoryApi, MasterCategoryApi, BankingApi } from './openApi/api'

export const personApi = new PersonApi()
export const budgetApi = new BudgetApi()
export const budgetDataApi = new BudgetDataApi()
export const accountApi = new AccountApi()
export const operationApi = new OperationApi()
export const allocationApi = new AllocationApi()
export const categoryApi = new CategoryApi()
export const masterCategoryApi = new MasterCategoryApi()
export const bankingApi = new BankingApi()
