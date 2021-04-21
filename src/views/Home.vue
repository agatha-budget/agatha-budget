<template >
  <div :class="this.$store.state.css">
    <div class="home row">
      <BudgetCmpt month="FEBRUARY" class="col-md-4 offset-md-2"/>
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
import StoreHandler from '@/store/StoreHandler'
import router from '@/router'
import BudgetCmpt from '@/components/BudgetCmpt.vue' // @ is an alias to /src
import AccountsWidget from '@/components/AccountsWidget.vue'
import PersonService from '@/services/PersonService'

export default defineComponent({
  name: 'Home',
  created: async function () {
    StoreHandler.initStore(this.$store)
  },
  components: {
    BudgetCmpt,
    AccountsWidget
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
