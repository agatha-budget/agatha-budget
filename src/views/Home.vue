<template >
  <div :class="this.css">
    <div class="home row">
      <BudgetCmpt month="FEBRUARY" :budget="budget" class="col-md-4 offset-md-2"/>
      <div class="col-md-2 offset-md-2">
      <AccountsWidget/>
      <ul id="actionsList">
        <li><button>{{ $t("GRAPH_AND_REPORT") }}</button></li>
        <li><button>{{ $t("PREFERENCES") }}</button></li>
        <li><button v-on:click="logout">{{ $t("LOGOUT") }}</button></li>
      </ul>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { useStore } from '@/store/index'
import router from '@/router'
import BudgetCmpt from '@/components/BudgetCmpt.vue' // @ is an alias to /src
import AccountsWidget from '@/components/AccountsWidget.vue'
import { personService } from '@/services/PersonService'
import { budgetService } from '@/services/BudgetService'

interface HomeData {
    css: string;
}

export default defineComponent({
  name: 'Home',
  created: async function () {
    this.getDefaultBudget()
  },
  components: {
    BudgetCmpt,
    AccountsWidget
  },
  data (): HomeData {
    return {
      css: 'blue'
    }
  },
  setup () {
    if (!useStore().state.logged) {
      router.push('/login')
    }
  },
  methods: {
    logout () {
      personService.deleteSession(this.$store)
    },
    async getDefaultBudget () {
      budgetService.getDefaultBudget(this.$store)
    }
  }
})
</script>
