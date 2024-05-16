<template>
  <div :class="css">
    <div class="profilePage menuLayout row col-md-8 offset-md-2 col-xxl-6 offset-xxl-3">
        <div class="header fixed title">
          {{ $t('PARAMETERS') }}
        </div>
        <div class="placeholder top">
          {{ $t('PARAMETERS') }}
        </div>
        <div class="content container">
          <img id="logoface" alt="Vue logoface" src="../assets/logo_round.png" />
          <button class="navigationButton row" v-on:click="goToSubscriptionPage">
            <span class="illustration fas fa-credit-card col-4"/>
            <span class="illustrationLabel col-8">{{ $t("SUBSCRIPTION") }}</span>
          </button>
          <button class="navigationButton row" v-on:click="goToBanksPage">
            <span class="illustration fas fa-university col-4"/>
            <span class="illustrationLabel col-8">{{ $t("BANKS") }}</span>
          </button>
          <button class="navigationButton disabled row">
            <span class="illustration fas fa-book-open col-4"/>
            <span class="illustrationLabel col-8">{{ $t("FREE_RESOURCES") }}</span>
          </button>
          <button class="navigationButton disabled row">
            <span class="illustration fas fa-palette col-4"/>
            <span class="illustrationLabel col-8">{{ $t("APPEARANCE") }}</span>
          </button>
          <a class="navigationButton row" href="https://forum.agatha-budget.fr">
            <span class="illustration fas fa-question col-4"/>
            <span class="illustrationLabel col-8">{{ $t("SUPPORT") }}</span>
          </a>
          <button class="navigationButton row" v-on:click="logout">
            <span class="illustration fas fa-sign-out-alt col-4"/>
            <span class="illustrationLabel col-8">{{ $t("LOGOUT") }}</span>
          </button>
        </div>
        <div class="versionNumber">
          v.{{ version }}
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
import Properties from "@/../properties";
import NavMenu from '@/components/NavigationMenu.vue';
import router, { RouterPages } from '@/router';
import KeycloakService from "@/services/security/KeycloakService";
import { usePersonStore } from '@/stores/personStore';
import { defineComponent } from 'vue';

export default defineComponent({
  name: 'ProfileView',
  components: { NavMenu },
  created: async function () {
    usePersonStore().init()
  },
  data () {
    return { }
  },
  computed: {
    css (): string {
      return usePersonStore().css
    },
    version(): string {
      return Properties.commitHash.slice(0,6)
    }
  },
  methods: {
    logout () {
      KeycloakService.CallLogOut();
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
