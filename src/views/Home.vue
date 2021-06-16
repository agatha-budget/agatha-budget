<template >
  <div :class="this.$store.state.css">
    <div class="home row">
      <div class="col-md-5 offset-md-2">
        <ul id="actionsList" class="list-group list-group-horizontal">
          <li><button class="btn fas fa-chart-pie" :title="$t('GRAPH_AND_REPORT')"/></li>
          <li><button class="btn fas fa-cog" :title="$t('PREFERENCES')"/></li>
          <li><button v-on:click="logout" class="btn fas fa-sign-out-alt" :title="$t('LOGOUT')"/></li>
        </ul>
        <BudgetCmpt :month="this.currentMonth" />
      </div>
      <div class="col-md-2 offset-md-1">
      <AccountsWidget/>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { useStore } from '@/store/index'
import StoreHandler from '@/store/StoreHandler'
import router, { redirectToLoginPageIfNotLogged } from '@/router'
import BudgetCmpt from '@/components/BudgetCmpt.vue' // @ is an alias to /src
import AccountsWidget from '@/components/AccountsWidget.vue'
import PersonService from '@/services/PersonService'
import Time from '@/utils/Time'

export default defineComponent({
  name: 'Home',
  created: async function () {
    redirectToLoginPageIfNotLogged(this.$store)
    StoreHandler.initStore(this.$store)
  },
  components: {
    BudgetCmpt,
    AccountsWidget
  },
  data () {
    return {
      currentMonth: Time.getCurrentMonth()
    }
  },
  setup () {
    if (!useStore().state.logged) {
      router.push('/login')
    }
  },
  methods: {
    logout () {
      PersonService.deleteSession(this.$store)
    }
  }
})
</script>
