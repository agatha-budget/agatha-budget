import type { Budget, BudgetData, CategoryDataList } from '@/model/model'
import { budgetApi, budgetDataApi } from './api/apis'
import { redirectOnApiError } from '@/router'
import { AxiosError } from 'axios'
import type {AxiosResponse} from 'axios'

export default class BudgetDataService {
  public static async getDefaultBudget(): Promise<Budget> {
    return budgetApi
      .findBudgetsByUser()
      .then((r: AxiosResponse) => {
        return r.data[0]
      })
      .catch((e: AxiosError) => {
        redirectOnApiError(e)
      })
  }

  public static async getBudgetData(budget: Budget): Promise<BudgetData> {
    if (budget.id == null) {
      return []
    }
    return budgetDataApi
      .findBudgetData(budget.id)
      .then((r: AxiosResponse) => {
        return r.data
      })
      .catch((e: AxiosError) => {
        redirectOnApiError(e)
      })
  }

  public static async getBudgetDataForMonth(
    budget: Budget,
    month: number
  ): Promise<CategoryDataList> {
    if (budget.id == null) {
      return {}
    }
    return budgetDataApi
      .findBudgetData(budget.id, month, month)
      .then((r: AxiosResponse) => {
        return r.data[month]
      })
      .catch((e: AxiosError) => {
        redirectOnApiError(e)
      })
  }

  public static async getBudgetAmount(budget: Budget, month: number): Promise<number> {
    if (budget.id == null) {
      return 0
    }
    return budgetDataApi
      .findTotalBudgetAmount(budget.id, month)
      .then((r: AxiosResponse) => {
        return r.data
      })
      .catch((e: AxiosError) => {
        redirectOnApiError(e)
      })
  }
}
