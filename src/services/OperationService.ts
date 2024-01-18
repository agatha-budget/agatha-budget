import type { Account, Operation, OperationWithDaughters } from '@/model/model'
import { operationApi } from '@/services/api/apis'
import { useBudgetStore } from '@/stores/budgetStore'

export default class OperationService {
  public static async getOperations(
    account: Account,
    categoryId: string | null
  ): Promise<OperationWithDaughters[]> {
    let data: OperationWithDaughters[] = []
    if (account.id) {
      let response
      if (categoryId) {
        response = await operationApi.findOperationsByAccount(account.id, categoryId)
      } else {
        response = await operationApi.findOperationsByAccount(account.id, undefined)
      }
      data = response.data
    }
    return data
  }

  public static async addOperation(
    accountId: string,
    day?: number,
    categoryId?: string,
    amount?: number,
    memo?: string,
    isPending?: boolean,
    motheroperationId?: string
  ): Promise<Operation> {
    const response = await operationApi.addOperation(
      accountId,
      day,
      categoryId,
      amount,
      memo,
      isPending,
      motheroperationId
    )
    useBudgetStore().updateAccounts()
    return response.data
  }

  public static async deleteOperation(operationId: string) {
    await operationApi.deleteOperation(operationId)
    useBudgetStore().updateAccounts()
  }

  public static async updateOperation(
    operationId: string,
    accountId: string,
    day?: number,
    categoryId?: string,
    removeCategory?: boolean,
    amount?: number,
    memo?: string,
    isPending?: boolean,
    motheroperationId?: string
  ): Promise<Operation> {
    const response = await operationApi.updateOperation(
      operationId,
      accountId,
      day,
      categoryId,
      removeCategory,
      amount,
      memo,
      isPending,
      motheroperationId
    )
    useBudgetStore().updateAccounts()
    return response.data
  }

  public static async importOfxFile(
    accountId: string,
    ofxFileContent: string
  ): Promise<string> {
    const response = await operationApi.importOfxFile(accountId, ofxFileContent)
    useBudgetStore().updateAccounts()
    return response.data
  }
}
