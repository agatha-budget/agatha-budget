<template >
  <div :class="this.css">
    <div class="home row">
      <h1>test - </h1>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { redirectToLoginPageIfNotLogged } from '@/router'
import AccountsWidget from '@/components/AccountsWidget.vue'
import { accountService } from '@/services/AccountService'
import { Account, Budget } from '@/model/model'


interface AccountPageData {
    account: Account;
    operations: any;
}

export default defineComponent({
  name: 'AccountPage',
  components: {
    AccountsWidget
  },
  created: async function () {
    redirectToLoginPageIfNotLogged(this.$store)
  },
  props: ['accountId'],
  data (): AccountPageData {
    return {
      account: {
        id: this.accountId,
        name: '',
        amount: 0
      },
      operations: {}
    }
  },
  methods: {
    async getAccountOperation () {
      accountService.getAccount(this.accountId)
    }
  }
})
</script>
