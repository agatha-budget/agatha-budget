<template>
    <div class="buttonMenu">
      <ul id="actionsList" class="list-group list-group-horizontal d-flex">
        <li><button v-on:click="goHomePage" class="btn fas fa-home" :title="$t('HOME')"/></li>
        <li><button class="btn fas fa-euro-sign" :title="$t('ACCOUNT')"/></li>
        <li><button class="btn fas fa-cog disabled" :title="$t('PREFERENCES')"/></li>
        <li><button v-on:click="logout" class="btn fas fa-sign-out-alt" :title="$t('LOGOUT')"/></li>
      </ul>
    </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import PersonService from '@/services/PersonService'
import router, { RouterPages } from '@/router'

export default defineComponent({
  name: 'BtnMenu',
  methods: {
    logout () {
      PersonService.deleteSession(this.$store)
    },
    async goHomePage (): Promise<void> {
      router.push(RouterPages.home)
    }
  }
})
</script>
