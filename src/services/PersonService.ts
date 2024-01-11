import type { Person } from '@/model/model'
import { redirectOnApiError } from '@/router'
import { personApi } from '@/services/api/apis'
import axios, { AxiosError, type AxiosResponse } from 'axios'

export default class PersonService {
  public static async getPerson(): Promise<Person> {
    return personApi
      .getPerson()
      .then((r: AxiosResponse) => {
        return r.data
      })
      .catch((e: AxiosError) => {
        redirectOnApiError(e)
      })
  }

  public static async rename(newName: string) {
    await personApi.updatePerson(newName)
  }

  public static async changeStyle(newStyle: string) {
    await personApi.updatePerson(undefined, newStyle)
  }

  public static async changeDyslexia(newDyslexia: boolean) {
    await personApi.updatePerson(undefined, undefined, newDyslexia)
  }

  public static async redirectToBillingPortalUrl(selectedPackage: string) {
    try {
      const billingPortalUrl = (await personApi.createBillingPortalSession(selectedPackage)).data
      window.location.href = billingPortalUrl
    } catch (exception) {
      if (axios.isAxiosError(exception)) {
        alert(exception.response?.data)
      }
    }
  }

  public static async manageSubscription() {
    try {
      const billingPortalUrl = (await personApi.createBillingPortalSession()).data
      window.location.href = billingPortalUrl
    } catch (exception) {
      if (axios.isAxiosError(exception)) {
        alert(exception.response?.data)
      }
    }
  }
}
