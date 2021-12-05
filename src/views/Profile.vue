<template>
  <div id="profile_page">
    <h1>Profil utilisateur</h1>
    <h3>Cette page est encore en construction</h3>
    <p><a class="teamSelector" v-on:click="goToBillingPortal">{{$t('BILLING_MANAGEMENT')}}</a></p>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import PersonService from '@/services/PersonService'

export default defineComponent({
  name: 'Profile',
  components: {},
  data () {
    return {}
  },
  methods: {
    async goToBillingPortal (): Promise<void> {
      PersonService.redirectToBillingPortalUrl(this.$store)
    }
  }

})
</script>
