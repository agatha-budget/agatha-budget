import { defineStore } from 'pinia'
import type { Person } from '@/model/model'
import { useBudgetStore } from './budgetStore'
import PersonService from '@/services/PersonService'

export const usePersonStore = defineStore('person', {
  state: () => ({
    logged: false,
    person: null as Person | null,
    css: 'default'
  }),
  actions: {
    reset() {
      this.logged = false
      this.person = null
      this.css = 'default'
    },
    updateLogged() {
      const budgetStore = useBudgetStore()
      this.logged = true // SuperTokensRequest.doesSessionExist() // TODO
      if (this.logged) {
        this.init()
        budgetStore.init()
      } else {
        this.reset()
        budgetStore.reset()
      }
    },
    async init() {
      this.person = await PersonService.getPerson()
      useBudgetStore().init()
    }
  }
})
