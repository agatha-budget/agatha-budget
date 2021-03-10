<template>
  <div class="login_page">
    <img id="logo" alt="Vue logo" src="../assets/logo.png" />
    <input v-model="email" placeholder="mail">
    <input v-model="password" placeholder="password">
    <button v-on:click="login">Login</button>
    <p>{{errorMsg}}</p>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { personService } from '@/services/PersonService'

export default defineComponent({
  name: 'Login',
  components: {},
  data () {
    return {
      email: '',
      password: '',
      errorMsg: ''
    }
  },
  computed: {
    logged (): boolean {
      return this.$store.state.logged
    }
  },
  methods: {
    async login () {
      const responseData = await personService.createSession(this.$store, this.email, this.password, this.$store)
      if (responseData.unlockingDate !== null) {
        this.$data.errorMsg = 'Sorry, you are locked out until : ' + new Date(responseData.unlockingDate)
      } else {
        this.$data.errorMsg = 'Invalid email/password combination'
      }
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
