<template>

  <div :class="css">
    <div class="subscriptionPage menuLayout row col-md-4 offset-md-4 col-xl-8 offset-xl-2">
      <div class="header fixed title">
        <h1 class="title">{{ $t('SUBSCRIPTION') }}</h1>
        <div v-if="!validSubscription">
          <p>{{ $t('TRIAL_PERIOD_IS_OVER') }}</p>
          <button class="navigationButton row" v-on:click="logout">
            <span class="illustration fas fa-sign-out-alt col-4"/>
            <span class="illustrationLabel col-8">{{ $t("LOGOUT") }}</span>
          </button>
        </div>
      </div>
      <div class="placeholder top">
        <h1 class="title">{{ $t('SUBSCRIPTION') }}</h1>
        <div v-if="!validSubscription">
          <p>{{ $t('TRIAL_PERIOD_IS_OVER') }}</p>
          <button class="navigationButton row" v-on:click="logout">
            <span class="illustration fas fa-sign-out-alt col-4"/>
            <span class="illustrationLabel col-8">{{ $t("LOGOUT") }}</span>
          </button>
        </div>
      </div>

      <div v-if="managementPage">
        <p>{{ $t('MANAGE_SUBSCRIPTION') }} : </p>
        <p v-if="!billingStatus">{{ $t('DEFERMENT_SUBSCRIPTION') }}</p>
        <button  class="actionButton" v-on:click="getInformation">{{ $t('MANAGE_SUBSCRIPTION') }}</button>
        <button class="navigationButton" v-on:click="changePage">{{ $t('SEE_ALL_OFFERS') }}</button>
      </div>
      <div v-else>
        <div v-if="profile == 'PROFILE_USER'" class="content">
          <p class="subtitle" v-on:click="goToContactPage">{{ $t('ASK_FOR_HELP') }}</p>
          <div class="essential">
            <div class="banner">
              <span class="icon icon-paper-plane" />
              <div class="title">{{ $t('CLASSICAL') }}</div>
            </div>
            <button class="actionButton" v-on:click="goToBillingPortal('MONTHLY_ESSENTIAL')">{{ $t('PRICE_MONTHLY_ESSENTIAL') }}</button>
            <button class="actionButton" v-on:click="goToBillingPortal('ANNUAL_ESSENTIAL')">{{ $t('PRICE_ANNUAL_ESSENTIAL') }}</button>
          </div>
          <div class="solidarity">
            <div class="banner">
              <span class="icon icon-support" />
              <div class="title">{{ $t('SOLIDARITY') }}</div>
            </div>
            <p v-on:click="goToContactPage">{{ $t('TEXT_FOR_USER') }}{{ $t('PLEASE_CONTACT_US') }}</p>
            <button class="actionButton disabled" v-on:click="goToContactPage">{{ $t('PRICE_MONTHLY_SOLIDARITY') }}</button>
          </div>
        </div>
        <div v-else class="content">
          <div class="businessSide">
            <div class="container header">
              <span class="icon icon-briefcase" />
              <div class="title">{{ $t('BUSINESS_SIDE') }}</div>
            </div>
            <p>{{ $t('TEXT_COMPANY') }}</p>
            <button class="actionButton" v-on:click="goToBillingPortal('MONTHLY_COMPANY')">{{ $t('PRICE_MONTHLY_COMPANY') }}</button>
            <button class="actionButton" v-on:click="goToBillingPortal('ANNUAL_COMPANY')">{{ $t('PRICE_ANNUAL_COMPANY') }}</button>
          </div>
          <div class="personalSide">
            <div class="container header">
              <span class="icon icon-home" />
              <div class="title">{{ $t('PERSONAL_SIDE') }}</div>
            </div>
            <p v-on:click="goToContactPage">{{ $t('TEXT_PERSONAL_SIDE') }}{{ $t('PLEASE_CONTACT_US') }}</p>
            <button class="actionButton" v-on:click="goToContactPage">{{ $t('PRICE_PERSONAL_SIDE_MONTHLY') }}</button>
            <button class="actionButton" v-on:click="goToContactPage">{{ $t('PRICE_PERSONAL_SIDE_ANNUAL') }}</button>
          </div>
          <div class="coaching">
            <div class="container header">
              <span class="icon icon-compass" />
              <div class="title">{{ $t('PERSONAL_SUPPORT') }}</div>
            </div>
            <p v-on:click="goToContactPage">{{ $t('TEXT_PERSONAL_SUPPORT') }}{{ $t('PLEASE_CONTACT_US_AND_RDV') }}</p>
            <button class="actionButton" v-on:click="goToContactPage">{{ $t('PRICE_COACHING_1H') }}</button>
            <button class="actionButton" v-on:click="goToContactPage">{{ $t('PRICE_COACHING_5H') }}</button>
          </div>
        </div>
        <button class="actionButton" v-if="hasBillingId" v-on:click="changePage">{{ $t('MANAGE_SUBSCRIPTION') }}</button>
      </div>
      <div class="placeholder bottom">
        <NavMenu :page="'profile'" />
      </div>
      <div class="footer fixed">
        <NavMenu :page="'profile'" />
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import NavMenu from '@/components/NavigationMenu.vue'
import type { Person } from '@/model/model'
import PersonService from '@/services/PersonService'
import KeycloakService from '@/services/security/KeycloakService'
import { useBudgetStore } from '@/stores/budgetStore'
import { usePersonStore } from '@/stores/personStore'
import { defineComponent } from 'vue'


interface SubscriptionPageData {
  hasBillingId: boolean | undefined;
  managementPage: boolean |undefined;
  billingStatus: boolean |undefined;
}

export default defineComponent({
  name: 'SubscriptionView',
  components: { NavMenu },
  created: async function () {
    usePersonStore().init()
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
      return useBudgetStore().budget?.profile
    },
    person (): Person | null {
      return usePersonStore().person
    },
    css (): string {
      return usePersonStore().css
    },
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
    },
    logout () {
      KeycloakService.CallLogOut();
    },
  }
})
</script>
