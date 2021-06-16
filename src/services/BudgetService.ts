import { Budget } from '@/model/model'
import { budgetApi } from './api/apis'

export default class BudgetService {
  public static async getDefaultBudget (): Promise<Budget> {
    const response = await budgetApi.findBudgetsByUser()
    return response.data[0]
  }
}
