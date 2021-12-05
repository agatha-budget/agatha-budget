import { Budget } from '@/model/model'
import { budgetApi } from './api/apis'
import { redirectOnApiError } from '@/router'
import { AxiosError, AxiosResponse } from 'axios'

export default class BudgetService {
  public static async getDefaultBudget (): Promise<Budget> {
    return budgetApi.findBudgetsByUser()
      .then((r: AxiosResponse) => {
        return r.data[0]
      })
      .catch((e: AxiosError) => {
        redirectOnApiError(e)
      })
  }
}
