<template>
  <div :class="this.$store.state.css">
    <div class="profilpage">
      <div class="col-12 offset-0 col-sm-8 offset-sm-2 col-md-6 offset-md-3 col-lg-4 offset-lg-4 col-xxl-3 offset-xxl-5 ">
        <div class="image-container">
           <img id="logoface" alt="Vue logoface" src="../assets/logo_round.png" />
        </div>
         <h3 class="ProfilName">Nom Prenom</h3>
        <li>
            <ul>
              <a class="buttonprofile" v-on:click="disabled"><div class="icone fas fa-credit-card"></div>{{
                $t("SUBCRIPTION")
              }}</a>
            </ul>
            <ul>
              <a class="buttonprofile" v-on:click="disabled"><div class="icone fas fa-book"></div>{{
                $t("FREE_RESOURCE")
              }}</a>
            </ul>
            <ul>
              <a class="buttonprofile" v-on:click="disabled"><div class="icone fas fa-palette"></div>{{
                $t("APPEARANCE")
              }}</a>
            </ul>
            <ul>
              <a class="buttonprofile"  v-on:click="disabled"><div class="icone fab fa-discord"></div>{{
                $t("COMUNITY")
              }}</a>
            </ul>
            <ul>
              <a class="buttonprofile" v-on:click="logout"><div class="icone fas fa-power-off"></div>{{
                $t(id="LOGOUT")
              }}</a>
            </ul>
          </li>
          <div class="navmenu">
          <NavMenu :page="'profile'" />
          </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import NavMenu from '@/components/NavigationMenu.vue'
import PersonService from '@/services/PersonService'
import router, { RouterPages, redirectToLoginPageIfNotLogged } from '@/router'

export default defineComponent({
  name: 'profilePage',
  components: {
    NavMenu
  },
  methods: {
    logout () {
      PersonService.deleteSession(this.$store)
    },
    goToProfile () {
      router.push(RouterPages.profile)
    }
  }
})
</script>
