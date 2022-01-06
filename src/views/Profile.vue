<template>
  <div id="profile_page" :class="this.$store.state.css">
    <h1>Profil utilisateur</h1>
    <h3>Cette page est encore en construction</h3>
    <p>Si vous avez été redirigé automatiquement ici, c'est que votre abonnement n'est pas à jour</p>
    <p><a class="teamSelector" v-on:click="goToBillingPortal">{{$t('BILLING_MANAGEMENT')}}</a></p>
    <NavMenu :page="'profile'" />
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import PersonService from '@/services/PersonService'
import NavMenu from '@/components/NavigationMenu.vue'

export default defineComponent({
  name: 'Profile',
  components: { NavMenu },
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
