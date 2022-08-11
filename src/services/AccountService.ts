import { Budget, Account } from '@/model/model'
import { accountApi } from '@/services/api/apis'
import StoreHandler from '@/store/StoreHandler'
import Time from '@/utils/Time'
import { Store } from 'vuex'
import { StoreState } from '@/store/index'

export default class AccountService {
  public static async getAccounts (budget: Budget): Promise<Account[]> {
    const data: Account[] = []
    if (budget.id) {
      const response = await accountApi.findAccountsByBudget(budget.id)
      return response.data
    }
    return data
  }

  public static async createAccount (store: Store<StoreState>, budget: Budget, name: string, amount: number) {
    accountApi.addAccount(budget.id, name, amount, Time.getCurrentDay()).then(
      () => {
        StoreHandler.updateAccounts(store)
      }
    )
  }

  public static async updateAccount (store: Store<StoreState>, id: string, newName?: string) {
    accountApi.updateAccount(id, newName).then(
      () => {
        StoreHandler.updateAccounts(store)
      }
    )
  }

  public static async updateAccountBankAssociation (store: Store<StoreState>, id: string, bankAccountId: string, importHistory: boolean) {
    if (bankAccountId === 'none') {
      accountApi.updateAccountBankAssociation(id, false).then(
        () => {
          StoreHandler.updateAccounts(store)
        }
      )
    } else {
      accountApi.updateAccountBankAssociation(id, importHistory, bankAccountId).then(
        () => {
          StoreHandler.updateAccounts(store)
        }
      )
    }
  }
}
