<template>
  <div class="home">
    <img id="logo" alt="Vue logo" src="../assets/logo.png" />
    <Budget month="February" />
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import Budget from '@/components/Budget.vue' // @ is an alias to /src

export default defineComponent({
  name: 'Home',
  components: {
    Budget
  }
})
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped lang="less">
#logo {
  height: 80px;
}
</style>
