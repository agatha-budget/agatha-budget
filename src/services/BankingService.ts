import { Bank } from '@/model/model'
import { bankingApi } from '@/services/api/apis'
import axios from 'axios'

export default class BankingService {
  public static async getAvailableBanks (): Promise<Bank[]> {
    const response = await bankingApi.getAvailableBanks()
    return response.data
  }

  public static async goToBankAgreement (bankId: string) {
    try {
      const agreementUrl = (await (bankingApi.getLinkForBankAgreement(bankId))).data
      window.location.href = agreementUrl
    } catch (exception) {
      if (axios.isAxiosError(exception)) {
        alert(exception.response?.data)
      }
    }
  }
}
