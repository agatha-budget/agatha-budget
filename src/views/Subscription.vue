<template>
  <div :class="this.$store.state.css">
    <div id="subscriptionPage" class="col-12 offset-0 col-md-8 offset-md-2 col-lg-6 offset-lg-3">
      <div v-if="profile == 'PROFILE_USER'" class="user">
        <div class="topBanner">
          <h1>{{ $t('SUBSCRIPTION') }}</h1>
          <p v-if="!validSubscription" class="col-12">{{ $t('TRIAL_PERIOD_IS_OVER') }}</p>
        </div>
        <div v-if="!validSubscription" class="placeholderTop"/>
        <p v-on:click="this.goToContactPage">{{ $t('CHOOSE_THE_BEST_FOR_YOU') }}</p>
        <div class="essential">
          <div class="container header">
            <span class="icon icon-paper-plane" />
            <div class="title">{{ $t('ESSENTIAL') }}</div>
          </div>
          <p>{{ $t('TEXT_ESSENTIAL') }}</p>
          <btn class="navigationButton" v-on:click="this.goToBillingPortal('MONTHLY_ESSENTIAL')">{{ $t('PRICE_MONTHLY_ESSENTIAL') }}</btn>
          <btn class="navigationButton" v-on:click="this.goToBillingPortal('ANNUAL_ESSENTIAL')">{{ $t('PRICE_ANNUAL_ESSENTIAL') }}</btn>
        </div>
        <div class="integral">
          <div class="container header">
            <span class="icon icon-plane" />
            <div class="title">{{ $t('INTEGRAL') }}</div>
          </div>
          <p>{{ $t('TEXT_INTEGRAL') }}</p>
          <btn class="navigationButton" v-on:click="this.goToBillingPortal('MONTHLY_INTEGRAL')">{{ $t('PRICE_MONTHLY_INTEGRAL') }}</btn>
          <btn class="navigationButton" v-on:click="this.goToBillingPortal('ANNUAL_INTEGRAL')">{{ $t('PRICE_ANNUAL_INTEGRAL') }}</btn>
        </div>
        <div class="solidarity">
          <div class="container header">
            <span class="icon icon-support" />
            <div class="title">{{ $t('SOLIDARITY') }}</div>
          </div>
          <p v-on:click="this.goToContactPage">{{ $t('TEXT_FOR_USER') }}{{ $t('PLEASE_CONTACT_US') }}</p>
          <btn class="navigationButton disabled" v-on:click="this.goToContactPage">{{ $t('PRICE_MONTHLY_SOLIDARITY') }}</btn>
        </div>
        <div class="placeholderBottom"/>
      </div>

      <div v-if="profile == 'PROFILE_COMPANY'" class="company">
        <div class="topBanner">
          <h1>{{ $t('SUBSCRIPTION') }}</h1>
          <p v-if="!validSubscription" class="col-12">{{ $t('TRIAL_PERIOD_IS_OVER') }}</p>
        </div>
        <p>{{ $t('CHOOSE_THE_BEST_FOR_YOU') }}</p>
        <div class="businessSide">
          <div class="container header">
            <span class="icon icon-briefcase" />
            <div class="title">{{ $t('BUSINESS_SIDE') }}</div>
          </div>
          <p>{{ $t('TEXT_COMPANY') }}</p>
          <btn class="navigationButton" v-on:click="this.goToBillingPortal('MONTHLY_COMPANY')">{{ $t('PRICE_MONTHLY_COMPANY') }}</btn>
          <btn class="navigationButton" v-on:click="this.goToBillingPortal('ANNUAL_COMPANY')">{{ $t('PRICE_ANNUAL_COMPANY') }}</btn>
        </div>
        <div class="personalSide">
          <div class="container header">
            <span class="icon icon-home" />
            <div class="title">{{ $t('PERSONAL_SIDE') }}</div>
          </div>
          <p v-on:click="this.goToContactPage">{{ $t('TEXT_PERSONAL_SIDE') }}{{ $t('PLEASE_CONTACT_US') }}</p>
          <btn class="navigationButton disabled" v-on:click="this.goToContactPage">{{ $t('PRICE_PERSONAL_SIDE_MONTHLY') }}</btn>
          <btn class="navigationButton disabled" v-on:click="this.goToContactPage">{{ $t('PRICE_PERSONAL_SIDE_ANNUAL') }}</btn>
        </div>
        <div class="coaching">
          <div class="container header">
            <span class="icon icon-compass" />
            <div class="title">{{ $t('PERSONAL_SUPPORT') }}</div>
          </div>
          <p v-on:click="this.goToContactPage">{{ $t('TEXT_PERSONAL_SUPPORT') }}{{ $t('PLEASE_CONTACT_US_AND_RDV') }}</p>
          <btn class="navigationButton disabled" v-on:click="this.goToContactPage">{{ $t('PRICE_COACHING_1H') }}</btn>
          <btn class="navigationButton disabled" v-on:click="this.goToContactPage">{{ $t('PRICE_COACHING_5H') }}</btn>
        </div>
        <div class="placeholderBottom"/>
      </div>
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
  props: {
    validSubscription: {
      type: Boolean,
      required: true
    }
  },
  data () {
    return { }
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
    }
  }
})
</script>
