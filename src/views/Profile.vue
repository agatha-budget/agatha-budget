<template>
  <div :class="this.$store.state.css">
    <div class="profilePage menuLayout row col-md-8 offset-md-2 col-xxl-6 offset-xxl-3">
        <div class="header fixed title">
          {{ $t('PARAMETERS') }}
        </div>
        <div class="placeholder top">
          {{ $t('PARAMETERS') }}
        </div>
        <div class="content container">
          <img id="logoface" alt="Vue logoface" src="../assets/logo_round.png" />
          <btn class="navigationButton row" v-on:click="goToSubscriptionPage">
            <span class="illustration fas fa-credit-card col-4"/>
            <span class="illustrationLabel col-8">{{ $t("SUBSCRIPTION") }}</span>
          </btn>
          <btn class="navigationButton row" v-on:click="goToBanksPage">
            <span class="illustration fas fa-university col-4"/>
            <span class="illustrationLabel col-8">{{ $t("BANKS") }}</span>
          </btn>
          <btn class="navigationButton disabled row">
            <span class="illustration fas fa-book-open col-4"/>
            <span class="illustrationLabel col-8">{{ $t("FREE_RESOURCES") }}</span>
          </btn>
          <btn class="navigationButton disabled row">
            <span class="illustration fas fa-palette col-4"/>
            <span class="illustrationLabel col-8">{{ $t("APPEARANCE") }}</span>
          </btn>
          <a class="navigationButton row" href="https://forum.agatha-budget.fr">
            <span class="illustration fas fa-question col-4"/>
            <span class="illustrationLabel col-8">{{ $t("SUPPORT") }}</span>
          </a>
          <btn class="navigationButton row" v-on:click="logout">
            <span class="illustration fas fa-sign-out-alt col-4"/>
            <span class="illustrationLabel col-8">{{ $t("LOGOUT") }}</span>
          </btn>
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
import { defineComponent } from 'vue'
import NavMenu from '@/components/NavigationMenu.vue'
import PersonService from '@/services/PersonService'
import StoreHandler from '@/store/StoreHandler'
import router, { RouterPages } from '@/router'

export default defineComponent({
  name: 'profilePage',
  components: { NavMenu },
  created: async function () {
    StoreHandler.initStore(this.$store)
  },
  data () {
    return { }
  },
  methods: {
    logout () {
      PersonService.deleteSession(this.$store)
    },
    goToSubscriptionPage () {
      router.push(RouterPages.subscription)
    },
    goToBanksPage () {
      router.push(RouterPages.banks)
    }
  }
})
</script>
