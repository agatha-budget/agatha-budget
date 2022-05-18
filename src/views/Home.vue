<template >
  <div :class="this.$store.state.css">
    <div class="row">
      <div v-if="this.$store.state.storeLoaded">
        <div class="home">
          <BudgetCmpt :month="this.currentMonth" />
          <div class="col-3 offset-1 col-xl-3 offset-xl-1 col-xxl-3 offset-xxl-1">
            <div class="accountWidgetAtHome">
              <AccountsWidget :page="'home'"/>
            </div>
            <NavMenu :page="'home'" />
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
import NavMenu from '@/components/NavigationMenu.vue'

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
    NavMenu
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
