import { Budget } from '@/model/model'
import { budgetApi } from './api/apis'
import { redirectToLoginPageIfUnauthorizedError } from '@/router'

export default class BudgetService {
  public static async getDefaultBudget (): Promise<Budget> {
    try {
      const response = await budgetApi.findBudgetsByUser()
      return response.data[0]
    } catch (exception) {
      redirectToLoginPageIfUnauthorizedError(exception.response)
      return exception.response
    }
  }
}
