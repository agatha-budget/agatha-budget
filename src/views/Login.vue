<template>
  <div class="login_page">
    <img id="logo" alt="Vue logo" src="../assets/logo.png" />
    <input v-model="email" placeholder="mail">
    <input v-model="password" placeholder="password">
    <button v-on:click="login">Login</button>
    <button v-on:click="logout">Logout</button>
    <p>User is logged as : {{userId}}</p>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { personService } from '../api/api'

export default defineComponent({
  name: 'Login',
  components: {},
  data () {
    return {
      email: '',
      password: ''
    }
  },
  computed: {
    userId (): string {
      return this.$store.state.userId
    }
  },
  methods: {
    login () {
      personService.createSession(this.$store, this.email, this.password, this.$store)
      this.$store.dispatch('login')
    },
    logout () {
      personService.deleteSession()
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
