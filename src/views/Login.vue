<template>
  <div id="login_page" class="col-2 offset-5">
    <img id="logo" alt="Vue logo" src="../assets/logo.png" />
    <input class="form-control" v-model="email" :placeholder="$t('EMAIL')">
    <input class="form-control" v-model="password" :placeholder="$t('PASSWORD')">
    <button class="btn btn-outline-info" v-on:click="login">{{$t('LOGIN')}}</button>
    <p id="login_error_msg">{{errorMsg}}</p>
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
