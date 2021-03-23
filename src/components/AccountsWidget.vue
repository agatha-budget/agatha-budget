<template>
  <div id="accountWidget">
    <ul>
      <li class="account" v-for="account, accountId in this.accountsList" :key="accountId">
        {{ account.name }} - {{account.amount}}
      </li>
    </ul>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'

interface AccountsWidgetData {
    accountsList: {
        [accountId: string]: {
          name: string;
          amount: number;
        };
    };
}

export default defineComponent({
  name: 'AccountsWidget',
  created: async function () {
    this.getAccounts()
  },
  data (): AccountsWidgetData {
    return {
      accountsList: {
        idaccount1: {
          name: 'courrant',
          amount: 125.25
        },
        idaccount2: {
          name: 'epargne',
          amount: 1250
        }
      }
    }
  },
  methods: {
    async getAccounts () {
      console.log('get accounts')
    }
  }
})
</script>
