import type { Account, Budget, Category, MasterCategory } from '@/model/model'
import AccountService from '@/services/AccountService'
import BudgetService from '@/services/BudgetService'
import CategoryService from '@/services/CategoryService'
import MasterCategoryService from '@/services/MasterCategoryService'
import { useOperationStore } from '@/stores/operationStore'
import { defineStore } from 'pinia'

export const useBudgetStore = defineStore('budget', {
  state: () => ({
    budget: null as Budget | null,
    accounts: [] as Account[],
    categories: [] as Category[],
    masterCategories: [] as MasterCategory[],
  }),
  actions: {
    reset() {
      this.budget = null
      this.accounts = []
      this.masterCategories = []
      this.categories = []
    },
    async init() {
      if (this.budget === null) {
        this.budget = await BudgetService.getDefaultBudget()
        this.onBudgetUpdate()
      }
    },
    async onBudgetUpdate() {
      // to be improved in case one of the update fail
      await Promise.all([
        this.updateAccounts(),
        this.updateMasterCategories(),
        this.updateCategories()
      ])
    },
    async updateAccounts(withOperation: boolean = true) {
      if (this.budget) {
        let res = await AccountService.getAccounts(this.budget)
        if (res.isOk()) {
          this.accounts = res.value
        }
        if (withOperation){
          useOperationStore().retrieveOperations(this.accounts)
        }
      }
    },
    async updateCategories() {
      if (this.budget) {
        let res = await CategoryService.getCategories(this.budget)
        if (res.isOk()) {
          this.categories = res.value
        }
      }
    },
    async updateMasterCategories() {
      if (this.budget) {
        let res = await MasterCategoryService.getMasterCategories(this.budget)
        if (res.isOk()) {
          this.masterCategories = res.value
        }
      }
    },
    getOrderedMasterCategory(): MasterCategory[] {
      return this.masterCategories.sort(function(a,b) {
        return (a.name < b.name) ? -1 : 1 
      })
    },
    getMasterCategoryById(masterCategoryId: string): MasterCategory | null {
      for (const masterCategory of this.masterCategories) {
        if (masterCategory.id === masterCategoryId) {
          return masterCategory
        }
      }
      return null
    },
    getCategoryById(categoryId: string): Category | null {
      for (const category of this.categories) {
        if (category.id === categoryId) {
          return category
        }
      }
      return null
    },
    getCategoriesByMasterCategory(masterCategory: MasterCategory, archived: boolean): Category[] {
      const categories: Category[] = []
      for (const category of this.categories) {
        if (category.masterCategoryId === masterCategory.id && category.archived === archived) {
          categories.push(category)
        }
      }
      return this.orderCategory(categories)
    },
    getCategoriesByArchivedStatus(archived: boolean): Category[] {
      const categories: Category[] = []
      for (const category of this.categories) {
        if (category.archived === archived) {
          categories.push(category)
        }
      }
      return categories
    },
    getAccountById(accountId: string): Account | null {
      for (const account of this.accounts) {
        if (account.id === accountId) {
          return account
        }
      }
      return null
    },
    orderCategory(categories: Category[]): Category[] {
      return categories.sort(function(a,b) {
        return (a.name < b.name) ? -1 : 1 
      })
    }
  }
})
