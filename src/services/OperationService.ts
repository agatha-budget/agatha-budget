import { Account, Operation } from '@/model/model'
import { operationApi } from '@/services/api/apis'
import StoreHandler from '@/store/StoreHandler'
import { StoreState } from '@/store/index'
import { Store } from 'vuex'
import { redirectOnApiError } from '@/router'

export default class OperationService {
  public static async getOperations (account: Account): Promise<Operation[]> {
    let data: Operation[] = []
    if (account.id) {
      const response = await operationApi.findOperationsByAccount(account.id)
      data = response.data
    }
    return data
  }

  public static async addOperation (store: Store<StoreState>, accountId: string, day?: number, categoryId?: string, amount?: number, memo?: string, isPending?: boolean) {
    const response = await operationApi.addOperation(accountId, day, categoryId, amount, memo, isPending)
    StoreHandler.updateAccounts(store)
  }

  public static async deleteOperation (store: Store<StoreState>, operation: Operation) {
    const response = await operationApi.deleteOperation(operation.id)
    StoreHandler.updateAccounts(store)
  }

  public static async updateOperation (store: Store<StoreState>, operation: Operation, accountId: string, day?: number, categoryId?: string, amount?: number, memo?: string, isPending?: boolean) {
    const response = await operationApi.updateOperation(operation.id, accountId, day, categoryId, amount, memo, isPending)
    StoreHandler.updateAccounts(store)
  }

  public static async importOfxFile (store: Store<StoreState>, accountId: string, ofxFileContent: string): Promise<string> {
    const response = await operationApi.importOfxFile(accountId, ofxFileContent)
    StoreHandler.updateAccounts(store)
    return response.data
  }
}
