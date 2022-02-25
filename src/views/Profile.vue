<template>
  <div id="profile_page" :class="this.$store.state.css">
    <div v-if="profileTest == 'user'" class="user">
      <p>Vous êtes un particulier et vous cherchez à renouveler votre abonnement</p>
      <div class="essentiel">
        <h1>Essentiel</h1>
        <div class="icon-plane" />
        <p>1 mois d'essai offert Un support individuel selon nos disponibilités </p>
        <button v-on:click="this.goToUserBillingPortal('MONTHLY_ESSENTIAL')">6€/mois sans engagement</button>
        <button v-on:click="this.goToUserBillingPortal('ANNUAL_ESSENTIAL')">60€/an soit 5€/mois</button>
      </div>
      <div class="integral">
        <h1>Intégral</h1>
        <div class="icon-paper-plane" />
        <p>1 mois d'essai offert Les fonctionnalités en avant-première Un support individuel prioritaire </p>
        <button v-on:click="this.goToUserBillingPortal('MONTHLY_INTEGRAL')">10€/mois sans engagement</button>
        <button v-on:click="this.goToUserBillingPortal('ANNUAL_INTEGRAL')">96€/an soit 8€/mois</button>
      </div>
      <p>Si vous êtes en difficulté vous pouvez bénéficier de notre programme solidaire, contactez-nous pour obtenir l'offre</p>
    </div>
    <div v-if="profileTest == 'company'" class="company">
      <p>Vous êtes un professionel et vous cherchez à renouveler votre abonnement ou prendre des séances de coaching</p>
      <div class="subscription">
        <h1>Entreprise</h1>
        <p>1 mois d'essai offert Toutes les fonctionnalités Un support individuel prioritaire</p>
        <button v-on:click="this.goToProfessionalBillingPortal('MONTHLY_COMPANY')">8€/mois HT sans engagement</button>
        <button v-on:click="this.goToProfessionalBillingPortal('ANNUAL_COMPANY')">84€/an HT soit 7€/mois HT</button>
      </div>
      <div class="coaching">
        <h1>Accompagnement individuel</h1>
        <p>Votre rapport à l'argent est complexe et devient un frein à votre projet ? Nous pouvons vous aider</p>
        <button disabled>45€/heure HT sans engagement</button>
        <button disabled>200€ pour 5h soit 40€/heure HT </button>
      </div>
      <p>Si vous souhaitez ouvrir un compte Agatha-budget pour vos finances personnelles vous avez le droit à une réduction, contactez-nous pour en savoir plus</p>
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

    async goToProfessionalBillingPortal (selectedPackage: string): Promise<void> {
      console.log(this.profile)
      console.log(selectedPackage)
      PersonService.redirectToBillingPortalUrl(selectedPackage)
    },
    async goToUserBillingPortal (selectedPackage: string): Promise<void> {
      console.log(this.profile)
      console.log(selectedPackage)
      PersonService.redirectToBillingPortalUrl(selectedPackage)
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
