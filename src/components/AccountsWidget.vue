<template>
  <div id="accountWidget" class="container">
    <div>
      <span class="subtitle"> {{ $t('SEE_MY_ACCOUNTS') }} </span>
    </div>
    <div class="accountList col-12 offset-0 col-sm-8 offset-sm-2 col-md-12 offset-md-0">
      <button v-for="account of accounts" :key="account" class="navigationButton accounts" v-on:click="goToAccountPage(account)">
        <template v-if="fromPage == 'home'">
          <div class="name col-10 offset-2 col-xl-8 offset-xl-0 col-xxl-7 offset-xxl-1">{{ account.name }} :</div>
          <div class="amount col-6 offset-3 col-xl-4 offset-xl-0">{{centsToEurosDisplay(account.amount)}}€</div>
        </template>
        <template v-else>
          <div class="name col-7 offset-1">{{ account.name }} :</div>
          <div class="amount col-4 offset-0">{{centsToEurosDisplay(account.amount)}}€</div>
        </template>
      </button>
    </div>
    <div>
      <span class="subtitle"> {{ $t('TOTAL') }} : {{centsToEurosDisplay(totalOnAccounts)}} €</span>
    </div>
    <div class="addAccount">
      <template v-if="!accountCreationFormIsDisplayed">
        <button v-on:click="changeAccountCreationFormDisplay" class="actionButton">{{$t('ADD_ACCOUNT')}}</button>
      </template>
      <template v-else>
        <AccountCreationForm class="container inline" @update-account-list="getAccounts" @close-form="changeAccountCreationFormDisplay"/>
      </template>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import AccountCreationForm from '@/components/forms/AccountCreationForm.vue'
import router, { RouterPages } from '@/router'
import type { Account } from '@/model/model'
import Utils from '@/utils/Utils'
import { useBudgetStore } from '@/stores/budgetStore'


interface AccountsWidgetData {
    accountCreationFormIsDisplayed: boolean;
    fromPage: string;
}

export default defineComponent({
  name: 'AccountsWidget',
  components: {
    AccountCreationForm
  },
  props: {
    page: {
      type: String,
      required: true
    }
  },
  data (): AccountsWidgetData {
    return {
      accountCreationFormIsDisplayed: false,
      fromPage: this.$props.page
    }
  },
  computed: {
    totalOnAccounts (): number {
      let total = 0
      for (const account of useBudgetStore().accounts) {
        total += account.amount
      }
      return total
    },
    accounts (): Account[] {
      return useBudgetStore().accounts
    }
  },
  methods: {
    goToAccountPage (account: Account) {
      router.push({ path: RouterPages.account, query: { accountId: account.id } })
    },
    changeAccountCreationFormDisplay () {
      this.$data.accountCreationFormIsDisplayed = !this.$data.accountCreationFormIsDisplayed
    },
    centsToEurosDisplay (amount: number): string {
      return Utils.centsToEurosDisplay(amount)
    }
  }
})
</script>
