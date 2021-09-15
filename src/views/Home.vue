<template >
  <div :class="this.$store.state.css">
    <div class="home row" style="margin-top: 20px">
      <div class="row">
        <div class="col-md-5 offset-md-2">
          <BudgetCmpt :month="this.currentMonth" />
        </div>
        <div class="col-md-2 offset-md-1">
          <AccountsWidget/>
          <ul id="actionsList" class="list-group list-group-horizontal d-flex justify-content-center">
          <li><button class="btn fas fa-chart-line disabled" :title="$t('GRAPH_AND_REPORT')"/></li>
          <li><button class="btn fas fa-cog disabled" :title="$t('PREFERENCES')"/></li>
          <li><button v-on:click="logout" class="btn fas fa-sign-out-alt" :title="$t('LOGOUT')"/></li>
        </ul>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import StoreHandler from '@/store/StoreHandler'
import { redirectToLoginPageIfNotLogged } from '@/router'
import BudgetCmpt from '@/components/BudgetCmpt.vue' // @ is an alias to /src
import AccountsWidget from '@/components/AccountsWidget.vue'
import PersonService from '@/services/PersonService'
import Time from '@/utils/Time'

export default defineComponent({
  name: 'Home',
  beforeCreate: async function () {
    redirectToLoginPageIfNotLogged(this.$store)
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
  methods: {
    logout () {
      PersonService.deleteSession(this.$store)
    }
  }
})
</script>
