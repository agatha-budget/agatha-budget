<template>
  <div :class="this.$store.state.css">
    <div id="subscritionPage">
      <div v-if="profileTest == 'user'" class="user">
        <p>Vous êtes un particulier et vous cherchez à renouveler votre abonnement</p>
        <div class="essentiel">
          <h1 class="title">{{ $t('ESSENTIAL') }}</h1>
          <div class="icon icon-plane" />
          <p>Un support individuel selon nos disponibilités </p>
          <button class="btnSubscrition" v-on:click="this.goToBillingPortal('MONTHLY_ESSENTIAL')">6€/mois</button>
          <button class="btnSubscrition" v-on:click="this.goToBillingPortal('ANNUAL_ESSENTIAL')">60€/an</button>
        </div>
        <div class="integral">
          <h1 class="title">{{ $t('INTEGRAL') }}</h1>
          <div class="icon icon-paper-plane" />
          <p>Les fonctionnalités en avant-première Un support individuel prioritaire </p>
          <button class="btnSubscrition" v-on:click="this.goToBillingPortal('MONTHLY_INTEGRAL')">10€/mois</button>
          <button class="btnSubscrition" v-on:click="this.goToBillingPortal('ANNUAL_INTEGRAL')">96€/an</button>
        </div>
        <p>Si vous êtes en difficulté vous pouvez bénéficier de notre programme solidaire, contactez-nous pour obtenir l'offre</p>
      </div>
      <div v-if="profileTest == 'company'" class="company">
        <p>Vous êtes un professionnel et vous cherchez à renouveler votre abonnement ou prendre des séances de coaching</p>
        <div class="subscription">
          <h1 class="title">{{ $t('COMPANY') }}</h1>
          <div class="icon icon-paper-clip" />
          <p>Toutes les fonctionnalités Un support individuel prioritaire</p>
          <button class="btnSubscrition" v-on:click="this.goToBillingPortal('MONTHLY_COMPANY')">8€/mois HT sans engagement</button>
          <button class="btnSubscrition" v-on:click="this.goToBillingPortal('ANNUAL_COMPANY')">84€/an HT soit 7€/mois HT</button>
        </div>
        <div class="coaching">
          <h1 class="title">{{ $t('PERSONAL_SUPPORT') }}</h1>
          <div class="icon icon-compass" />
          <p>Votre rapport à l'argent est complexe et devient un frein à votre projet ? Nous pouvons vous aider</p>
          <button class="btnSubscrition disabled">1h -> 45€</button>
          <button class="btnSubscrition disabled">5h -> 200€</button>
        </div>
        <p>Si vous souhaitez ouvrir un compte Agatha-budget pour vos finances personnelles vous avez le droit à une réduction, contactez-nous pour en savoir plus</p>
      </div>
    </div>

    <div class="navMenu">
      <NavMenu/>
    </div>
  </div>
  <button v-on:click="changerProfileTest">changer de profil</button>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import NavMenu from '@/components/NavigationMenu.vue'
import PersonService from '@/services/PersonService'
import StoreHandler from '@/store/StoreHandler'

export default defineComponent({
  name: 'Subscription',
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
    logout () {
      PersonService.deleteSession(this.$store)
    },

    async goToBillingPortal (selectedPackage: string): Promise<void> {
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
