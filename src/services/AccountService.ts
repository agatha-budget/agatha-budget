import type { Budget, Account } from '@/model/model'
import { accountApi } from '@/services/api/apis'
import Time from '@/utils/Time'
import { useBudgetStore } from '@/stores/budgetStore'


export default class AccountService {
  public static async getAccounts(budget: Budget): Promise<Account[]> {
    const data: Account[] = []
    if (budget.id) {
      const response = await accountApi.findAccountsByBudget(budget.id)
      return response.data
    }
    return data
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
