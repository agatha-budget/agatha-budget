<template>
  <div class="login_page">
    <img id="logo" alt="Vue logo" src="../assets/logo.png" />
    <input v-model="username" placeholder="id">
    <input v-model="password" placeholder="password">
    <button v-on:click="login">Login</button>
    <button v-on:click="logout">Logout</button>
    <p>User is logged : {{logged}}</p>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'

export default defineComponent({
  name: 'Login',
  components: {},
  data () {
    return {
      username: '',
      password: ''
    }
  },
  computed: {
    logged (): boolean {
      return this.$store.state.logged
    }
  },
  methods: {
    login () {
      this.$store.dispatch('login')
    },
    logout () {
      this.$store.dispatch('logout')
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
