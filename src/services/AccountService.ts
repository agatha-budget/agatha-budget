import { Account, Budget, AccountList } from '@/model/model'
import { accountApi } from '@/services/api/apis'
import Time from '@/utils/Time'

class AccountService {
  public async getAccounts (budget: Budget): Promise<AccountList> {
    const data: AccountList = {}
    if (budget.id) {
      const response = await accountApi.findAccountsByBudget(budget.id)
      const accounts: Account[] = response.data
      for (const account of accounts) {
        data[account.id] = { id: account.id, name: account.name, amount: 0 }
      }
    }
    return data
  }

  public async createAccount (budget: Budget, name: string, amount: number) {
    await accountApi.addAccount(budget.id, name, amount, Time.getCurrentDay())
  }
}

export const accountService = new AccountService()
