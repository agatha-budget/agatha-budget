<template>
  <div id="login_page">
    <img id="logo" alt="Vue logo" src="../assets/logo.png" />
    <input class="form-control" v-model="email" :placeholder="$t('EMAIL')">
    <input class="form-control" type="password" v-model="password" :placeholder="$t('PASSWORD')">
    <button class="btn btn-info" v-on:click="login">{{$t('LOGIN')}}</button>
    <p id="login_error_msg">{{errorMsg}}</p>
    <p><a class="teamSelector" v-on:click="goToSignUp">{{$t('CREATE_ACCOUNT_HERE')}}</a></p>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import PersonService from '@/services/PersonService'
import router, { RouterPages } from '@/router'

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
  methods: {
    async login () {
      const responseData = await PersonService.createSession(this.$store, this.email, this.password)
      if (responseData.unlockingDate !== null) {
        const minutes = 1000 * 60
        const lockingDuration = Math.round((responseData.unlockingDate - new Date().getTime()) / minutes)
        if (lockingDuration > 0) {
          this.$data.errorMsg = this.$t('SORRY_LOCKED_OUT', { lockingDuration: lockingDuration })
        } else {
          this.$data.errorMsg = this.$t('SORRY_WRONG_PASSWORD')
        }
      } else {
        this.$data.errorMsg = this.$t('SORRY_WRONG_LOGIN')
      }
    },
    async goToSignUp (): Promise<void> {
      router.push(RouterPages.signup)
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
