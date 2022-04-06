<template>
  <div :class="this.$store.state.css">
    <div class="profilPage">
      <div class="boxMenu col-12 offset-0 col-sm-8 offset-sm-2 col-md-6 offset-md-3 col-lg-4 offset-lg-4 col-xxl-3 offset-xxl-5 ">
        <div class="image-container">
           <img id="logoface" alt="Vue logoface" src="../assets/logo_round.png" />
        </div>
        <li>
            <ul>
              <a class="buttonProfile row" v-on:click="goSubscriptionPage">
                <div class="icone fas fa-credit-card col-4"></div>
                <div class="label col-8">{{ $t("SUBSCRIPTION") }}</div>
              </a>
            </ul>
            <ul>
              <a class="buttonProfile disabled row">
                <div class="icone fas fa-book col-4"></div>
                <div class="label col-8">{{ $t("FREE_RESOURCES") }}</div>
              </a>
            </ul>
            <ul>
              <a class="buttonProfile disabled row">
                <div class="icone fas fa-palette col-4"></div>
                <div class="label col-8">{{ $t("APPEARANCE") }}</div>
              </a>
            </ul>
            <ul>
              <a class="buttonProfile row" href="https://forum.agatha-budget.fr">
                <div class="icone fas fa-question col-4"></div>
                <div class="label col-8">{{ $t("SUPPORT") }}</div>
              </a>
            </ul>
          </li>
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
