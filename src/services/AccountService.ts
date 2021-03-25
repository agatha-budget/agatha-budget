import { Account, Currency } from '@/model/model'
import { currencies } from "@/model/enum";

class AccountService {
  public async getAccounts (): Promise<Account> {
    return {
      id: "account_id",
      name: "La Poste",
      amount: 25.75,
      currency: currencies.EUROS
    }
  }

  public async createAccount (name: string, amount : number, currency : Currency): Promise<Account> {
    return {
      id: "account_2id",
      name: name,
      amount: amount,
      currency: currency
    }
  }

}

export const accountService = new AccountService()
