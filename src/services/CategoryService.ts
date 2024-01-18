import type { Budget, Category, MasterCategory } from '@/model/model'
import { categoryApi } from '@/services/api/apis'

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

  public static async getCategories(budget: Budget): Promise<Category[]> {
    const data: Category[] = []
    if (budget.id) {
      const response = await categoryApi.getCategoriesByBudget(budget.id)
      return response.data
    }
    return data
  }
}
