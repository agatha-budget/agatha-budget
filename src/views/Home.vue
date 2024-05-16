<template >
  <div :class="css">
    <div class="row">
      <div v-if="storeLoaded">
        <div class="home">
          <BudgetCmpt :month="currentMonth" />
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
import AccountsWidget from '@/components/AccountsWidget.vue';
import BudgetCmpt from '@/components/BudgetCmpt.vue';
import NavMenu from '@/components/NavigationMenu.vue';
import Loader from '@/components/utils/Loader.vue';
import router, { RouterPages } from '@/router';
import { useOperationStore } from '@/stores/operationStore';
import { usePersonStore } from '@/stores/personStore';
import Time from '@/utils/Time';
import { defineComponent } from 'vue';

export default defineComponent({
  name: 'HomeView',
  created: async function () {
    usePersonStore().init()
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
  computed: {
    css (): string {
      return usePersonStore().css
    },
    storeLoaded (): boolean {
      return useOperationStore().storeLoaded
    }
  },
  methods: {
    goToProfile () {
      router.push(RouterPages.profile)
    }
  }
})
</script>
