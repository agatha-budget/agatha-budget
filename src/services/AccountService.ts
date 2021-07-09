import { Budget, Account } from '@/model/model'
import { accountApi } from '@/services/api/apis'
import { store } from '@/store'
import StoreHandler from '@/store/StoreHandler'
import Time from '@/utils/Time'
import { redirectToLoginPageIfUnauthorizedError } from '@/router'

export default class AccountService {
  public static async getAccounts (budget: Budget): Promise<Account[]> {
    const data: Account[] = []
    if (budget.id) {
      const response = await accountApi.findAccountsByBudget(budget.id)
      redirectToLoginPageIfUnauthorizedError(response)
      return response.data
    }
    return data
  }

  public static async createAccount (budget: Budget, name: string, amount: number) {
    await accountApi.addAccount(budget.id, name, amount, Time.getCurrentDay()).then(
      (response) => {
        redirectToLoginPageIfUnauthorizedError(response)
        StoreHandler.updateAccounts(store)
      }
    )
  }
}
