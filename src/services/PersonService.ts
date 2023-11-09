import { personApi } from '@/services/api/apis'
import router, { RouterPages, redirectToLoginPageIfNotLogged, redirectOnApiError } from '@/router'
import type { Person } from '@/model/model'
import axios, { AxiosError, type AxiosResponse } from 'axios'
import { usePersonStore } from '@/stores/personStore'


interface LoginResponse {
  name: string
  unlockingDate: number
}

export default class PersonService {
  public static async createSession(
    email: string,
    password: string
  ): Promise<LoginResponse> {
    let data
    let response
    try {
      response = await personApi.createSession(email, password)
      console.log(response)
      data = response.data
      usePersonStore().updateLogged()
      router.push(RouterPages.home)
    } catch (exception) {
      console.log(exception)
      if (axios.isAxiosError(exception)) {
        response = exception.response
        data = response ? response.data : {}
      }
    }
    return JSON.parse(data)
  }

  public static async createPerson(
    name: string,
    email: string,
    password: string,
    profile: string
  ) {
    try {
      await personApi.createPerson(name, password, email, profile)
      usePersonStore().updateLogged()
      router.push(RouterPages.home)
    } catch (exception) {
      if (axios.isAxiosError(exception)) {
        alert(exception.response?.data)
      }
    }
  }

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

  public static async deleteSession() {
    await personApi.deleteSession()
    usePersonStore().updateLogged()
    redirectToLoginPageIfNotLogged()
  }
}
