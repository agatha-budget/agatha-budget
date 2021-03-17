<template>
  <div id="nav">
    <a href="#" v-on:click="logout">Logout</a>
    <router-link to="/about">About</router-link>
  </div>
  <div class="home">
    <Budget month="February" />
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import Budget from '@/components/Budget.vue' // @ is an alias to /src
import { useStore } from '@/store/index'
import router from '@/router'
import { personService } from '@/services/PersonService'

export default defineComponent({
  name: 'Home',
  components: {
    Budget
  },
  setup () {
    if (!useStore().state.logged) {
      router.push('/login')
    }
  },
  methods: {
    logout () {
      personService.deleteSession(this.$store)
    }
  }
})
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped lang="less">
#logo {
  height: 80px;
}
</style>
