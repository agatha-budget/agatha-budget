import { InjectionKey } from 'vue'
import { createStore, Store, useStore as baseUseStore } from 'vuex'

export interface State {
  logged: boolean;
}

export const key: InjectionKey<Store<State>> = Symbol('injectionKey')

export const store = createStore<State>({
  state: {
    logged: false
  },
  mutations: {
    setToLogged (state) {
      console.log('was called')
      state.logged = true
      console.log(state.logged)
    },
    setToUnlogged (state) {
      state.logged = false
    }
  },
  actions: {
    login (context) {
      context.commit('setToLogged')
    },
    logout (context) {
      context.commit('setToUnlogged')
    }
  },
  modules: {
  }
})

export function useStore (): Store<State> {
  return baseUseStore(key)
}
