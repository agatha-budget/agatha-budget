import type { Account, Operation, OperationWithDaughters } from '@/model/model'
import { operationToOperationWithDaughter } from '@/model/model'
import OperationService from '@/services/OperationService'
import Utils from '@/utils/Utils'
import { defineStore } from 'pinia'

export const useOperationStore = defineStore('operation', {
  state: () => ({
    operations: {} as Record<string, OperationWithDaughters[]>,
    storeLoaded: false,
  }),
  actions: {
    reset() {
      this.operations = {}
      this.storeLoaded = false
    },
    retrieveOperations(accounts: Account[]) {
      this.operations = {} // reset to prevent duplication of operations
      Promise.all(accounts.map(
        async (account) => this.retrieveOperationsForAccount(account))
      ).then(
        () => this.storeLoaded = true
      )
    },
    async retrieveOperationsForAccount(account: Account) {
      let res = await OperationService.getOperations(account)
        if (res.isOk()) {
          this.operations[account.id] = res.value
        } else {
          alert(`Désolée ! un problème est survenu lors du chargement des opérations du compte ${account.name}`)
        }
    },
    getOperationByAccount(accountId: string) : OperationWithDaughters[] {
      return this.operations[accountId] 
    },
    getOperationByAccountAndCategory(accountId: string, categoryId: string) : OperationWithDaughters[] {
      const operations: OperationWithDaughters[] = []
      for (const op of this.operations[accountId]) {
        if (op.categoryId === categoryId) {
          operations.push(op)
        }
        for (const dau of op.daughters){
          if (dau.categoryId === categoryId) {
            operations.push(op)
          }
        }
      }
      return operations 
    },
    addOperationToAccount(operation: Operation) {
      if (operation.motherOperationId){
        this.addDaughterOperation(operation)
      } else {
        let motherOperation = operationToOperationWithDaughter(operation)
        this.operations[operation.accountId] = Utils.insertInListSortedByDate(motherOperation, this.operations[operation.accountId])
      }
    },
    addDaughterOperation(operation: Operation) {
      for (const op of this.operations[operation.accountId]) {
        if (op.id === operation.motherOperationId) {
          op.daughters.push(operation)
        }
      } 
    },
    update(operation: Operation) {
      if (operation.motherOperationId){
        this.updateDaughterOperation(operation)
      } else {
        this.updateOperationWithDaugther(operation)
      }
    },
    updateOperationWithDaugther(operation: Operation) {
      const index = this.getOperationIndex(operation.id, this.operations[operation.accountId])
      let daugthers = this.operations[operation.accountId][index].daughters
      let operationWithDaughters = operationToOperationWithDaughter(operation)
      operationWithDaughters.daughters = daugthers
      if (index >= 0) {
        this.operations[operation.accountId].splice(index, 1, operationWithDaughters)

      }    
    },
    updateDaughterOperation(operation: Operation) {
      for (const op of this.operations[operation.accountId]) {
        if (op.id === operation.motherOperationId) {
          const index = this.getOperationIndex(operation.id, op.daughters)
          if (index >= 0) {
            op.daughters.splice(index, 1, operation)
          } else {
            alert(`Une erreur a eu lieu lors de la mise à jour de l'opération ${operation.memo}. Nous vous conseillons de recharger la page`)
          }
        }
      } 
    },
    delete(accountId: string, operationId: string, motherOperationId?: string) {
      if (motherOperationId){
        this.deleteDaughterOperation(accountId, operationId, motherOperationId)
      } else {
        this.deleteOperationInAccount(accountId, operationId)
      }
    },
    deleteOperationInAccount(accountId: string, operationId: string) {
      const index = this.getOperationIndex(operationId, this.operations[accountId])
      if (index >= 0) {
        this.operations[accountId].splice(index, 1)
      }
    },
    deleteDaughterOperation(accountId: string, operationId: string, motherOperationId: string) {
      for (const op of this.operations[accountId]) {
        if (op.id === motherOperationId) {
          const index = this.getOperationIndex(operationId, op.daughters)
          if (index >= 0) {
            op.daughters.splice(index, 1)
          } else {
            alert("Une erreur a eu lieu lors de la mise à jour de l'opération. Nous vous conseillons de recharger la page")
          }
        }
      } 
    },
    getOperationIndex(operationId: string, operationList: Operation[] | OperationWithDaughters[]) : number{
      for (const [index, op] of operationList.entries()) {
        if (op.id === operationId) {
          return index
        }
      }
      return -1
    }
  }
})
