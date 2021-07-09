import { Budget, MasterCategory } from '@/model/model'
import { masterCategoryApi } from '@/services/api/apis'
import { redirectToLoginPageIfUnauthorizedError } from '@/router'

export default class MasterCategoryService {
  public static async createMasterCategory (name: string, budget: Budget) {
    await masterCategoryApi.addMasterCategory(name, budget.id)
  }

  public static async renameMasterCategory (masterCategoryId: string, newName: string) {
    const response = await masterCategoryApi.updateMasterCategory(masterCategoryId, newName)
    redirectToLoginPageIfUnauthorizedError(response)
  }

  public static async archiveMasterCategory (masterCategoryId: string) {
    const response = await masterCategoryApi.updateMasterCategory(masterCategoryId, undefined, true)
    redirectToLoginPageIfUnauthorizedError(response)
  }

  public static async unarchiveMasterCategory (masterCategoryId: string) {
    const response = await masterCategoryApi.updateMasterCategory(masterCategoryId, undefined, false)
    redirectToLoginPageIfUnauthorizedError(response)
  }

  public static async getMasterCategories (budget: Budget): Promise<MasterCategory[]> {
    const data: MasterCategory[] = []
    if (budget.id) {
      const response = await masterCategoryApi.getMasterCategoriesByBudget(budget.id)
      redirectToLoginPageIfUnauthorizedError(response)
      return response.data
    }
    return data
  }
}
