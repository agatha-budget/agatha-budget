import { Account, Budget, AccountList } from '@/model/model'
import { accountApi } from './api/apis'

class AccountService {
  public async getAccounts (budget: Budget): Promise<AccountList> {
    const response = await accountApi.findAccountsByBudget(budget.id)
    const accounts: Account[] = response.data
    const data: AccountList = {}
    for (const account of accounts) {
      data[account.id] = { id: account.id, name: account.name, amount: 0 }
    }
    return data
  }

  public async createAccount (budget: Budget, name: string, amount: number) {
    const response = await accountApi.addAccount(budget.id, name, amount)
  }
}

export const accountService = new AccountService()
