import { Account, Category, Operation } from '@/model/model'
import { operationApi } from '@/services/api/apis'
import Time from '@/utils/Time'

export default class OperationService {
  public static async getOperations (account: Account): Promise<Operation[]> {
    let data: Operation[] = []
    if (account.id) {
      const response = await operationApi.findOperationsByAccount(account.id)
      data = response.data
    }
    return data
  }

  public static async addOperation (accountId: string, day?: string, categoryId?: string, amount?: number, memo?: string) {
    const dayAsInt = day ? Time.getDayFromDateString(day) : undefined
    await operationApi.addOperation(accountId, dayAsInt, categoryId, amount, memo)
  }

  public static async deleteOperation (operation: Operation) {
    await operationApi.deleteOperation(operation.id)
  }
}
