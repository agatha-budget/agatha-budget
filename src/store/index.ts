import { InjectionKey } from 'vue'
import { createStore, Store, useStore as baseUseStore } from 'vuex'
import SuperTokensRequest from 'supertokens-website/axios'

export interface State {
  logged: boolean;
}

export const key: InjectionKey<Store<State>> = Symbol('injectionKey')

export const store = createStore<State>({
  state: {
    logged: false
  },
  mutations: {
    updateLogged (state) {
      state.logged = SuperTokensRequest.doesSessionExist()
    }
  },
  actions: {
    updateLogged (context) {
      context.commit('updateLogged')
    }
  },
  modules: {
  }
})

export function useStore (): Store<State> {
  return baseUseStore(key)
}
