<template>
  <div id="accountWidget">
    <div class="row title">
      <h1>{{$t('MY_ACCOUNTS')}}</h1>
    </div>
    <span class="total"> total : {{this.getEurosAmount(this.totalOnAccounts)}} €</span>
    <ul>
      <li class="accounts" v-for="account of this.$store.state.accounts" :key="account">
        <button class="btn" v-on:click="goToAccountPage(account)">{{ account.name }} : {{this.getEurosAmount(account.amount)}} €</button>
      </li>
      <li class="accountForm">
        <div v-if="!accountCreationFormIsDisplayed">
          <button class="btn displayFormBtn" v-on:click="changeAccountCreationFormDisplay" >
            <span >{{$t('ADD_ACCOUNT')}}</span>
          </button>
        </div>
        <div v-else class="closeBtnContainer">
          <button class="btn closeFormBtn" v-on:click="changeAccountCreationFormDisplay">
            <span>x</span>
          </button>
        </div>
        <div class="formContainer" v-if="accountCreationFormIsDisplayed">
          <AccountCreationForm @update-account-list="getAccounts" @close-form="changeAccountCreationFormDisplay" />
        </div>
      </li>
    </ul>
    </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import AccountCreationForm from '@/components/forms/AccountCreationForm.vue'
import router, { RouterPages } from '@/router'
import { Account } from '@/model/model'
import Utils from '@/utils/Utils'

interface AccountsWidgetData {
    accountCreationFormIsDisplayed: boolean;
}

export default defineComponent({
  name: 'AccountsWidget',
  components: {
    AccountCreationForm
  },
  data (): AccountsWidgetData {
    return {
      accountCreationFormIsDisplayed: false
    }
  },
  computed: {
    totalOnAccounts (): number {
      let total = 0
      for (const account of this.$store.state.accounts) {
        total += account.amount
      }
      return total
    }
  },
  methods: {
    goToAccountPage (account: Account) {
      router.push({ path: RouterPages.account, query: { accountId: account.id } })
    },
    changeAccountCreationFormDisplay () {
      this.$data.accountCreationFormIsDisplayed = !this.$data.accountCreationFormIsDisplayed
    },
    getEurosAmount (amount: number): number {
      return Utils.getEurosAmount(amount)
    }
  }
})
</script>
