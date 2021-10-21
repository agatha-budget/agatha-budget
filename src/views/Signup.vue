<template>
  <div id="sign_up_page">
    <img id="logo" alt="Vue logo" src="../assets/logo.png" />
    <div>
      <label for="name">{{$t('ENTER_NAME')}}</label>
      <input class="form-control" type="text" id="name" v-model="name" :placeholder="$t('NAME')">
      <label for="email">{{$t('ENTER_EMAIL')}}</label>
      <input class="form-control" id="email" v-model="email" :placeholder="$t('EMAIL')">
      <label for="password">{{$t('ENTER_PASSWORD')}}</label>
      <input class="form-control" type="password" id="password" v-model="password" :placeholder="$t('PASSWORD')">
      <label for="password">{{$t('REPEAT_PASSWORD')}}</label>
      <input class="form-control" type="password" id="passwordConf" v-model="passwordConf" :placeholder="$t('PASSWORD')">
      <button class="btn btn-info" v-on:click="create">{{$t('CREATE')}}</button>
      <p id="login_error_msg">{{errorMsg}}</p>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import PersonService from '@/services/PersonService'

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
    create () {
      const regex = /^\w+([.-]?\w+)*@\w+([.-]?\w+)*(\.\w{2,3})+$/
      if (this.name === '') { alert('nom vide') }
      if (!this.email.match(regex)) { alert('adresse mail invalide') }
      if (!this.passwordCheckSecurity(this.password)) { alert('pas assez securise') }
      if (this.password !== this.passwordConf) { alert('ce ne sont pas les memes mdp') }
    },
    passwordCheckSecurity (password: string): boolean {
      const regExp = /(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%&*()]).{8,}/
      const validPassword = regExp.test(password)
      return validPassword
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
