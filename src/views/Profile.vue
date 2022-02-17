<template>
  <div id="profile_page" :class="this.$store.state.css">
    <div class="card-container">
      <div class="upper-container"></div>
      <div class="lower-container">
        <div class="container-cadre">
          <div class="image-container">
           <img id="logoface" alt="Vue logoface" src="../assets/logo_round.png" />
        </div>
            <h3 class="ProfilName">Nom Prenom</h3>
        <div>
          <header>
          <li>
            <ul>
              <a class="btnprofil fas fa-credit-card" v-on:click="goToBillingPortal">{{
                $t("SUBCRIPTION")
              }}</a>
            </ul>
            <ul>
              <a class="btnprofil fas fa-book" v-on:click="goToBillingPortal">{{
                $t("FREE_RESOURCE")
              }}</a>
            </ul>
            <ul>
              <a class="btnprofil fas fa-palette" v-on:click="apearance">{{
                $t("APPEARANCE")
              }}</a>
            </ul>
            <ul>
              <a class="btnprofil fab fa-discord"  v-on:click="goToBillingPortal">{{
                $t("COMUNITY")
              }}</a>
            </ul>
            <ul>
              <a class="btnprofil fas fa-power-off" v-on:click="login">{{
                $t(id="LOGOUT")
              }}</a>
            </ul>
          </li>
          </header>
          </div>
          </div>
    <div class="navprofile">
    <NavMenu :page="'profile'" />
    </div>
        </div>
      </div>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import PersonService from '@/services/PersonService'
import NavMenu from '@/components/NavigationMenu.vue'
import router, { RouterPages } from '@/router'

export default defineComponent({
  name: 'Profile',
  components: { NavMenu },
  data () {
    return {}
  },
  methods: {
    async goToBillingPortal (): Promise<void> {
      PersonService.redirectToBillingPortalUrl()
    },
    logout () {
      PersonService.deleteSession(this.$store)
    },
    goChooseAccount () {
      router.push(RouterPages.redirectToAccountPage)
    }
  }
})
</script>
