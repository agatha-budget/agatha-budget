import { InjectionKey } from 'vue'
import { createStore, Store, useStore as baseUseStore } from 'vuex'

export interface State {
  logged: boolean;
  userId: string;
}

export const key: InjectionKey<Store<State>> = Symbol('injectionKey')

export const store = createStore<State>({
  state: {
    logged: false,
    userId: 'none'
  },
  mutations: {
    setToLogged (state) {
      console.log('was called')
      state.logged = true
      console.log(state.logged)
    },
    setToUnlogged (state) {
      state.logged = false
    },
    setUserId (state, userId) {
      state.userId = userId
    }
  },
  actions: {
    login (context) {
      context.commit('setToLogged')
    },
    logout (context) {
      context.commit('setToUnlogged')
    },
    setUserId (context, userId) {
      console.log('was here')
      context.commit('setUserId', userId)
    }
  },
  modules: {
  }
})

export function useStore (): Store<State> {
  return baseUseStore(key)
}
