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

  public static async addOperation (store: Store<StoreState>, accountId: string, day?: number, categoryId?: string, amount?: number, memo?: string, isPending?: boolean) {
    await operationApi.addOperation(accountId, day, categoryId, amount, memo, isPending)
    StoreHandler.updateAccounts(store)
  }

  public static async deleteOperation (store: Store<StoreState>, operation: Operation) {
    await operationApi.deleteOperation(operation.id)
    StoreHandler.updateAccounts(store)
  }

  public static async updateOperation (store: Store<StoreState>, operation: Operation, accountId: string, day?: number, categoryId?: string, amount?: number, memo?: string, isPending?: boolean) {
    await operationApi.updateOperation(operation.id, accountId, day, categoryId, amount, memo, isPending)
    StoreHandler.updateAccounts(store)
  }

  public static async importOfxFile (store: Store<StoreState>, accountId: string, ofxFileContent: string): Promise<string> {
    const response = await operationApi.importOfxFile(accountId, ofxFileContent)
    StoreHandler.updateAccounts(store)
    return response.data
  }

  public static async getMotherOperationsByAccount(account: Account, categoryId: string | null): Promise<Operation[]> {
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

  public static async getDaughterOperationsByAccount(account: Account, categoryId: string | null): Promise<Operation[]> {
    let data: Operation[] = []
    if (account.id) {
      let response
      if (categoryId) {
        response = await operationApi.findDaughterOperationsByAccount(account.id, categoryId)
      } else {
        response = await operationApi.findDaughterOperationsByAccount(account.id, undefined)
      }
      data = response.data
    }
    return data
  }

  // public static async  getMotherOperationByDaughter(account: Account, operationId: string): Promise<Operation> {
  //   let data: Operation = 
  //   if (account.id) {
  //     let response = await operationApi.findMotherOperationsByDaughter(operationId)
  //     data = response.data
  //   }
  //   return data
  // }

  public static async  getDaughterOperationByMother(operationId: string): Promise<Operation[]> {
    let response = await operationApi.findDaughterOperationsByMother(operationId)
    let data: Operation[] = response.data
    return data
  }
  
}
