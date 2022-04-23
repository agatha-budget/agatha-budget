<template>
  <div :class="this.$store.state.css">
    <div class="profilePage">
      <div class="container">
        <img id="logoface" alt="Vue logoface" src="../assets/logo_round.png" />
        <h1 class="title">{{ $t('PREFERENCES') }}</h1>
        <btn class="navigationButton row" v-on:click="goSubscriptionPage">
          <span class="illustration fas fa-credit-card col-4"/>
          <span class="illustrationLabel col-8">{{ $t("SUBSCRIPTION") }}</span>
        </btn>
        <btn class="navigationButton disabled row">
          <span class="illustration fas fa-book-open col-4"/>
          <span class="illustrationLabel col-8">{{ $t("FREE_RESOURCES") }}</span>
        </btn>
        <btn class="navigationButton disabled row">
          <span class="illustration fas fa-chart-area col-4"/>
          <span class="illustrationLabel col-8">{{ $t("GRAPH") }}</span>
        </btn>
        <btn class="navigationButton disabled row">
          <span class="illustration fas fa-palette col-4"/>
          <span class="illustrationLabel col-8">{{ $t("APPEARANCE") }}</span>
        </btn>
        <a class="navigationButton row" href="https://forum.agatha-budget.fr">
          <span class="illustration fas fa-question col-4"/>
          <span class="illustrationLabel col-8">{{ $t("SUPPORT") }}</span>
        </a>
      </div>
    </div>
      <NavMenu :page="'profile'" />
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
    goSubscriptionPage () {
      router.push(RouterPages.subscription)
    }
  }
})
</script>
