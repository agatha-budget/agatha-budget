<template>
  <div :class="css">
    <div class="profilePage page">
      <div class="fixedHeader">
        <h1 class="title">{{ $t('PARAMETERS') }}</h1>
      </div>
      <div class="pageContent">
        <div class="centeredContent">
          <img id="logoface" alt="Vue logoface" src="../assets/logo_round.png"/>
          <div class="buttonList">
            <button class="navigationButton illustrated" v-on:click="goToSubscriptionPage">
              <span class="illustration fas fa-credit-card"/>
              <span class="illustrationLabel">{{ $t("SUBSCRIPTION") }}</span>
            </button>
            <button class="navigationButton illustrated" v-on:click="goToBanksPage">
              <span class="illustration fas fa-university"/>
              <span class="illustrationLabel">{{ $t("BANKS") }}</span>
            </button>
            <button class="navigationButton illustrated disabled">
              <span class="illustration fas fa-palette"/>
              <span class="illustrationLabel">{{ $t("APPEARANCE") }}</span>
            </button>
            <button class="navigationButton illustrated" v-on:click="logout">
              <span class="illustration fas fa-sign-out-alt"/>
              <span class="illustrationLabel">{{ $t("LOGOUT") }}</span>
            </button>
          </div>
          <div class="versionNumber">
            v.{{ version }}
          </div>
        </div>
      </div>
      <div class="fixedFooter">
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
