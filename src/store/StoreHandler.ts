import { Budget, AccountList, CategoryList, MasterCategoryList, CategoryByMasterCategoryList } from '@/model/model'
import AccountService from '@/services/AccountService'
import BudgetService from '@/services/BudgetService'
import CategoryService from '@/services/CategoryService'
import MasterCategoryService from '@/services/MasterCategoryService'
import { StoreState } from '@/store/index'
import { Store } from 'vuex'

export default class StoreHandler {
  public static async initStore (store: Store<StoreState>) {
    await StoreHandler.initBudget(store)
  }

  public static async updateOnBudgetChange (store: Store<StoreState>) {
    this.updateAccounts(store)
    this.updateMasterCategories(store)
    this.updateCategories(store)
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

  public static async updateMasterCategories (store: Store<StoreState>) {
    if (store.state.budget) {
      MasterCategoryService.getMasterCategories(store.state.budget).then(
        (masterCategories: MasterCategoryList) => {
          store.dispatch('updateMasterCategories', masterCategories)
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

  public static createCategoryIdListByMasterCategoryId (categoriesList: CategoryList, masterCategoriesList: MasterCategoryList, wantedArchiveStatus = false): CategoryByMasterCategoryList {
    const data: CategoryByMasterCategoryList = {}
    for (const categoryId of Object.keys(categoriesList)) {
      const masterCategoryId = categoriesList[categoryId].masterCategoryId
      /* check :
        - isn't a universal category without masterCategory (like income or transfert)
        - is or isn't archived depending of the wanted status
        - the masterCategories list is up to date (to prevent "is undefined" when rendering a masterCategoryCmpt )
      */
      if (masterCategoryId && categoriesList[categoryId].archived === wantedArchiveStatus && Object.keys(masterCategoriesList).includes(masterCategoryId)) {
        if (!data[masterCategoryId]) {
          data[masterCategoryId] = []
        }
        data[masterCategoryId].push(categoryId)
      }
    }
    return this.orderCategories(data, categoriesList)
  }

  public static orderCategories (categoryByMasterCategoryList: CategoryByMasterCategoryList, categoriesList: CategoryList) {
    for (const masterCategoryId in categoryByMasterCategoryList) {
      const sorted = categoryByMasterCategoryList[masterCategoryId].sort((a, b) => (categoriesList[a].name.toLowerCase() <= categoriesList[b].name.toLowerCase()) ? -1 : 1)
      categoryByMasterCategoryList[masterCategoryId] = sorted
    }
    return categoryByMasterCategoryList
  }

  public static orderMasterCategories (masterCategoriesList: MasterCategoryList): string[] {
    return Object.keys(masterCategoriesList).sort((a, b) => (masterCategoriesList[a].name.toLowerCase() <= masterCategoriesList[b].name.toLowerCase()) ? -1 : 1)
  }
}
