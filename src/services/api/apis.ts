import { PersonApi, AccountApi, BudgetApi, AllocationApi, BudgetDataApi, OperationApi } from './openApi/api'

export const personApi = new PersonApi()
export const budgetApi = new BudgetApi()
export const budgetDataApi = new BudgetDataApi()
export const accountApi = new AccountApi()
export const operationApi = new OperationApi()
export const allocationApi = new AllocationApi()
