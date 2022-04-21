<template>
  <div :class="this.$store.state.css">
    <div id="subscriptionPage" class="col-12 offset-0 col-md-8 offset-md-2 col-lg-6 offset-lg-3">
      <div v-if="profileTest == 'PROFILE_USER'" class="user">
        <div class="topBanner">
          <h1>{{ $t('SUBSCRIPTION') }}</h1>
          <p class="col-12">{{ $t('TRIAL_PERIOD_IS_OVER') }}</p>
        </div>
        <p>{{ $t('CHOOSE_THE_BEST_FOR_YOU') }}</p>
        <div class="essential">
          <span class="icon icon-paper-plane" />
          <div class="container header title">{{ $t('ESSENTIAL') }}</div>
          <p>{{ $t('TEXT_ESSENTIAL') }}</p>
          <btn class="navigationButton" v-on:click="this.goToBillingPortal('MONTHLY_ESSENTIAL')">{{ $t('PRICE_MONTHLY_ESSENTIAL') }}</btn>
          <btn class="navigationButton" v-on:click="this.goToBillingPortal('ANNUAL_ESSENTIAL')">{{ $t('PRICE_ANNUAL_ESSENTIAL') }}</btn>
        </div>
        <div class="integral">
          <span class="icon icon-plane" />
          <div class="container header title">{{ $t('INTEGRAL') }}</div>
          <p>{{ $t('TEXT_INTEGRAL') }}</p>
          <btn class="navigationButton" v-on:click="this.goToBillingPortal('MONTHLY_INTEGRAL')">{{ $t('PRICE_MONTHLY_INTEGRAL') }}</btn>
          <btn class="navigationButton" v-on:click="this.goToBillingPortal('ANNUAL_INTEGRAL')">{{ $t('PRICE_ANNUAL_INTEGRAL') }}</btn>
        </div>
        <div class="solidarity">
          <span class="icon icon-support" />
          <div class="container header title">{{ $t('SOLIDARITY') }}</div>
          <p v-on:click="this.goToContactPage">{{ $t('TEXT_FOR_USER') }}{{ $t('PLEASE_CONTACT_US') }}</p>
          <btn class="navigationButton disabled" v-on:click="this.goToContactPage">{{ $t('PRICE_MONTHLY_SOLIDARITY') }}</btn>
        </div>
      <btn v-on:click="switchProfileTest">switch</btn>
      </div>

      <div v-if="profileTest == 'PROFILE_COMPANY'" class="company">
        <div class="topBanner">
          <h1>{{ $t('SUBSCRIPTION') }}</h1>
          <p>{{ $t('TRIAL_PERIOD_IS_OVER') }}</p>
        </div>
        <p>{{ $t('CHOOSE_THE_BEST_FOR_YOU') }}</p>
        <div class="businessSide">
          <span class="icon icon-briefcase" />
          <div class="container header title">{{ $t('BUSINESS_SIDE') }}</div>
          <p>{{ $t('TEXT_COMPANY') }}</p>
          <btn class="navigationButton" v-on:click="this.goToBillingPortal('MONTHLY_COMPANY')">{{ $t('PRICE_MONTHLY_COMPANY') }}</btn>
          <btn class="navigationButton" v-on:click="this.goToBillingPortal('ANNUAL_COMPANY')">{{ $t('PRICE_ANNUAL_COMPANY') }}</btn>
        </div>
        <div class="personalSide">
          <span class="icon icon-home" />
          <div class="container header title">{{ $t('PERSONAL_SIDE') }}</div>
          <p v-on:click="this.goToContactPage">{{ $t('TEXT_PERSONAL_SIDE') }}{{ $t('PLEASE_CONTACT_US') }}</p>
          <btn class="navigationButton disabled" v-on:click="this.goToContactPage">{{ $t('PRICE_PERSONAL_SIDE_MONTHLY') }}</btn>
          <btn class="navigationButton disabled" v-on:click="this.goToContactPage">{{ $t('PRICE_PERSONAL_SIDE_ANNUAL') }}</btn>
        </div>
        <div class="coaching">
          <span class="icon icon-compass" />
          <div class="container header title">{{ $t('PERSONAL_SUPPORT') }}</div>
          <p v-on:click="this.goToContactPage">{{ $t('TEXT_PERSONAL_SUPPORT') }}{{ $t('PLEASE_CONTACT_US_AND_RDV') }}</p>
          <btn class="navigationButton disabled" v-on:click="this.goToContactPage">{{ $t('PRICE_COACHING_1H') }}</btn>
          <btn class="navigationButton disabled" v-on:click="this.goToContactPage">{{ $t('PRICE_COACHING_5H') }}</btn>
        </div>
      <btn v-on:click="switchProfileTest">switch</btn>
      </div>
      <div class="placeholderBottom"/>
      <div class="navigationMenu">
        <NavMenu/>
      </div>
    </div>
  </div>
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
      profileTest: 'PROFILE_USER'
    }
  },
  computed: {
    profile (): string | undefined {
      return this.$store.state.budget?.profile
    }
  },
  methods: {
    async goToBillingPortal (selectedPackage: string): Promise<void> {
      PersonService.redirectToBillingPortalUrl(selectedPackage)
    },
    goToContactPage () {
      window.location.href = 'https://agatha-budget.fr/contact/'
    },
    switchProfileTest () {
      if (this.profileTest === 'PROFILE_USER') {
        this.profileTest = 'PROFILE_COMPANY'
      } else {
        this.profileTest = 'PROFILE_USER'
      }
    }
  }
})
</script>
