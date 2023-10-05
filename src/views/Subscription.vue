<template>

  <div :class="this.$store.state.css">
    <div class="subscriptionPage menuLayout row col-md-4 offset-md-4 col-xl-8 offset-xl-2">
      <div class="header fixed title">
        <h1 class="title">{{ $t('SUBSCRIPTION') }}</h1>
        <p v-if="!validSubscription" class="col-12">{{ $t('TRIAL_PERIOD_IS_OVER') }}</p>
      </div>
      <div class="placeholder top">
        <h1 class="title">{{ $t('SUBSCRIPTION') }}</h1>
        <p v-if="!validSubscription" class="col-12">{{ $t('TRIAL_PERIOD_IS_OVER') }}</p>
      </div>
      <div v-if="this.managementPage">
        <p>{{ $t('MANAGE_SUBSCRIPTION') }} : </p>
        <p v-if="!billingStatus">{{ $t('DEFERMENT_SUBSCRIPTION') }}</p>
        <button  class="actionButton" v-on:click="this.getInformation">{{ $t('MANAGE_SUBSCRIPTION') }}</button>
        <button class="navigationButton" v-on:click="this.changePage">{{ $t('SEE_ALL_OFFERS') }}</button>
      </div>
      <div v-else>
        <div v-if="profile == 'PROFILE_USER'" class="content">
          <p class="subtitle" v-on:click="this.goToContactPage">{{ $t('CHOOSE_THE_BEST_FOR_YOU') }}</p>
          <p class="subtitle" v-on:click="this.goToContactPage">{{ $t('ASK_FOR_HELP') }}</p>
          <div class="essential">
            <div class="banner">
              <span class="icon icon-paper-plane" />
              <div class="title">{{ $t('ESSENTIAL') }}</div>
            </div>
            <p>{{ $t('TEXT_ESSENTIAL') }}</p>
            <button class="actionButton" v-on:click="this.goToBillingPortal('MONTHLY_ESSENTIAL')">{{ $t('PRICE_MONTHLY_ESSENTIAL') }}</button>
            <button class="actionButton" v-on:click="this.goToBillingPortal('ANNUAL_ESSENTIAL')">{{ $t('PRICE_ANNUAL_ESSENTIAL') }}</button>
          </div>
          <div class="integral">
            <div class="banner">
              <span class="icon icon-plane" />
              <div class="title">{{ $t('INTEGRAL') }}</div>
            </div>
            <p>{{ $t('TEXT_INTEGRAL') }}</p>
            <button class="actionButton" v-on:click="this.goToBillingPortal('MONTHLY_INTEGRAL')">{{ $t('PRICE_MONTHLY_INTEGRAL') }}</button>
            <button class="actionButton" v-on:click="this.goToBillingPortal('ANNUAL_INTEGRAL')">{{ $t('PRICE_ANNUAL_INTEGRAL') }}</button>
          </div>
          <div class="solidarity">
            <div class="banner">
              <span class="icon icon-support" />
              <div class="title">{{ $t('SOLIDARITY') }}</div>
            </div>
            <p v-on:click="this.goToContactPage">{{ $t('TEXT_FOR_USER') }}{{ $t('PLEASE_CONTACT_US') }}</p>
            <button class="actionButton disabled" v-on:click="this.goToContactPage">{{ $t('PRICE_MONTHLY_SOLIDARITY') }}</button>
          </div>
        </div>
        <div v-else class="content">
          <div class="businessSide">
            <div class="container header">
              <span class="icon icon-briefcase" />
              <div class="title">{{ $t('BUSINESS_SIDE') }}</div>
            </div>
            <p>{{ $t('TEXT_COMPANY') }}</p>
            <button class="actionButton" v-on:click="this.goToBillingPortal('MONTHLY_COMPANY')">{{ $t('PRICE_MONTHLY_COMPANY') }}</button>
            <button class="actionButton" v-on:click="this.goToBillingPortal('ANNUAL_COMPANY')">{{ $t('PRICE_ANNUAL_COMPANY') }}</button>
          </div>
          <div class="personalSide">
            <div class="container header">
              <span class="icon icon-home" />
              <div class="title">{{ $t('PERSONAL_SIDE') }}</div>
            </div>
            <p v-on:click="this.goToContactPage">{{ $t('TEXT_PERSONAL_SIDE') }}{{ $t('PLEASE_CONTACT_US') }}</p>
            <button class="actionButton" v-on:click="this.goToContactPage">{{ $t('PRICE_PERSONAL_SIDE_MONTHLY') }}</button>
            <button class="actionButton" v-on:click="this.goToContactPage">{{ $t('PRICE_PERSONAL_SIDE_ANNUAL') }}</button>
          </div>
          <div class="coaching">
            <div class="container header">
              <span class="icon icon-compass" />
              <div class="title">{{ $t('PERSONAL_SUPPORT') }}</div>
            </div>
            <p v-on:click="this.goToContactPage">{{ $t('TEXT_PERSONAL_SUPPORT') }}{{ $t('PLEASE_CONTACT_US_AND_RDV') }}</p>
            <button class="actionButton" v-on:click="this.goToContactPage">{{ $t('PRICE_COACHING_1H') }}</button>
            <button class="actionButton" v-on:click="this.goToContactPage">{{ $t('PRICE_COACHING_5H') }}</button>
          </div>
        </div>
        <button class="actionButton" v-if="hasBillingId" v-on:click="this.changePage">{{ $t('MANAGE_SUBSCRIPTION') }}</button>
      </div>
      <div class="placeholder bottom">
        <NavMenu/>
      </div>
      <div class="footer fixed">
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
import { Person } from '@/model/model'

interface SubscriptionPageData {
  hasBillingId: boolean | undefined;
  managementPage: boolean |undefined;
  billingStatus: boolean |undefined;
}

export default defineComponent({
  name: 'SubscriptionView',
  components: { NavMenu },
  created: async function () {
    StoreHandler.initStore(this.$store)
    this.hasBillingId = this.person?.hasBillingId
    this.managementPage = this.person?.hasBillingId
    this.billingStatus = this.person?.billingStatus
  },
  props: {
    validSubscription: {
      type: Boolean,
      required: true
    }
  },
  data (): SubscriptionPageData {
    return {
      hasBillingId: true,
      managementPage: true,
      billingStatus: true
    }
  },
  computed: {
    profile (): string | undefined {
      return this.$store.state.budget?.profile
    },
    person (): Person | null {
      return this.$store.state.person
    }
  },
  methods: {
    async goToBillingPortal (selectedPackage: string): Promise<void> {
      PersonService.redirectToBillingPortalUrl(selectedPackage)
    },
    goToContactPage () {
      window.location.href = 'https://agatha-budget.fr/contact/'
    },
    async getInformation () {
      PersonService.manageSubscription()
    },
    changePage () {
      this.managementPage = !this.managementPage
    }
  }
})
</script>
