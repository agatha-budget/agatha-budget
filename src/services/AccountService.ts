import { Budget, AccountList } from '@/model/model'
import { accountApi } from '@/services/api/apis'
import Time from '@/utils/Time'

export default class AccountService {
  public static async getAccounts (budget: Budget): Promise<AccountList> {
    const data: AccountList = {}
    if (budget.id) {
      const response = await accountApi.findAccountsByBudget(budget.id)
      const accounts = response.data
      for (const account of accounts) {
        data[account.id] = account
      }
    }
    return data
  }

  public static async createAccount (budget: Budget, name: string, amount: number) {
    await accountApi.addAccount(budget.id, name, amount, Time.getCurrentDay())
  }
}
