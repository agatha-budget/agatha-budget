import { Budget, MasterCategory } from '@/model/model'
import { masterCategoryApi } from '@/services/api/apis'

export default class MasterCategoryService {
  public static async createMasterCategory (name: string, budget: Budget) {
    await masterCategoryApi.addMasterCategory(name, budget.id)
  }

  public static async renameMasterCategory (masterCategoryId: string, newName: string) {
    await masterCategoryApi.updateMasterCategory(masterCategoryId, newName)
  }

  public static async archiveMasterCategory (masterCategoryId: string) {
    await masterCategoryApi.updateMasterCategory(masterCategoryId, undefined, true)
  }

  public static async unarchiveMasterCategory (masterCategoryId: string) {
    await masterCategoryApi.updateMasterCategory(masterCategoryId, undefined, false)
  }

  public static async getMasterCategories (budget: Budget): Promise<MasterCategory[]> {
    const data: MasterCategory[] = []
    if (budget.id) {
      const response = await masterCategoryApi.getMasterCategoriesByBudget(budget.id)
      return response.data
    }
    return data
  }
}
