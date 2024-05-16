import type { Person } from '@/model/model'
import PersonService from '@/services/PersonService'
import { defineStore } from 'pinia'
import { useBudgetStore } from './budgetStore'

export const usePersonStore = defineStore('person', {
  state: () => ({
    person: null as Person | null,
    css: 'default'
  }),
  actions: {
    reset() {
      this.person = null
      this.css = 'default'
    },
    async init() {
      if (this.person === null) {
        this.person = await PersonService.getPerson()
      }      
      useBudgetStore().init()
    }
  }
})
