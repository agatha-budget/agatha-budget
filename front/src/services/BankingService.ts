import type { Bank, Budget, BankAccount } from '@/model/model'
import { bankingApi } from '@/services/api/apis'
import axios from 'axios'

export default class BankingService {
  public static async getAvailableBanks(): Promise<Bank[]> {
    const response = await bankingApi.getAvailableBanks()
    return response.data
  }

  public static async getAuthorizedAccounts(budget: Budget): Promise<BankAccount[]> {
    const response = await bankingApi.getAuthorizedAccounts(budget.id)
    return response.data
  }

  public static async updateBankAccountList(bankAgreemendId: string) {
    await bankingApi.updateBankAccountList(bankAgreemendId)
  }

  public static async goToBankAgreement(budget: Budget, bankId: string) {
    try {
      const agreementUrl = (await bankingApi.getLinkForBankAgreement(bankId, budget.id)).data
      window.location.href = agreementUrl
    } catch (exception) {
      if (axios.isAxiosError(exception)) {
        alert(exception.response?.data)
      }
    }
  }
}
