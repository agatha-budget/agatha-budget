import { Budget, BudgetData, CategoryDataList } from '@/model/model'
import { budgetApi, budgetDataApi } from './api/apis'

export default class BudgetDataService {
  public static async getDefaultBudget (): Promise<Budget> {
    const response = await budgetApi.findBudgetsByUser()
    return response.data[0]
  }

  public static async getBudgetData (budget: Budget): Promise<BudgetData> {
    const response = await budgetDataApi.findBudgetData(budget.id)
    return response.data
  }

  public static async getBudgetDataForMonth (budget: Budget, month: number): Promise<CategoryDataList> {
    const response = await budgetDataApi.findBudgetData(budget.id, month, month)
    return response.data[month]
  }

  public static async getBudgetAmount (budget: Budget, month: number): Promise<number> {
    const response = await budgetDataApi.findTotalBudgetAmount(budget.id, month)
    return response.data
  }
}
