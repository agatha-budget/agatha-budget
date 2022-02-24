<template>
  <div id="profile_page" :class="this.$store.state.css">
    <h1>Profil utilisateur</h1>
    <h3>Cette page est encore en construction</h3>
    <p>Si vous avez été redirigé automatiquement ici, c'est que votre abonnement n'est pas à jour</p>
    <p><a class="teamSelector" v-on:click="goToBillingPortal">{{$t('BILLING_MANAGEMENT')}}</a></p>

    <div v-if="profileTest == 'user'">
      <p>Vous êtes un particulier et vous cherchez à renouveler votre abonnement</p>
      <button v-on:click="goToUserBillingPortal">intégral annuel</button>
      <button v-on:click="goToUserBillingPortal">intégral mensuel</button>
      <button v-on:click="goToUserBillingPortal">essentiel annuel</button>
      <button v-on:click="goToUserBillingPortal">essentiel mensuel</button>
      <p>Si vous êtes en difficulté vous pouvez bénéficier de notre programme solidaire, contactez-nous pour obtenir l'offre</p>
    </div>
    <div v-if="profileTest == 'company'">
      <p>Vous êtes un professionel et vous cherchez à renouveler votre abonnement</p>
      <button v-on:click="goToProfessionalBillingPortal">annuel</button>
      <button v-on:click="goToProfessionalBillingPortal">mensuel</button>
      <p>Vous ne savez pas par quel bout commencer pour gérer vous finances ? bénéficiez de nos séances de coaching</p>
      <button v-on:click="goToProfessionalBillingPortal">coaching 1h</button>
      <button v-on:click="goToProfessionalBillingPortal">coaching 5h</button>
      <p>Si vous souhaitez ouvrir un compte Agatha-budget pour vos finances personnelles vous avez le droit à un réduction, contactez-nous pour en savoir plus</p>
    </div>
    <button v-on:click="changerProfileTest">changer de profil</button>
    <NavMenu :page="'profile'" />
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import PersonService from '@/services/PersonService'
import NavMenu from '@/components/NavigationMenu.vue'
import StoreHandler from '@/store/StoreHandler'

export default defineComponent({
  name: 'Profile',
  components: { NavMenu },
  created: async function () {
    StoreHandler.initStore(this.$store)
  },
  data () {
    return {
      profileTest: 'user'
    }
  },
  computed: {
    profile (): string | undefined {
      return this.$store.state.budget?.profile
    }
  },
  methods: {
    async goToBillingPortal (): Promise<void> {
      PersonService.redirectToBillingPortalUrl()
    },
    logout () {
      PersonService.deleteSession(this.$store)
    },

    async goToProfessionalBillingPortal (): Promise<void> {
      console.log(this.profile)
      // PersonService.redirectToBillingPortalUrl()
    },
    async goToUserBillingPortal (): Promise<void> {
      console.log(this.profile)
      // PersonService.redirectToBillingPortalUrl()
    },

    changerProfileTest () {
      if (this.profileTest === 'user') {
        this.profileTest = 'company'
      } else {
        this.profileTest = 'user'
      }
    }
  }

})
</script>
