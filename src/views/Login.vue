<template>
  <div class="login_page">
    <img id="logo" alt="Vue logo" src="../assets/logo.png" />
    <input v-model="email" :placeholder="$t('EMAIL')">
    <input v-model="password" :placeholder="$t('PASSWORD')">
    <button v-on:click="login">{{$t('LOGIN')}}</button>
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
        this.$data.errorMsg = this.$t('SORRY_LOCKED_OUT') + new Date(responseData.unlockingDate)
      } else {
        this.$data.errorMsg = this.$t('SORRY_LOCKED_OUT')
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
