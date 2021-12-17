<template>
    <div class="buttonMenu">
      <ul id="actionsList" class="list-group list-group-horizontal d-flex">
        <li v-if="this.fromPage == 'home' "><button class="btn fas fa-home disabled" :title="$t('HOME')"/></li>
        <li v-else><button v-on:click="goHomePage" class="btn fas fa-home" :title="$t('HOME')"/></li>
        <li v-if="this.fromPage == 'redirect' "><button class="btn fas fa-euro-sign disabled" :title="$t('ACCOUNT')"/></li>
        <li v-else><button v-on:click="goChooseAccount" class="btn fas fa-euro-sign" :title="$t('ACCOUNT')"/></li>
        <li><button class="btn fas fa-cog disabled" :title="$t('PREFERENCES')"/></li>
        <li><button v-on:click="logout" class="btn fas fa-sign-out-alt" :title="$t('LOGOUT')"/></li>
      </ul>
    </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import PersonService from '@/services/PersonService'
import router, { RouterPages } from '@/router'

interface MenuData {
    fromPage: string;
}

export default defineComponent({
  name: 'BtnMenu',
  props: {
    page: {
      type: String,
      required: true
    }
  },
  data (): MenuData {
    return {
      fromPage: this.$props.page
    }
  },
  methods: {
    logout () {
      PersonService.deleteSession(this.$store)
    },
    async goHomePage (): Promise<void> {
      router.push(RouterPages.home)
    },
    async goChooseAccount () {
      router.push(RouterPages.redirectAtAccountPage)
    }
  }
})
</script>
