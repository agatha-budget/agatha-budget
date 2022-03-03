<template>
  <div :class="this.$store.state.css">
    <div id="subscriptionPage" class="col-12 offset-0 col-sm-10 offset-sm-1 col-md-8 offset-md-2 col-lg-6 offset-lg-3">
      <div v-if="profileTest == 'user'" class="user">
        <p class="col-12">{{ $t('TEXT_1_MONTH_FREE') }}</p>
        <div class="essential row">
          <span class="icon icon-plane col-12" />
          <h1 class="title col-12">{{ $t('ESSENTIAL') }}</h1>
          <p class=" col-12">{{ $t('TEXT_ESSENTIAL') }}</p>
          <div class="btnSubscription col-6">
            <button v-on:click="this.goToBillingPortal('MONTHLY_ESSENTIAL')">{{ $t('PRICE_MONTHLY_ESSENTIAL') }}</button>
          </div>
          <div class="btnSubscription col-6">
            <button v-on:click="this.goToBillingPortal('ANNUAL_ESSENTIAL')">{{ $t('PRICE_ANNUAL_ESSENTIAL') }}</button>
          </div>
        </div>
        <div class="integral">
          <span class="icon icon-paper-plane col-12" />
          <h1 class="title col-12">{{ $t('INTEGRAL') }}</h1>
          <p class="col-12">{{ $t('TEXT_INTEGRAL') }}</p>
          <div class="btnSubscription col-6">
            <button v-on:click="this.goToBillingPortal('MONTHLY_INTEGRAL')">{{ $t('PRICE_MONTHLY_INTEGRAL') }}</button>
          </div>
          <div class="btnSubscription col-6">
            <button v-on:click="this.goToBillingPortal('ANNUAL_INTEGRAL')">{{ $t('PRICE_ANNUAL_INTEGRAL') }}</button>
          </div>
        </div>
        <div class="solidarity">
          <span class="icon icon-support col-12" />
          <h1 class="title col-12">{{ $t('SOLIDARITY') }}</h1>
          <p class="col-12" v-on:click="this.goToContactPage">{{ $t('TEXT_FOR_USER') }}{{ $t('PLEASE_CONTACT_US') }}</p>
          <div class="btnSubscription disabled col-12">
            <button v-on:click="this.goToContactPage">{{ $t('PRICE_MONTHLY_SOLIDARITY') }}</button>
          </div>
        </div>
      </div>

      <div v-if="profileTest == 'company'" class="company">
        <p class="col-12">{{ $t('TEXT_1_MONTH_FREE') }}</p>
        <div class="businessSide">
          <span class="icon icon-briefcase col-12" />
          <h1 class="title col-12">{{ $t('BUSINESS_SIDE') }}</h1>
          <p class="col-12">{{ $t('TEXT_COMPANY') }}</p>
          <div class="btnSubscription col-6">
            <button v-on:click="this.goToBillingPortal('MONTHLY_COMPANY')">{{ $t('PRICE_MONTHLY_COMPANY') }}</button>
          </div>
          <div class="btnSubscription col-6">
            <button v-on:click="this.goToBillingPortal('ANNUAL_COMPANY')">{{ $t('PRICE_ANNUAL_COMPANY') }}</button>
          </div>
        </div>
        <div class="personalSide">
          <span class="icon icon-home col-12" />
          <h1 class="title col-12">{{ $t('PERSONAL_SIDE') }}</h1>
          <p class="col-12" v-on:click="this.goToContactPage">{{ $t('TEXT_PERSONAL_SIDE') }}{{ $t('PLEASE_CONTACT_US') }}</p>
          <div class="btnSubscription disabled col-6">
            <button v-on:click="this.goToContactPage">{{ $t('PRICE_PERSONAL_SIDE_MONTHLY') }}</button>
          </div>
          <div class="btnSubscription disabled col-6">
            <button v-on:click="this.goToContactPage">{{ $t('PRICE_PERSONAL_SIDE_ANNUAL') }}</button>
          </div>
        </div>
        <div class="coaching">
          <span class="icon icon-compass col-12" />
          <h1 class="title col-12">{{ $t('PERSONAL_SUPPORT') }}</h1>
          <p class="col-12" v-on:click="this.goToContactPage">{{ $t('TEXT_PERSONAL_SUPPORT') }}{{ $t('PLEASE_CONTACT_US_AND_RDV') }}</p>
          <div class="btnSubscription disabled col-6">
            <button v-on:click="this.goToContactPage">{{ $t('PRICE_COACHING_1H') }}</button>
          </div>
          <div class="btnSubscription disabled col-6">
            <button v-on:click="this.goToContactPage">{{ $t('PRICE_COACHING_5H') }}</button>
          </div>
        </div>
      </div>
      <button v-on:click="changerProfileTest">changer de profil</button>
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
      PersonService.redirectToBillingPortalUrl(selectedPackage)
    },
    goToContactPage () {
      window.location.href = 'https://agatha-budget.fr/contact/'
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
