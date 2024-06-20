<template>
  <div id="accountWidget" class="container">
    <div>
      <span class="subtitle"> {{ $t('SEE_MY_ACCOUNTS') }} </span>
    </div>
    <div>
      <button v-for="account of accounts" class="navigationButton accounts" v-on:click="goToAccountPage(account)">
        <div class="name">{{ account.name }}</div>
        <div class="amount">{{centsToEurosDisplay(account.amount)}}€</div>
        <div class="sync">
          <span v-if="isSynced(account)" class="fas fa-link"/>
          <span v-if="isRecentlyUnsynced(account)" class="fas fa-unlink"/>
        </div>
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
import AccountCreationForm from '@/components/forms/AccountCreationForm.vue'
import type { Account } from '@/model/model'
import router, { RouterPages } from '@/router'
import { useBudgetStore } from '@/stores/budgetStore'
import Time from '@/utils/Time'
import Utils from '@/utils/Utils'
import { defineComponent } from 'vue'


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
    },
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
    },
    getAccounts(){
      useBudgetStore().updateAccounts()
    },
    isSynced(account: Account): Boolean {
      return (account.syncedUntil != null) && (account.syncedUntil > Time.now())
    },
    isRecentlyUnsynced(account: Account): Boolean {
      return (account.syncedUntil != null) && !this.isSynced(account) && (account.syncedUntil > Time.get30DaysAgo())
    }
  }
})
</script>
