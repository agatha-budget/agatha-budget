import { Budget, AccountList, CategoryList } from '@/model/model'
import AccountService from '@/services/AccountService'
import BudgetService from '@/services/BudgetService'
import CategoryService from '@/services/CategoryService'
import { StoreState } from '@/store/index'
import { Store } from 'vuex'

export default class StoreHandler {
  public static async initStore (store: Store<StoreState>) {
    await StoreHandler.initBudget(store)
  }

  public static async updateAccounts (store: Store<StoreState>) {
    if (store.state.budget) {
      AccountService.getAccounts(store.state.budget).then(
        (accounts: AccountList) => {
          store.dispatch('updateAccounts', accounts)
        }
      )
    }
  }

  public static async updateCategories (store: Store<StoreState>) {
    if (store.state.budget) {
      CategoryService.getCategories(store.state.budget).then(
        (categories: CategoryList) => {
          store.dispatch('updateCategories', categories)
        }
      )
    }
  }

  public static async initBudget (store: Store<StoreState>) {
    if (!store.state.budget) {
      BudgetService.getDefaultBudget().then(
        (budget: Budget) => {
          store.dispatch('updateBudget', budget)
        }
      )
    }
  }
}
