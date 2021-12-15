<template >
  <div :class="this.$store.state.css">
    <div class="home row">
      <div v-if="this.$store.state.storeLoaded" class="row">
        <div class="col-12 offset-0 col-sm-10 offset-sm-1 col-md-8 offset-md-2 col-lg-6 offset-lg-1 col-xl-5 offset-xl-2 col-xxl-5 offset-xxl-2">
          <BudgetCmpt :month="this.currentMonth" />
        </div>
        <div class="col-8 offset-2 col-sm-6 offset-sm-3 col-md-4 offset-md-4 col-lg-3 offset-lg-1 col-xl-3 offset-xl-1 col-xxl-2 offset-xxl-1">
          <div class="accountWidgetAtHome">
            <AccountsWidget/>
          </div>
          <div class="buttonsAtHome">
            <BtnMenu/>
          </div>
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
import router, { RouterPages, redirectToLoginPageIfNotLogged } from '@/router'
import BudgetCmpt from '@/components/BudgetCmpt.vue' // @ is an alias to /src
import AccountsWidget from '@/components/AccountsWidget.vue'
import PersonService from '@/services/PersonService'
import Time from '@/utils/Time'
import Loader from '@/components/utils/Loader.vue'
import BtnMenu from '@/components/ButtonMenu.vue'

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
    Loader,
    BtnMenu
  },
  data () {
    return {
      currentMonth: Time.getCurrentMonth()
    }
  },
  methods: {
    logout () {
      PersonService.deleteSession(this.$store)
    },
    goToProfile () {
      router.push(RouterPages.profile)
    }
  }
})
</script>
