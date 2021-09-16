import { Budget, BudgetData, CategoryDataList } from '@/model/model'
import { budgetApi, budgetDataApi } from './api/apis'
import { redirectToLoginPageIfUnauthorizedError } from '@/router'

export default class BudgetDataService {
  public static async getDefaultBudget (): Promise<Budget> {
    const response = await budgetApi.findBudgetsByUser()
    redirectToLoginPageIfUnauthorizedError(response)
    return response.data[0]
  }

  public static async getBudgetData (budget: Budget): Promise<BudgetData> {
    if (budget.id == null) { return [] }
    const response = await budgetDataApi.findBudgetData(budget.id)
    redirectToLoginPageIfUnauthorizedError(response)
    return response.data
  }

  public static async getBudgetDataForMonth (budget: Budget, month: number): Promise<CategoryDataList> {
    if (budget.id == null) { return {} }
    const response = await budgetDataApi.findBudgetData(budget.id, month, month)
    redirectToLoginPageIfUnauthorizedError(response)
    return response.data[month]
  }

  public static async getBudgetAmount (budget: Budget, month: number): Promise<number> {
    if (budget.id == null) { return 0 }
    const response = await budgetDataApi.findTotalBudgetAmount(budget.id, month)
    redirectToLoginPageIfUnauthorizedError(response)
    return response.data
  }
}
