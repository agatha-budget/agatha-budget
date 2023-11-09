import { defineStore } from 'pinia'
import type { Budget, Account, Category, MasterCategory } from '@/model/model'
import BudgetService from '@/services/BudgetService'
import AccountService from '@/services/AccountService'
import CategoryService from '@/services/CategoryService'
import MasterCategoryService from '@/services/MasterCategoryService'

export const useBudgetStore = defineStore('budget', {
  state: () => ({
    budget: null as Budget | null,
    accounts: [] as Account[],
    categories: [] as Category[],
    masterCategories: [] as MasterCategory[],
    storeLoaded: false
  }),
  actions: {
    reset() {
      this.budget = null
      this.accounts = []
      this.masterCategories = []
      this.categories = []
      this.storeLoaded = false
    },
    async init() {
      this.budget = await BudgetService.getDefaultBudget()
      this.onBudgetUpdate()
    },
    async onBudgetUpdate() {
      // to be improved in case one of the update fail
      await Promise.all([
        this.updateAccounts(),
        this.updateMasterCategories(),
        this.updateCategories()
      ])
      this.storeLoaded = true
    },
    async updateAccounts() {
      if (this.budget) {
        this.accounts = await AccountService.getAccounts(this.budget)
      }
    },
    async updateCategories() {
      if (this.budget) {
        this.categories = await CategoryService.getCategories(this.budget)
      }
    },
    async updateMasterCategories() {
      if (this.budget) {
        this.masterCategories = await MasterCategoryService.getMasterCategories(this.budget)
      }
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
      return categories
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
    }
  }
})
