<template>
    <div class="navBar">
      <ul class="list-group list-group-horizontal d-flex">
        <li v-if="this.fromPage == 'home' "><button class="navBarBtn btn home fas fa-home disabled" :title="$t('HOME')"/></li>
        <li v-else><button v-on:click="goHomePage" class="navBarBtn btn home fas fa-home" :title="$t('HOME')"/></li>
        <li v-if="this.fromPage == 'chart' "><button class="navBarBtn btn chart fas fa-chart-line disabled" :title="$t('CHART')"/></li>
        <li v-else><button v-on:click="goChartPage" class="navBarBtn btn chart fas fa-chart-line" :title="$t('CHART')"/></li>
        <li v-if="this.fromPage == 'accounts' "><button class="navBarBtn btn account fas fa-euro-sign disabled" :title="$t('ACCOUNTS')"/></li>
        <li v-else><button v-on:click="goChooseAccount" class="navBarBtn btn account fas fa-euro-sign" :title="$t('ACCOUNTS')"/></li>
        <li v-if="this.fromPage == 'profile' "><button class="navBarBtn btn profile fas fa-cog disabled" :title="$t('PREFERENCES')"/></li>
        <li v-else><button v-on:click="goProfilePage" class="navBarBtn btn profile fas fa-cog" :title="$t('PREFERENCES')"/></li>
      </ul>
    </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import router, { RouterPages } from '@/router'

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
