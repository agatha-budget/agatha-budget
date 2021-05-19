import { Budget, BudgetData } from '@/model/model'
import { budgetApi, budgetDataApi } from './api/apis'

export default class BudgetDataService {
  public static async getDefaultBudget (): Promise<Budget> {
    const response = await budgetApi.findBudgetsByUser()
    return response.data[0]
  }

  public static async getBudgetData (budget: Budget): Promise<BudgetData> {
    const response = await budgetDataApi.findBudgetData(budget.id)
    for (const monthString in response) {
      console.log(monthString)
    }
    return {}
  }
}
