import { allocationApi } from './api/apis'
import { redirectOnApiError } from '@/router'

export default class AllocationService {
  public static async updateAllocation (month: number, categoryId: string, amount: number) {
    const response = await allocationApi.addAllocation(month, categoryId, amount)
  }
}
