import { Budget, Category, MasterCategory } from '@/model/model'
import { categoryApi } from '@/services/api/apis'
import { redirectToLoginPageIfUnauthorizedError } from '@/router'

export default class CategoryService {
  public static async createCategory (name: string, masterCategory: MasterCategory) {
    const response = await categoryApi.addCategory(name, masterCategory.id)
    redirectToLoginPageIfUnauthorizedError(response)
  }

  public static async updateCategory (categoryId: string, newName: string) {
    const response = await categoryApi.updateCategory(categoryId, newName)
    redirectToLoginPageIfUnauthorizedError(response)
  }

  public static async archiveCategory (categoryId: string) {
    const response = await categoryApi.updateCategory(categoryId, undefined, undefined, true)
    redirectToLoginPageIfUnauthorizedError(response)
  }

  public static async unarchiveCategory (categoryId: string) {
    const response = await categoryApi.updateCategory(categoryId, undefined, undefined, false)
    redirectToLoginPageIfUnauthorizedError(response)
  }

  public static async getCategories (budget: Budget): Promise<Category[]> {
    const data: Category[] = []
    if (budget.id) {
      const response = await categoryApi.getCategoriesByBudget(budget.id)
      redirectToLoginPageIfUnauthorizedError(response)
      return response.data
    }
    return data
  }
}
