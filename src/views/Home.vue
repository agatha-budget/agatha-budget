<template >
  <div :class="this.css">
    <div class="home row">
      <Budget month="February" class="col-md-4 offset-md-2"/>
      <div class="col-md-4 offset-md-2">
      <AccountsWidget/>
      <ul id="actionsList">
        <li><button>Graphs and Reports</button></li>
        <li><button>Preferences</button></li>
        <li><button v-on:click="logout">Log out</button></li>
      </ul>
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
