import { Account, Operation } from '@/model/model'
import { operationApi } from '@/services/api/apis'

class OperationService {
  public async getOperations (account: Account): Promise<Operation[]> {
    let data: Operation[] = []
    if (account.id) {
      const response = await operationApi.findOperationsByAccount(account.id)
      data = response.data
    }
    return data
  }
}

export const operationService = new OperationService()
