import { Account, Budget } from '@/model/model'
import { accountApi } from '@/services/api/apis'
import Time from '@/utils/Time'

export default class AccountService {
  public static async getAccounts (budget: Budget): Promise<Account[]> {
    let data: Account[] = []
    if (budget.id) {
      const response = await accountApi.findAccountsByBudget(budget.id)
      data = response.data
    }
    return data
  }

  public static async createAccount (budget: Budget, name: string, amount: number) {
    await accountApi.addAccount(budget.id, name, amount, Time.getCurrentDay())
  }
}
