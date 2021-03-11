import { State } from '@/store/index'
import { Store } from 'vuex'
import { personApi } from '@/services/api/apis'
import router from '@/router'
import axios from 'axios'

interface LoginResponse {
    name: string;
    unlockingDate: number;
}

class PersonService {
  public async createSession (store: Store<State>, email: string, password: string, options?: any): Promise<LoginResponse> {
    let data
    let response
    try {
      response = await personApi.createSession(email, password, options)
      data = response.data
      store.dispatch('updateLogged')
      router.push('/')
    } catch (exception) {
      if (axios.isAxiosError(exception)) {
        response = exception.response
        data = (response) ? response.data : {}
      }
    }
    return JSON.parse(data)
  }

  public async deleteSession (store: Store<State>, options?: any) {
    await personApi.deleteSession(options)
    store.dispatch('updateLogged')
    router.push('/login')
  }
}

export const personService = new PersonService()
