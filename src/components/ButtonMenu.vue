<template>
    <div class="buttonMenu">
        <ul id="actionsList" class="list-group list-group-horizontal d-flex justify-content-center">
        <li><button v-on:click="goHomePage" class="btn fas fa-home" :title="$t('HOME')"/></li>
        <li><button class="btn fas fa-chart-line disabled" :title="$t('GRAPH_AND_REPORT')"/></li>
        <li><button class="btn fas fa-cog disabled" :title="$t('PREFERENCES')"/></li>
        <li><button v-on:click="logout" class="btn fas fa-sign-out-alt" :title="$t('LOGOUT')"/></li>
      </ul>
    </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'

export default defineComponent({
  name: 'BtnMenu'
})
</script>
