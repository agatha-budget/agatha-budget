<template>
  <div id="sign_up_page">
    <img id="logo" alt="Vue logo" src="../assets/logo.png" />
    <input class="for;-control" type="text" v-model="name" :placeholder="$t('NAME')">
    <input class="form-control" v-model="email" :placeholder="$t('EMAIL')">
    <input class="form-control" type="password" v-model="password" :placeholder="$t('PASSWORD')">
    <input class="form-control" type="password" v-model="password" :placeholder="$t('PASSWORD')">
    <button class="btn btn-info" v-on:click="create">{{$t('CREATE')}}></button>
    <p id="login_error_msg">{{errorMsg}}</p>
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
    /* async signup () {
    }
*/
    /*
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
    } */
  }

})
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped lang="less">
#logo {
  height: 80px;
}
</style>
