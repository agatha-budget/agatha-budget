<template >
  <div :class="this.$store.state.css">
    <div class="home row">
      <div v-if="this.$store.state.storeLoaded" class="row">
        <div class="col-sm-8 offset-sm-2 col-md-6 offset-md-3 col-xl-5 offset-xl-2">
          <BudgetCmpt :month="this.currentMonth" />
        </div>
        <div class="col-8 offset-2 col-sm-6 offset-sm-3 col-md-4 offset-md-4 col-xl-2 offset-xl-1">
          <AccountsWidget/>
          <ul id="actionsList" class="list-group list-group-horizontal d-flex justify-content-center">
          <li><button class="btn fas fa-chart-line disabled" :title="$t('GRAPH_AND_REPORT')"/></li>
          <li><button class="btn fas fa-cog disabled" :title="$t('PREFERENCES')"/></li>
          <li><button v-on:click="logout" class="btn fas fa-sign-out-alt" :title="$t('LOGOUT')"/></li>
        </ul>
        </div>
      </div>
    <div v-else>
        <Loader class="loader"/>
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
import Loader from '@/components/utils/Loader.vue'

export default defineComponent({
  name: 'Home',
  beforeCreate: async function () {
    redirectToLoginPageIfNotLogged(this.$store)
  },
  created: async function () {
    StoreHandler.initStore(this.$store)
  },
  components: {
    BudgetCmpt,
    AccountsWidget,
    Loader
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
