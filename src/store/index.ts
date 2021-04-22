import { InjectionKey } from 'vue'
import { createStore, Store, useStore as baseUseStore } from 'vuex'
import SuperTokensRequest from 'supertokens-website/axios'
import { Budget, AccountList, CategoryList } from '@/model/model'
import StoreHandler from './StoreHandler'

export interface StoreState {
  logged: boolean;
  budget: Budget | null;
  accounts: AccountList;
  categories: CategoryList;
  css: string;
}

export const key: InjectionKey<Store<StoreState>> = Symbol('injectionKey')

export const store = createStore<StoreState>({
  state: {
    logged: SuperTokensRequest.doesSessionExist(),
    budget: null,
    accounts: {},
    categories: {},
    css: 'blue'
  },
  mutations: {
    updateLogged (state) {
      state.logged = SuperTokensRequest.doesSessionExist()
    },
    updateBudget (state, budget: Budget) {
      state.budget = budget
      StoreHandler.updateAccounts(store)
      StoreHandler.updateCategories(store)
    },
    updateAccounts (state, accounts: AccountList) {
      state.accounts = accounts
    },
    updateCategories (state, categories: CategoryList) {
      state.categories = categories
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
    updateAccounts (context, accounts: AccountList) {
      context.commit('updateAccounts', accounts)
      console.log(this.state)
    },
    updateCategories (context, categories: CategoryList) {
      context.commit('updateCategories', categories)
      console.log(this.state)
    }
  },
  modules: {
  }
})

export function useStore (): Store<StoreState> {
  return baseUseStore(key)
}
