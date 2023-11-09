import { allocationApi } from './api/apis'

export default class AllocationService {
  public static updateAllocation(month: number, categoryId: string, amount: number) {
    allocationApi.addAllocation(month, categoryId, amount)
  }
}
