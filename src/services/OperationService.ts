import { Account, Operation } from '@/model/model'
import { operationApi } from '@/services/api/apis'
import StoreHandler from '@/store/StoreHandler'
import { StoreState } from '@/store/index'
import { Store } from 'vuex'

export default class OperationService {
  public static async getOperations (account: Account, categoryId: string | null): Promise<Operation[]> {
    let data: Operation[] = []
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

  public static async addOperation (store: Store<StoreState>, accountId: string, day?: number, categoryId?: string, amount?: number, memo?: string, isPending?: boolean, motheroperationId?: string): Promise<Operation> {
    const response = await operationApi.addOperation(accountId, day, categoryId, amount, memo, isPending, motheroperationId)
    StoreHandler.updateAccounts(store)
    return response.data
  }

  public static async deleteOperation (store: Store<StoreState>, operation: Operation) {
    const daughters = await this.getDaughtersFromMother(operation)
    if (daughters.length > 0) {
      daughters.forEach(daughter => {
        operationApi.deleteOperation(daughter.id)
      })
    }
    await operationApi.deleteOperation(operation.id)
    StoreHandler.updateAccounts(store)
  }

  public static async updateOperation (store: Store<StoreState>, operationId: string, accountId: string, day?: number, categoryId?: string, amount?: number, memo?: string, isPending?: boolean, motheroperationId?: string): Promise<Operation> {
    const response = await operationApi.updateOperation(operationId, accountId, day, categoryId, amount, memo, isPending, motheroperationId)
    StoreHandler.updateAccounts(store)
    return response.data
  }

  public static async importOfxFile (store: Store<StoreState>, accountId: string, ofxFileContent: string): Promise<string> {
    const response = await operationApi.importOfxFile(accountId, ofxFileContent)
    StoreHandler.updateAccounts(store)
    return response.data
  }

  public static async getMotherOperationsByAccount (account: Account, categoryId: string | null): Promise<Operation[]> {
    let data: Operation[] = []
    if (account.id) {
      let response
      if (categoryId) {
        response = await operationApi.findMotherOperationsByAccount(account.id, categoryId)
      } else {
        response = await operationApi.findMotherOperationsByAccount(account.id, undefined)
      }
      data = response.data
    }
    return data
  }

  public static async getDaughtersFromMother (operation: Operation): Promise<Operation[]> {
    const response = await operationApi.findDaughterOperationsByMother(operation.id)
    return response.data
  }

  public static async getMotherFromDaughter (operation: Operation): Promise<Operation> {
    const response = await operationApi.findMotherOperationsByDaughter(operation.id)
    return response.data
  }
}
