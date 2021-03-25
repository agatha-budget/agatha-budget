import { PersonApi, AccountApi, BudgetApi } from './openApi/api'

export const personApi = new PersonApi()
export const budgetApi = new BudgetApi()
export const accountApi = new AccountApi()