<template>
  <div id="accountWidget">
    <div>
      <h1 class="title">{{$t('MY_ACCOUNTS')}}</h1>
      <span class="subtitle"> {{ $t('TOTAL') }} : {{this.getEurosAmount(this.totalOnAccounts)}} €</span>
    </div>
    <div class="accountContainer col-12 offset-0 col-sm-8 offset-sm-2 col-md-12 offset-md-0">
      <btn v-for="account of this.$store.state.accounts" :key="account" class="navigationButton accounts" v-on:click="goToAccountPage(account)">
        <template v-if="this.fromPage == 'home'">
          <div class="name col-10 offset-2 col-xl-8 offset-xl-0 col-xxl-7 offset-xxl-1">{{ account.name }} :</div>
          <div class="amount col-6 offset-3 col-xl-4 offset-xl-0">{{this.getEurosAmount(account.amount)}}€</div>
        </template>
        <template v-else>
          <div class="name col-7 offset-1">{{ account.name }} :</div>
          <div class="amount col-4 offset-0">{{this.getEurosAmount(account.amount)}}€</div>
        </template>
      </btn>
      <div class="addAccount">
        <template v-if="!accountCreationFormIsDisplayed">
          <btn v-on:click="changeAccountCreationFormDisplay" class="actionButton">{{$t('ADD_ACCOUNT')}}</btn>
        </template>
        <template v-else>
          <AccountCreationForm class="container" @update-account-list="getAccounts" @close-form="changeAccountCreationFormDisplay"/>
        </template>
      </div>
    </div>
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
    getEurosAmount (amount: number): string {
      const value = Utils.getEurosAmount(amount)
      return this.addSpacesInThousand(value)
    },
    addSpacesInThousand (number: number): string {
      return Utils.addSpacesInThousand(number)
    }
  }
})
</script>
