import { Budget, CategoryList, MasterCategory } from '@/model/model'
import { categoryApi } from '@/services/api/apis'

export default class CategoryService {
  public static async createCategory (name: string, masterCategory: MasterCategory) {
    await categoryApi.addCategory(name, masterCategory.id)
  }

  public static async getCategories (budget: Budget): Promise<CategoryList> {
    const data: CategoryList = {}
    if (budget.id) {
      const response = await categoryApi.getCategoriesByBudget(budget.id)
      const categories = response.data
      for (const category of categories) {
        data[category.id] = category
      }
    }
    return data
  }
}
