<template>
  <div id="accountWidget">
    <div class="row">
    <h3 class="col-10">{{$t('MY_ACCOUNTS')}}</h3>
    </div>
    <ul>
      <li class="account" v-for="account, accountId in this.accounts" :key="accountId">
        <button class="btn" v-on:click="goToAccountPage(account)">{{ account.name }} - {{account.amount}} €</button>
      </li>
      <li>
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
import { AccountList, Budget } from '@/model/model'
import { accountService } from '@/services/AccountService'
import AccountCreationForm from '@/components/forms/AccountCreationForm.vue'
import router, { RouterPages } from '@/router'

interface AccountsWidgetData {
    accountCreationFormIsDisplayed: boolean;
    accounts: AccountList;
}

export default defineComponent({
  name: 'AccountsWidget',
  components: {
    AccountCreationForm
  },
  watch: {
    budget: async function () {
      this.getAccounts()
    }
  },
  data (): AccountsWidgetData {
    return {
      accountCreationFormIsDisplayed: false,
      accounts: {}
    }
  },
  computed: {
    budget (): Budget {
      return this.$store.state.budget
    }
  },
  methods: {
    async getAccounts () {
      return accountService.getAccounts(this.budget).then(
        (accounts) => {
          this.accounts = accounts
        }
      )
    },
    goToAccountPage (account: Account) {
      router.push({ path: RouterPages.account, query: { accountId: account.id } })
    },
    changeAccountCreationFormDisplay () {
      this.$data.accountCreationFormIsDisplayed = !this.$data.accountCreationFormIsDisplayed
    }
  }
})
</script>
