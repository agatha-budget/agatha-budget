import { Person, Budget, Account, Category, MasterCategory } from '@/model/model'
import AccountService from '@/services/AccountService'
import BudgetService from '@/services/BudgetService'
import CategoryService from '@/services/CategoryService'
import MasterCategoryService from '@/services/MasterCategoryService'
import PersonService from '@/services/PersonService'
import { StoreState } from '@/store/index'
import { Store } from 'vuex'

export default class StoreHandler {
  public static async initStore (store: Store<StoreState>) {
    await this.initBudget(store)
    await this.initPerson(store)
  }

  public static resetStore (store: Store<StoreState>) {
    store.dispatch('updateBudget', null)
    store.dispatch('updateAccounts', [])
    store.dispatch('updateCategories', [])
    store.dispatch('updateMasterCategories', [])
    store.dispatch('updateStoreLoaded', false)
    store.dispatch('updatePerson', null)
  }

  public static async updateOnBudgetChange (store: Store<StoreState>) {
    this.updateAccounts(store)
    this.updateMasterCategories(store)
    this.updateCategories(store)
    store.dispatch('updateStoreLoaded', true)
  }

  public static async updateAccounts (store: Store<StoreState>) {
    if (store.state.budget) {
      AccountService.getAccounts(store.state.budget).then(
        (accounts: Account[]) => {
          store.dispatch('updateAccounts', accounts)
        }
      )
    }
  }

  public static async updateCategories (store: Store<StoreState>) {
    if (store.state.budget) {
      CategoryService.getCategories(store.state.budget).then(
        (categories: Category[]) => {
          store.dispatch('updateCategories', categories)
        }
      )
    }
  }

  public static async updateMasterCategories (store: Store<StoreState>) {
    if (store.state.budget) {
      MasterCategoryService.getMasterCategories(store.state.budget).then(
        (masterCategories: MasterCategory[]) => {
          store.dispatch('updateMasterCategories', masterCategories)
        }
      )
    }
  }

  public static async initBudget (store: Store<StoreState>) {
    BudgetService.getDefaultBudget().then(
      (budget: Budget) => {
        store.dispatch('updateBudget', budget)
      }
    )
  }

  public static getMasterCategoryById (store: Store<StoreState>, masterCategoryId: string): MasterCategory | null {
    for (const masterCategory of store.state.masterCategories) {
      if (masterCategory.id === masterCategoryId) {
        return masterCategory
      }
    }
    return null
  }

  public static getCategoryById (store: Store<StoreState>, categoryId: string): Category | null {
    for (const category of store.state.categories) {
      if (category.id === categoryId) {
        return category
      }
    }
    return null
  }

  public static getCategoriesByMasterCategory (store: Store<StoreState>, masterCategory: MasterCategory, archived: boolean): Category[] {
    const categories: Category[] = []
    for (const category of store.state.categories) {
      if (category.masterCategoryId === masterCategory.id && category.archived === archived) {
        categories.push(category)
      }
    }
    return categories
  }

  public static getCategoriesByArchivedStatus (store: Store<StoreState>, archived: boolean): Category[] {
    const categories: Category[] = []
    for (const category of store.state.categories) {
      if (category.archived === archived) {
        categories.push(category)
      }
    }
    return categories
  }

  public static getAccountById (store: Store<StoreState>, accountId: string): Account | null {
    for (const account of store.state.accounts) {
      if (account.id === accountId) {
        return account
      }
    }
    return null
  }

  public static initPerson (store: Store<StoreState>) {
    PersonService.getPerson().then(
      (person: Person) => {
        store.dispatch('updatePerson', person)
      }
    )
  }
}
