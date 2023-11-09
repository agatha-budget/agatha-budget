<template>
  <div id="sign_up_page">
    <div class="form">
      <img id="logo" alt="Vue logo" src="../assets/logo.png" />
      <input class="form-control" type="textInput" id="name" v-model="name" :placeholder="$t('NAME')">
      <input class="form-control" id="email" v-model="email" :placeholder="$t('EMAIL')">
      <input class="form-control" type="password" id="password" v-model="password" :placeholder="$t('PASSWORD')">
      <input class="form-control" type="password" id="passwordConfirm" v-model="passwordConfirm" :placeholder="$t('PASSWORD_CONFIRMATION')">
      <select class="form-control" id="selectProfile" v-model="profile">
        <option disabled value="">{{$t('CHOOSE_PROFILE')}}</option>
        <option value="PROFILE_USER">{{$t('USER')}}</option>
        <option value="PROFILE_COMPANY">{{$t('COMPANY')}}</option>
      </select>
      <button class="navigationButton btn" v-on:click="create">{{$t('CREATE_MY_ACCOUNT')}}</button>
      <p>{{errorMsg}}</p>
      <button id="toOtherForm" class="navigationButton btn" v-on:click="goToLogin">{{$t('LOGIN')}}</button>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import PersonService from '@/services/PersonService'
import router, { RouterPages } from '@/router'
import { usePersonStore } from '@/stores/personStore'


export default defineComponent({
  name: 'SignupView',
  components: {},
  data () {
    return {
      name: '',
      email: '',
      password: '',
      passwordConfirm: '',
      errorMsg: '',
      profile: ''
    }
  },
  computed: {
    logged (): boolean {
      return usePersonStore().logged
    }
  },
  methods: {
    async create () {
      if (this.testForm()) {
        PersonService.createPerson(this.name, this.email, this.password, this.profile)
      }
    },
    testForm (): boolean {
      // regex verify email have an email form
      const regex = /^\w+([.-]?\w+)*@\w+([.-]?\w+)*(\.\w{2,3})+$/
      if (this.password !== this.passwordConfirm) {
        this.$data.errorMsg = this.$t('DIFFERENT_PASSWORDS')
        return false
      }
      if (!this.passwordCheckSecurity(this.password)) {
        this.$data.errorMsg = this.$t('INSUFFICIENT_SECURITY')
        return false
      }
      if (!this.email.match(regex)) {
        this.$data.errorMsg = this.$t('INVALID_EMAIL')
        return false
      }
      if (this.name === '') {
        this.$data.errorMsg = this.$t('EMPTY_NAME')
        return false
      }
      if (this.profile === '') {
        this.$data.errorMsg = this.$t('EMPTY_PROFILE')
        return false
      }
      return true
    },
    passwordCheckSecurity (password: string): boolean {
      // regExp verify password is enought complex (more than 8 caracters, with lower and capital letter and with special caracter !@#$%&*())
      const regExp = /(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%&*()-_<>/\\]).{8,}/
      const validPassword = regExp.test(password)
      return validPassword
    },
    async goToLogin (): Promise<void> {
      router.push(RouterPages.login)
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
