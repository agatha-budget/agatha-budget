<template>
  <div id="accountWidget">
    <div class="row">
    <h3 class="col-10">{{$t('MY_ACCOUNTS')}}</h3>
    </div>
    <ul>
      <li class="account" v-for="account, accountId in this.$store.state.accounts" :key="accountId">
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
import { Budget } from '@/model/model'
import AccountCreationForm from '@/components/forms/AccountCreationForm.vue'
import router, { RouterPages } from '@/router'

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
    budget (): Budget {
      return this.$store.state.budget
    }
  },
  methods: {
    goToAccountPage (account: Account) {
      router.push({ path: RouterPages.account, query: { accountId: account.id } })
    },
    changeAccountCreationFormDisplay () {
      this.$data.accountCreationFormIsDisplayed = !this.$data.accountCreationFormIsDisplayed
    }
  }
})
</script>
