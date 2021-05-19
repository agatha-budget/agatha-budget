import { Budget, MasterCategoryList } from '@/model/model'
import { masterCategoryApi } from '@/services/api/apis'

export default class CategoryService {
  public static async createCategory (name: string, budget: Budget) {
    await masterCategoryApi.addMasterCategory(name, budget.id)
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
