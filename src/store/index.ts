import { InjectionKey } from 'vue'
import { createStore, Store, useStore as baseUseStore } from 'vuex'
import SuperTokensRequest from 'supertokens-website/axios'
import { Budget, Account, Category, MasterCategory } from '@/model/model'
import StoreHandler from './StoreHandler'

export interface StoreState {
  logged: boolean;
  budget: Budget | null;
  accounts: Account[];
  categories: Category[];
  masterCategories: MasterCategory[];
  css: string;
}

export const key: InjectionKey<Store<StoreState>> = Symbol('injectionKey')

export const store = createStore<StoreState>({
  state: {
    logged: SuperTokensRequest.doesSessionExist(),
    budget: null,
    accounts: [],
    categories: [],
    masterCategories: [],
    css: 'blue'
  },
  mutations: {
    updateLogged (state) {
      state.logged = SuperTokensRequest.doesSessionExist()
    },
    updateBudget (state, budget: Budget) {
      state.budget = budget
      StoreHandler.updateOnBudgetChange(store)
    },
    updateAccounts (state, accounts: Account[]) {
      state.accounts = accounts
    },
    updateCategories (state, categories: Category[]) {
      state.categories = categories.sort((a, b) => (a.name.toLowerCase() <= b.name.toLowerCase() ? -1 : 1))
    },
    updateMasterCategories (state, masterCategories: MasterCategory[]) {
      state.masterCategories = masterCategories.sort((a, b) => (a.name.toLowerCase() <= b.name.toLowerCase() ? -1 : 1))
    }
  },
  actions: {
    updateLogged (context) {
      context.commit('updateLogged')
    },
    updateBudget (context, budget: Budget) {
      context.commit('updateBudget', budget)
    },
    updateAccounts (context, accounts: Account[]) {
      context.commit('updateAccounts', accounts)
    },
    updateCategories (context, categories: Category[]) {
      context.commit('updateCategories', categories)
    },
    updateMasterCategories (context, masterCategories: MasterCategory[]) {
      context.commit('updateMasterCategories', masterCategories)
    }

  },
  modules: {
  }
})

export function useStore (): Store<StoreState> {
  return baseUseStore(key)
}
