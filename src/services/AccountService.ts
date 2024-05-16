import type { Account, Budget } from '@/model/model'
import { accountApi } from '@/services/api/apis'
import { useBudgetStore } from '@/stores/budgetStore'
import Time from '@/utils/Time'
import { ResultAsync, okAsync } from "neverthrow"
import { defaultErrorHandler } from './ServicesUtils'


export default class AccountService {

  public static getAccounts(budget: Budget): ResultAsync<Account[], Error> {
    if (budget.id) {
      return ResultAsync.fromPromise(
        accountApi.findAccountsByBudget(budget.id),
        defaultErrorHandler
      ).map((response) => {
        return response.data
      })
    }
    else {
      return okAsync([])
    }
  }

  public static async createAccount(
    budget: Budget,
    name: string,
    amount: number
  ) {
    accountApi.addAccount(budget.id, name, amount, Time.getCurrentDay()).then(() => {
      useBudgetStore().updateAccounts()
    })
  }

  public static async updateAccount(id: string, newName?: string) {
    accountApi.updateAccount(id, newName).then(() => {
      useBudgetStore().updateAccounts()
    })
  }

  public static async updateAccountBankAssociation(
    id: string,
    bankAccountId: string,
    importHistory: boolean
  ) {
    const budgetStore = useBudgetStore()
    if (bankAccountId === 'none') {
      accountApi.updateAccountBankAssociation(id, false).then(() => {
        budgetStore.updateAccounts()
      })
    } else {
      accountApi.updateAccountBankAssociation(id, importHistory, bankAccountId).then(() => {
        budgetStore.updateAccounts()
      })
    }
  }
}
