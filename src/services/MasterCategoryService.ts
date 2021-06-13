import { Budget, MasterCategoryList } from '@/model/model'
import { masterCategoryApi } from '@/services/api/apis'

export default class MasterCategoryService {
  public static async createMasterCategory (name: string, budget: Budget) {
    await masterCategoryApi.addMasterCategory(name, budget.id)
  }

  public static async updateMasterCategory (masterCategoryId: string, newName: string) {
    const response = await masterCategoryApi.updateMasterCategory(masterCategoryId, newName)
  }

  public static async archiveMasterCategory (masterCategoryId: string) {
    const response = await masterCategoryApi.updateMasterCategory(masterCategoryId, undefined, true)
  }

  public static async unarchiveMasterCategory (masterCategoryId: string) {
    const response = await masterCategoryApi.updateMasterCategory(masterCategoryId, undefined, false)
  }

  public static async getMasterCategories (budget: Budget): Promise<MasterCategoryList> {
    const data: MasterCategoryList = {}
    if (budget.id) {
      const response = await masterCategoryApi.getMasterCategoriesByBudget(budget.id)
      const masterCategories = response.data
      for (const masterCategory of masterCategories) {
        data[masterCategory.id] = masterCategory
      }
    }
    return data
  }
}
