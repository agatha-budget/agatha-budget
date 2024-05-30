<template>
  <div :class="css">
    <div class="redirectToAccountPage menuLayout">
      <div class="header fixed title">
        {{ $t('MY_ACCOUNTS') }}
      </div>
      <div class="placeholder top">
        {{ $t('MY_ACCOUNTS') }}
      </div>
      <div class="content container">
        <AccountsWidget :page="'account'"/>
      </div>
      <div class="placeholder bottom">
        <NavMenu :page="'accounts'" />
      </div>
      <div class="footer fixed">
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
