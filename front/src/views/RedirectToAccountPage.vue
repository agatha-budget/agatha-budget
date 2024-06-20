<template>
  <div :class="css">
    <div class="redirectToAccountPage page">
      <div class="fixedHeader">
        <h1 class="title">{{ $t('MY_ACCOUNTS') }}</h1>
      </div>
      <div class="pageContent">
        <AccountsWidget class="centeredContent" :page="'account'"/>
      </div>
      <div class="fixedFooter">
        <NavMenu :page="'accounts'" />
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import AccountsWidget from '@/components/AccountsWidget.vue'
import NavMenu from '@/components/NavigationMenu.vue'
import { usePersonStore } from '@/stores/personStore'

export default defineComponent({
  name: 'RedirectToAccountPage',
  components: {
    AccountsWidget,
    NavMenu
  },
  computed: {
    css (): string {
      return usePersonStore().css
    }
  },
  methods: {}
})
</script>
