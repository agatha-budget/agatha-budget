<template>
  <div :class="this.$store.state.css">
    <div class="profilPage">
      <div class="boxMenu col-12 offset-0 col-sm-8 offset-sm-2 col-md-6 offset-md-3 col-lg-4 offset-lg-4 col-xxl-3 offset-xxl-5 ">
        <div class="image-container">
           <img id="logoface" alt="Vue logoface" src="../assets/logo_round.png" />
        </div>
         <h3 class="profilName">{{$t("LASTNAME_FIRSTNAME")}}</h3>
        <li>
            <ul>
              <a class="buttonProfile" v-on:click="goToBillingPortal"><div class="icone fas fa-credit-card"></div>{{
                $t("SUBCRIPTION")
              }}</a>
            </ul>
            <ul>
              <a class="buttonProfile disabled"><div class="icone fas fa-book"></div>{{
                $t("FREE_RESOURCE")
              }}</a>
            </ul>
            <ul>
              <a class="buttonProfile disabled"><div class="icone fas fa-palette"></div>{{
                $t("APPEARANCE")
              }}</a>
            </ul>
            <ul>
              <a class="buttonProfile disabled"  ><div class="icone fab fa-discord"></div>{{
                $t("COMMUNITY")
              }}</a>
            </ul>
            <ul>
              <a class="buttonProfile" v-on:click="logout"><div class="icone fas fa-power-off"></div>{{
                $t("LOGOUT")
              }}</a>
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

export default defineComponent({
  name: 'profilePage',
  components: {
    NavMenu
  },
  methods: {
    logout () {
      PersonService.deleteSession(this.$store)
    },
    async goToBillingPortal (): Promise<void> {
      PersonService.redirectToBillingPortalUrl()
    }
  }
})
</script>
