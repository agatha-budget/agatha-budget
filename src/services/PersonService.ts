import { State } from './../store/index'
import { Store } from 'vuex'
import SuperTokensRequest from 'supertokens-website/axios'
import { personApi } from './../api/api'

class PersonService {
  public async createSession (store: Store<State>, email: string, password: string, options?: any) {
    await personApi.createSession(email, password, options)
    const userId = SuperTokensRequest.getUserId()
    store.dispatch('setUserId', userId)
  }

  public async deleteSession (options?: any) {
    await personApi.deleteSession(options)
  }
}

export const personService = new PersonService()
