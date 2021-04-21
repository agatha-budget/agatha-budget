import { InjectionKey } from 'vue'
import { createStore, Store, useStore as baseUseStore } from 'vuex'
import SuperTokensRequest from 'supertokens-website/axios'
import { Budget } from '@/model/model'

export interface StoreState {
  logged: boolean;
  budget: Budget;
  css: string;
}

export const key: InjectionKey<Store<StoreState>> = Symbol('injectionKey')

export const store = createStore<StoreState>({
  state: {
    logged: SuperTokensRequest.doesSessionExist(),
    budget: { id: '', name: '' },
    css: 'blue'
  },
  mutations: {
    updateLogged (state) {
      state.logged = SuperTokensRequest.doesSessionExist()
    },
    updateBudget (state, budget: Budget) {
      state.budget = budget
    }
  },
  actions: {
    updateLogged (context) {
      context.commit('updateLogged')
      console.log(this.state)
    },
    updateBudget (context, budget: Budget) {
      context.commit('updateBudget', budget)
      console.log(this.state)
    }
  },
  modules: {
  }
})

export function useStore (): Store<StoreState> {
  return baseUseStore(key)
}
