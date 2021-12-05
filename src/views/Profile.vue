<template>
  <div id="profile_page">
    <h1>Profil utilisateur</h1>
    <h3>Cette page est encore en construction</h3>
    <p>Si vous avez été redirigé automatiquement ici, c'est que votre abonnement n'est pas à jour</p>
    <p><a class="teamSelector" v-on:click="goToBillingPortal">{{$t('BILLING_MANAGEMENT')}}</a></p>
    <button v-on:click="logout" class="btn fas fa-sign-out-alt" :title="$t('LOGOUT')"/>
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
    },
    logout () {
      PersonService.deleteSession(this.$store)
    }
  }

})
</script>
