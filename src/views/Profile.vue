<template>
  <div :class="this.$store.state.css">
    <div class="profilePage">
      <div class="boxMenu col-12 offset-0 col-md-6 offset-md-3 col-lg-4 offset-lg-4 col-xxl-3 offset-xxl-5 ">
        <div class="image-container">
           <img id="logoface" alt="Vue logoface" src="../assets/logo_round.png" />
        </div>
        <div class="title">{{ $t('PREFERENCES') }}</div>
        <div class="buttonContainer row">
          <btn class="navigationButton col-10 offset-1 col-sm-6 offset-sm-3 col-md-10 offset-md-1 row" v-on:click="goSubscriptionPage">
            <span class="illustration fas fa-credit-card col-4"/>
            <div class="name col-8">{{ $t("SUBSCRIPTION") }}</div>
          </btn>
          <btn class="navigationButton disabled col-10 offset-1 col-sm-6 offset-sm-3 col-md-10 offset-md-1 row">
            <span class="illustration fas fa-book-open col-4"/>
            <div class="name col-8">{{ $t("FREE_RESOURCES") }}</div>
          </btn>
          <btn class="navigationButton disabled col-10 offset-1 col-sm-6 offset-sm-3 col-md-10 offset-md-1 row">
            <span class="illustration fas fa-chart-area col-4"/>
            <div class="name col-8">{{ $t("GRAPH") }}</div>
          </btn>
          <btn class="navigationButton disabled col-10 offset-1 col-sm-6 offset-sm-3 col-md-10 offset-md-1 row">
            <span class="illustration fas fa-palette col-4"/>
            <div class="name col-8">{{ $t("APPEARANCE") }}</div>
          </btn>
          <a class="navigationButton col-10 offset-1 col-sm-6 offset-sm-3 col-md-10 offset-md-1 row" href="https://forum.agatha-budget.fr">
            <span class="illustration fas fa-question col-4"/>
            <div class="name col-8">{{ $t("SUPPORT") }}</div>
          </a>
        </div>
      </div>
    </div>
    <div class="navMenu">
      <NavMenu :page="'profile'" />
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
    goSubscriptionPage () {
      router.push(RouterPages.subscription)
    }
  }
})
</script>
