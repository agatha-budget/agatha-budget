import { Account, Budget } from '@/model/model'
import AccountService from '@/services/AccountService'
import BudgetService from '@/services/BudgetService'
import { StoreState } from '@/store/index'
import { Store } from 'vuex'

export default class StoreHandler {
  public static async initStore (store: Store<StoreState>) {
    await StoreHandler.initBudget(store)
    StoreHandler.updateAccounts(store)
  }

  public static async updateAccounts (store: Store<StoreState>) {
    if (store.state.budget.id) {
      AccountService.getAccounts(store.state.budget).then(
        (accounts: Account[]) => {
          store.dispatch('updateAccounts', accounts)
        }
      )
    }
  }

  public static async initBudget (store: Store<StoreState>) {
    if (!store.state.budget.id) {
      BudgetService.getDefaultBudget().then(
        (budget: Budget) => {
          store.dispatch('updateBudget', budget)
        }
      )
    }
  }
}
