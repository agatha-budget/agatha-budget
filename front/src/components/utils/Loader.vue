<template>
  <div class="loader-container">
    <h3 class="loaderTitle" v-if="message">{{message}}</h3>
    <h3 class="loaderTitle" v-else="message">{{ $t("DEFAULT_LOADING_MSG")}}</h3>
    <div class="image-spinner">
      <img src="@/assets/logo_round.png"/>
      <div class="spin spinner"></div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'

export default defineComponent({
  name: 'LoaderCmpt',
  props: {
    message: {
      type: String,
      required: false
    }
  },
})
</script>
