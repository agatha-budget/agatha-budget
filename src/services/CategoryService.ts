import { Budget, Category, CategoryList, MasterCategory } from '@/model/model'
import { categoryApi } from '@/services/api/apis'

export default class CategoryService {
  public static async createCategory (name: string, masterCategory: MasterCategory) {
    const response = await categoryApi.addCategory(name, masterCategory.id)
  }

  public static async updateCategory (categoryId: string, newName: string) {
    const response = await categoryApi.updateCategory(categoryId, newName)
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
