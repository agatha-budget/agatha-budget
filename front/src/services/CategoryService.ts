import type { Budget, Category, MasterCategory } from '@/model/model'
import { categoryApi } from '@/services/api/apis'
import { ResultAsync, okAsync } from 'neverthrow'
import { defaultErrorHandler } from './ServicesUtils'

export default class CategoryService {
  public static async createCategory(name: string, masterCategory: MasterCategory) {
    await categoryApi.addCategory(name, masterCategory.id)
  }

  public static async updateCategory(categoryId: string, newName: string) {
    await categoryApi.updateCategory(categoryId, newName)
  }

  public static async archiveCategory(categoryId: string) {
    await categoryApi.updateCategory(categoryId, undefined, undefined, true)
  }

  public static async unarchiveCategory(categoryId: string) {
    await categoryApi.updateCategory(categoryId, undefined, undefined, false)
  }

  public static getCategories(budget: Budget): ResultAsync<Category[], Error> {
    if (budget.id) {
      return ResultAsync.fromPromise(
        categoryApi.getCategoriesByBudget(budget.id),
        defaultErrorHandler
      ).map((response) => {
        return response.data
      })
    }
    else {
      return okAsync([])
    }
  }



}
