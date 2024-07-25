<template>

  <div :class="css">
    <div id="subscriptionPage" class="page">
      <div class="fixedHeader">
        <h1 class="title">{{ $t('SUBSCRIPTION') }}</h1>
      </div>  
      <div class="pageContent">
        <div class="centeredContent">
          <div v-if="!validSubscription">
            <p>{{ $t('TRIAL_PERIOD_IS_OVER') }}</p>
            <button class="navigationButton row" v-on:click="logout">
              <span class="illustration fas fa-sign-out-alt col-4"/>
              <span class="illustrationLabel col-8">{{ $t("LOGOUT") }}</span>
            </button>
          </div>
          <p v-if="!billingStatus">{{ $t('DEFERMENT_SUBSCRIPTION') }}</p>
          <button class="actionButton" v-if="hasBillingId" v-on:click="changePage">{{ $t('MANAGE_SUBSCRIPTION') }}</button>
          <div>
            <p class="subtitle">{{ $t('ASK_FOR_HELP') }}</p>
            <div class="offer">
              <div class="banner">
                <span class="icon icon-paper-plane" />
                <div class="title">{{ $t('CLASSICAL') }}</div>
              </div>
              <div class="actions">
                <button class="actionButton" v-on:click="goToBillingPortal('MONTHLY_ESSENTIAL')">{{ $t('PRICE_MONTHLY_ESSENTIAL') }}</button>
                <button class="actionButton" v-on:click="goToBillingPortal('ANNUAL_ESSENTIAL')">{{ $t('PRICE_ANNUAL_ESSENTIAL') }}</button>
              </div> 
            </div>
            <div class="offer">
              <div class="banner">
                <span class="icon icon-support" />
                <div class="title">{{ $t('SOLIDARITY') }}</div>
              </div>
              <div class="action">
                <p>{{ $t('TEXT_FOR_USER') }}</p>
                <p> {{ $t('PLEASE_CONTACT_US_TO_ASK') }}</p>
                <a href="mailto:erica@agatha-budget.fr" class="btn actionButton">{{ $t('PRICE_MONTHLY_SOLIDARITY') }}</a>
              </div>
            </div>
            <div class="offer">
              <div class="banner">
                <span class="icon icon-support" />
                <div class="title">{{ $t('INDIVIDUAL_COACHING') }}</div>
              </div>
              <div class="action">
                <p>{{ $t('TEXT_PERSONAL_SUPPORT') }}</p>
                <p>{{ $t('PLEASE_CONTACT_US_AND_RDV') }}</p>
                <a href="mailto:erica@agatha-budget.fr" class="btn actionButton">{{ $t('PRICE_COACHING_1H') }}</a>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="fixedFooter">
        <NavMenu/>
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
    this.managementPage = true// this.person?.hasBillingId
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
