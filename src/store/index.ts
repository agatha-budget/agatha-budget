import { InjectionKey } from 'vue'
import { createStore, Store, useStore as baseUseStore } from 'vuex'
import SuperTokensRequest from 'supertokens-website/axios'
import { Budget, Account } from '@/model/model'

export interface StoreState {
  logged: boolean;
  budget: Budget;
  accounts: Account[];
  css: string;
}

export const key: InjectionKey<Store<StoreState>> = Symbol('injectionKey')

export const store = createStore<StoreState>({
  state: {
    logged: SuperTokensRequest.doesSessionExist(),
    budget: { id: '', name: '' },
    accounts: [],
    css: 'blue'
  },
  mutations: {
    updateLogged (state) {
      state.logged = SuperTokensRequest.doesSessionExist()
    },
    updateBudget (state, budget: Budget) {
      state.budget = budget
    },
    updateAccounts (state, accounts: Account[]) {
      state.accounts = accounts
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
    },
    updateAccounts (context, accounts: Account[]) {
      context.commit('updateAccounts', accounts)
      console.log(this.state)
    }
  },
  modules: {
  }
})

export function useStore (): Store<StoreState> {
  return baseUseStore(key)
}
