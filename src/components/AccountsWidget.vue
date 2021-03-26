<template>
  <div id="accountWidget">
    <div class="row">
    <h3 class="col-10">{{$t('MY_ACCOUNTS')}}</h3>
    </div>
    <ul>
      <li class="account" v-for="account, accountId in this.accounts" :key="accountId">
        {{ account.name }} - {{account.amount}} €
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
          <AccountCreationForm />
        </div>
      </li>
    </ul>
    </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { AccountList } from '@/model/model'
import { accountService } from '@/services/AccountService'
import AccountCreationForm from '@/components/forms/AccountCreationForm.vue'

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
    currentBudget: async function () {
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
    currentBudget () {
      return this.$store.state.budget
    }
  },
  methods: {
    async getAccounts () {
      return accountService.getAccounts(this.$store.state.budget).then(
        (accounts) => {
          this.accounts = accounts
        }
      )
    },
    changeAccountCreationFormDisplay () {
      this.$data.accountCreationFormIsDisplayed = !this.$data.accountCreationFormIsDisplayed
    }
  }
})
</script>
