<template >
  <div :class="this.css">
    <div id="nav">
      <a href="#" v-on:click="logout">Logout</a>
      <router-link to="/about">About</router-link>
    </div>
    <div class="home row">
      <Budget month="February" class="col-md-4 offset-md-2"/>
      <div class="col-md-4 offset-md-2">
      <AccountsWidget/>
      <button>Graphs and Reports</button>
      <button>Preferences</button>
      <button>Log out</button>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import Budget from '@/components/Budget.vue' // @ is an alias to /src
import { useStore } from '@/store/index'
import router from '@/router'
import { personService } from '@/services/PersonService'
import AccountsWidget from '@/components/AccountsWidget.vue'

export default defineComponent({
  name: 'Home',
  components: {
    Budget,
    AccountsWidget
  },
  data () {
    return {
      css: 'blue'
    }
  },
  setup () {
    if (!useStore().state.logged) {
      router.push('/login')
    }
  },
  methods: {
    logout () {
      personService.deleteSession(this.$store)
    }
  }
})
</script>
