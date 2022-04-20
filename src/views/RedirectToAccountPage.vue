<template>
  <div :class="this.$store.state.css">
    <div class="col-12 offset-0 col-sm-8 offset-sm-2 col-md-6 offset-md-3 col-lg-4 offset-lg-4 col-xxl-3 offset-xxl-5 ">
      <AccountsWidget :page="'account'"/>
      <NavMenu :page="'accounts'" />
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import AccountsWidget from '@/components/AccountsWidget.vue'
import NavMenu from '@/components/NavigationMenu.vue'

export default defineComponent({
  name: 'RedirectToAccountPage',
  components: {
    AccountsWidget,
    NavMenu
  },
  methods: {}
})
</script>
