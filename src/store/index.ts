import { InjectionKey } from 'vue'
import { createStore, Store, useStore as baseUseStore } from 'vuex'
import SuperTokensRequest from 'supertokens-website/axios'
import { Budget, AccountList, CategoryList, MasterCategoryList, CategoryByMasterCategoryList } from '@/model/model'
import StoreHandler from './StoreHandler'

export interface StoreState {
  logged: boolean;
  budget: Budget | null;
  accounts: AccountList;
  categories: CategoryList;
  masterCategories: MasterCategoryList;
  orderedMasterCategoriesId: string[];
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
    categories: {},
    masterCategories: {},
    orderedMasterCategoriesId: [],
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
    },
    updateCategories (state, categories: CategoryList) {
      state.categories = categories
      state.nonArchivedCategoriesIdByMasterCategoriesId = StoreHandler.createCategoryIdListByMasterCategoryId(categories, state.masterCategories)
      state.archivedCategoriesIdByMasterCategoriesId = StoreHandler.createCategoryIdListByMasterCategoryId(categories, state.masterCategories, true)
    },
    updateMasterCategories (state, masterCategories: MasterCategoryList) {
      state.masterCategories = masterCategories
      state.orderedMasterCategoriesId = StoreHandler.orderMasterCategories(masterCategories)
      state.nonArchivedCategoriesIdByMasterCategoriesId = StoreHandler.createCategoryIdListByMasterCategoryId(state.categories, masterCategories)
      state.archivedCategoriesIdByMasterCategoriesId = StoreHandler.createCategoryIdListByMasterCategoryId(state.categories, masterCategories, true)
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
