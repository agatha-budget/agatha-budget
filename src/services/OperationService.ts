import { Account, Operation } from '@/model/model'
import { operationApi } from '@/services/api/apis'

export default class OperationService {
  public static async getOperations (account: Account): Promise<Operation[]> {
    let data: Operation[] = []
    if (account.id) {
      const response = await operationApi.findOperationsByAccount(account.id)
      data = response.data
    }
    return data
  }

  public static async addOperation (accountId: string, day?: number, categoryId?: string, amount?: number, memo?: string) {
    await operationApi.addOperation(accountId, day, categoryId, amount, memo)
  }

  public static async deleteOperation (operation: Operation) {
    await operationApi.deleteOperation(operation.id)
  }

  public static async updateOperation (operation: Operation, accountId: string, day?: number, categoryId?: string, amount?: number, memo?: string) {
    await operationApi.updateOperation(operation.id, accountId, day, categoryId, amount, memo)
  }
}
