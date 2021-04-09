import { StoreState } from '@/store/index'
import { Store } from 'vuex'
import { personApi } from '@/services/api/apis'
import router, { RouterPages } from '@/router'
import axios from 'axios'

interface LoginResponse {
    name: string;
    unlockingDate: number;
}

class PersonService {
  public async createSession (store: Store<StoreState>, email: string, password: string): Promise<LoginResponse> {
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

  public async deleteSession (store: Store<StoreState>) {
    await personApi.deleteSession()
    store.dispatch('updateLogged')
    router.push(RouterPages.login)
  }
}

export const personService = new PersonService()
