<template >
  <div v-if="storeLoaded">
    <div :class="css">

      <div class="home">
        <div class="main">
          <BudgetCmpt :month="currentMonth" />
        </div>
        <div class="sidebar">
          <div class="accountWidget">
            <AccountsWidget :page="'home'"/>
          </div>
        </div>
        <NavMenu :page="'home'" />
      </div>
    </div>
  </div>

  <div v-else>
      <Loader class="loader"/>
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
