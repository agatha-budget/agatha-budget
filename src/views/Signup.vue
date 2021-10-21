<template>
  <div id="sign_up_page">
    <img id="logo" alt="Vue logo" src="../assets/logo.png" />
    <div>
      <label for="name">{{$t('NAME')}}</label>
      <input class="form-control" type="text" id="name" v-model="name" :placeholder="$t('NAME')">
      <label for="email">{{$t('EMAIL')}}</label>
      <input class="form-control" id="email" v-model="email" :placeholder="$t('EMAIL')">
      <label for="password">{{$t('PASSWORD')}}</label>
      <input class="form-control" type="password" id="password" v-model="password" :placeholder="$t('PASSWORD')">
      <label for="password">{{$t('CONFIRM_PASSWORD')}}</label>
      <input class="form-control" type="password" id="passwordConf" v-model="passwordConf" :placeholder="$t('PASSWORD')">
      <button class="btn btn-info" v-on:click="create">{{$t('CREATE')}}</button>
      <p id="login_error_msg">{{errorMsg}}</p>
      <p><a class="teamSelector" v-on:click="goToLogin">{{$t('RETURN_TO_LOGIN')}}</a></p>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import PersonService from '@/services/PersonService'
import router, { RouterPages } from '@/router'

export default defineComponent({
  name: 'Signup',
  components: {},
  data () {
    return {
      name: '',
      email: '',
      password: '',
      passwordConf: '',
      passwordLow: '',
      errorMsg: '',
      caractere: ''
    }
  },
  computed: {
    logged (): boolean {
      return this.$store.state.logged
    }
  },
  methods: {
    async create () {
      if (this.testForm()) {
        const responseData = PersonService.createPerson(this.$store, this.name, this.email, this.password)
      }
    },
    testForm (): boolean {
      const regex = /^\w+([.-]?\w+)*@\w+([.-]?\w+)*(\.\w{2,3})+$/
      if (this.password !== this.passwordConf) {
        this.$data.errorMsg = this.$t('DIFFERENT_PASSWORDS')
        return false
      }
      if (!this.passwordCheckSecurity(this.password)) {
        this.$data.errorMsg = this.$t('LESS_SECURITY')
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
      return true
    },
    passwordCheckSecurity (password: string): boolean {
      const regExp = /(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%&*()]).{8,}/
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
