<template>
  <div id="login_page">
    <div v-on:keyup.enter="login" class="form">
      <img id="logo" alt="Vue logo" src="../assets/logo.png" />
      <input class="form-control" v-model="email" :placeholder="$t('EMAIL')">
      <input class="form-control" type="password" v-model="password" :placeholder="$t('PASSWORD')">
      <button class="navigationButton btn" v-on:click="login">{{$t('LOGIN')}}</button>
      <p>{{errorMsg}}</p>
      <button id="toOtherForm" class="navigationButton btn" v-on:click="goToSignUp">{{$t('CREATE_ACCOUNT_HERE')}}</button>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import PersonService from '@/services/PersonService'
import router, { RouterPages } from '@/router'

export default defineComponent({
  name: 'LoginView',
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
          this.$data.errorMsg = this.$t('SORRY_LOCKED_OUT', { lockingDuration })
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
