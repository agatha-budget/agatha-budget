import { Budget } from '@/model/model'
import { budgetApi } from './api/apis'
import { redirectToLoginPageIfUnauthorizedError } from '@/router'

export default class BudgetService {
  public static async getDefaultBudget (): Promise<Budget> {
    const response = await budgetApi.findBudgetsByUser()
    redirectToLoginPageIfUnauthorizedError(response)
    return response.data[0]
  }
}
