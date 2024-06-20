import type { Budget, MasterCategory } from '@/model/model'
import { masterCategoryApi } from '@/services/api/apis'
import { ResultAsync, okAsync } from "neverthrow"
import { defaultErrorHandler } from './ServicesUtils'

export default class MasterCategoryService {
  public static async createMasterCategory(name: string, budget: Budget) {
    await masterCategoryApi.addMasterCategory(name, budget.id)
  }

  public static async renameMasterCategory(masterCategoryId: string, newName: string) {
    await masterCategoryApi.updateMasterCategory(masterCategoryId, newName)
  }

  public static async updateColorMasterCategory(masterCategoryId: string, newColor: string) {
    await masterCategoryApi.updateMasterCategory(
      masterCategoryId,
      undefined,
      undefined,
      undefined,
      newColor
    )
  }

  public static async archiveMasterCategory(masterCategoryId: string) {
    await masterCategoryApi.updateMasterCategory(masterCategoryId, undefined, true)
  }

  public static async unarchiveMasterCategory(masterCategoryId: string) {
    await masterCategoryApi.updateMasterCategory(masterCategoryId, undefined, false)
  }

  public static getMasterCategories(budget: Budget): ResultAsync<MasterCategory[], Error> {
    if (budget.id) {
      return ResultAsync.fromPromise(
        masterCategoryApi.getMasterCategoriesByBudget(budget.id),
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
