import { State } from '@/store/index'
import { Store } from 'vuex'
import { personApi } from '@/services/api/apis'
import router from '@/router'

class PersonService {
  public async createSession (store: Store<State>, email: string, password: string, options?: any) {
    await personApi.createSession(email, password, options)
    store.dispatch('updateLogged')
    router.push('/')
  }

  public async deleteSession (store: Store<State>, options?: any) {
    await personApi.deleteSession(options)
    store.dispatch('updateLogged')
    router.push('/login')
  }
}

export const personService = new PersonService()
