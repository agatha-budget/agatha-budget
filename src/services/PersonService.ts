import { StoreState } from '@/store/index'
import { Store } from 'vuex'
import { personApi } from '@/services/api/apis'
import router, { RouterPages, redirectOnApiError, redirectToLoginPageIfNotLogged } from '@/router'
import axios from 'axios'

interface LoginResponse {
    name: string;
    unlockingDate: number;
}

export default class PersonService {
  public static async createSession (store: Store<StoreState>, email: string, password: string): Promise<LoginResponse> {
    let data
    let response
    try {
      response = await personApi.createSession(email, password)
      data = response.data
      store.dispatch('updateLogged')
      router.push(RouterPages.home)
    } catch (exception) {
      if (axios.isAxiosError(exception)) {
        response = exception.response
        data = (response) ? response.data : {}
      }
    }
    return JSON.parse(data)
  }

  public static async createPerson (store: Store<StoreState>, name: string, email: string, password: string, profile: string) {
    try {
      await (personApi.createPerson(name, password, email, profile))
      store.dispatch('updateLogged')
      router.push(RouterPages.home)
    } catch (exception) {
      if (axios.isAxiosError(exception)) {
        alert(exception.response?.data)
      }
    }
  }

  public static async redirectToBillingPortalUrl (store: Store<StoreState>) {
    try {
      const billingPortalUrl = (await (personApi.createBillingPortalSession())).data
      window.location.href = billingPortalUrl
    } catch (exception) {
      if (axios.isAxiosError(exception)) {
        alert(exception.response?.data)
      }
    }
  }

  public static async deleteSession (store: Store<StoreState>) {
    const response = await personApi.deleteSession()
    store.dispatch('updateLogged')
    redirectToLoginPageIfNotLogged(store)
  }
}
