import type { Account, Operation, OperationWithDaughters } from '@/model/model'
import { operationApi } from '@/services/api/apis'
import { useBudgetStore } from '@/stores/budgetStore'
import { useOperationStore } from '@/stores/operationStore'
import { ResultAsync } from "neverthrow"
import { defaultErrorHandler } from './ServicesUtils'

export default class OperationService {

  public static getOperations(
    account: Account,
    categoryId: string | null = null
  ): ResultAsync<OperationWithDaughters[], Error> {
    
    return ResultAsync.fromPromise(
      operationApi.findOperationsByAccount(account.id, categoryId || undefined),
      () => Error("error while contacting the DB")
    ).map((response) => response.data)
  }
  
  public static addOperation(
    accountId: string,
    day?: number,
    categoryId?: string,
    amount?: number,
    memo?: string,
    isPending?: boolean,
    motheroperationId?: string
  ): ResultAsync<Operation, Error> {
    return ResultAsync.fromPromise(
      operationApi.addOperation(
        accountId,
        day,
        categoryId,
        amount,
        memo,
        isPending,
        motheroperationId
      ),
      defaultErrorHandler
    ).map((response) => {
      let operation = response.data
      useOperationStore().addOperationToAccount(operation)
      return operation
    })
  }

  public static deleteOperation(accountId: string, operationId: string): ResultAsync<string, Error> {
    return ResultAsync.fromPromise(
      operationApi.deleteOperation(operationId),
      defaultErrorHandler
    ).map((response) => {
        useOperationStore().delete(accountId, operationId)
        return response.data
    })
  }

  public static deleteDaughterOperation(accountId: string, operationId: string, motherOperationId: string): ResultAsync<string, Error> {
    return ResultAsync.fromPromise(
      operationApi.deleteOperation(operationId),
      defaultErrorHandler
    ).map((response) => {
        useOperationStore().delete(accountId, operationId, motherOperationId)
        return response.data
    })
  }

  public static updateOperation(
    operationId: string,
    accountId: string,
    day?: number,
    categoryId?: string,
    removeCategory?: boolean,
    amount?: number,
    memo?: string,
    isPending?: boolean,
    motheroperationId?: string
  ): ResultAsync<Operation, Error> {
    return ResultAsync.fromPromise(
      operationApi.updateOperation(
        operationId,
        accountId,
        day,
        categoryId,
        removeCategory,
        amount,
        memo,
        isPending,
        motheroperationId
      ),
      defaultErrorHandler
    ).map((response) => {
      let operation = response.data
      useOperationStore().update(operation)
      return operation
    })
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
