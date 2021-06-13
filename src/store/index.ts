import { InjectionKey } from 'vue'
import { createStore, Store, useStore as baseUseStore } from 'vuex'
import SuperTokensRequest from 'supertokens-website/axios'
import { Budget, AccountList, CategoryList, MasterCategoryList, CategoryByMasterCategoryList } from '@/model/model'
import StoreHandler from './StoreHandler'

export interface StoreState {
  logged: boolean;
  budget: Budget | null;
  accounts: AccountList;
  totalOnAccounts: number;
  categories: CategoryList;
  masterCategories: MasterCategoryList;
  nonArchivedCategoriesIdByMasterCategoriesId: CategoryByMasterCategoryList;
  archivedCategoriesIdByMasterCategoriesId: CategoryByMasterCategoryList;
  css: string;
}

export const key: InjectionKey<Store<StoreState>> = Symbol('injectionKey')

export const store = createStore<StoreState>({
  state: {
    logged: SuperTokensRequest.doesSessionExist(),
    budget: null,
    accounts: {},
    totalOnAccounts: 0,
    categories: {},
    masterCategories: {},
    nonArchivedCategoriesIdByMasterCategoriesId: {},
    archivedCategoriesIdByMasterCategoriesId: {},
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
    updateAccounts (state, accounts: AccountList) {
      state.accounts = accounts
      let total = 0
      for (const accountId in accounts) {
        total += accounts[accountId].amount
      }
      state.totalOnAccounts = total
    },
    updateCategories (state, categories: CategoryList) {
      state.categories = categories
      state.nonArchivedCategoriesIdByMasterCategoriesId = StoreHandler.createNonArchivedCategoryIdListByMasterCategoryId(categories, state.masterCategories)
      state.archivedCategoriesIdByMasterCategoriesId = StoreHandler.createArchivedCategoryIdListByMasterCategoryId(categories, state.masterCategories)
    },
    updateMasterCategories (state, masterCategories: MasterCategoryList) {
      state.masterCategories = masterCategories
      state.nonArchivedCategoriesIdByMasterCategoriesId = StoreHandler.createNonArchivedCategoryIdListByMasterCategoryId(state.categories, masterCategories)
      state.archivedCategoriesIdByMasterCategoriesId = StoreHandler.createArchivedCategoryIdListByMasterCategoryId(state.categories, masterCategories)
    }
  },
  actions: {
    updateLogged (context) {
      context.commit('updateLogged')
    },
    updateBudget (context, budget: Budget) {
      context.commit('updateBudget', budget)
    },
    updateAccounts (context, accounts: AccountList) {
      context.commit('updateAccounts', accounts)
    },
    updateCategories (context, categories: CategoryList) {
      context.commit('updateCategories', categories)
    },
    updateMasterCategories (context, masterCategories: MasterCategoryList) {
      context.commit('updateMasterCategories', masterCategories)
    }

  },
  modules: {
  }
})

export function useStore (): Store<StoreState> {
  return baseUseStore(key)
}
