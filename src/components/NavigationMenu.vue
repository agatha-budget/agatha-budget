<template>
    <div class="navBar">
      <ul class="list-group list-group-horizontal d-flex">
        <li v-if="fromPage == 'home' "><button class="navBarBtn btn home fas fa-home disabled" :title="$t('HOME')"/></li>
        <li v-else><button v-on:click="goHomePage" class="navBarBtn btn home fas fa-home" :title="$t('HOME')"/></li>
        <li v-if="fromPage == 'accounts' "><button class="navBarBtn btn account fas fa-euro-sign disabled" :title="$t('ACCOUNTS')"/></li>
        <li v-else><button v-on:click="goChooseAccount" class="navBarBtn btn account fas fa-euro-sign" :title="$t('ACCOUNTS')"/></li>
        <li v-if="fromPage == 'profile' "><button class="navBarBtn btn profile fas fa-cog disabled" :title="$t('PREFERENCES')"/></li>
        <li v-else><button v-on:click="goProfilePage" class="navBarBtn btn profile fas fa-cog" :title="$t('PREFERENCES')"/></li>
      </ul>
    </div>
</template>

<script lang="ts">
import router, { RouterPages } from '@/router';
import { defineComponent } from 'vue';

interface MenuData {
    fromPage: string;
}

export default defineComponent({
  name: 'NavMenu',
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
    goHomePage () {
      router.push(RouterPages.home)
    },
    goProfilePage () {
      router.push(RouterPages.profile)
    },
    goChooseAccount () {
      router.push(RouterPages.redirectToAccountPage)
    },
    goChartPage () {
      router.push(RouterPages.chartPage)
    }
  }
})
</script>
