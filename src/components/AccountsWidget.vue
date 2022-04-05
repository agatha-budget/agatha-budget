<template>
  <div id="accountWidget">
    <div class="row title">
      <h1>{{$t('MY_ACCOUNTS')}}</h1>
    </div>
    <span class="total"> total : {{this.getEurosAmount(this.totalOnAccounts)}} €</span>
    <input type="text" v-model="testString" placeholder="je ne sers à rien">
    <div>testString : {{ testString }}</div>
    <div>testParse : {{ this.parseComma(testString) }}</div>
    <div>testNombere : {{ this.testNombre }}</div>
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
    testString: string;
}

export default defineComponent({
  name: 'AccountsWidget',
  components: {
    AccountCreationForm
  },
  data (): AccountsWidgetData {
    return {
      accountCreationFormIsDisplayed: false,
      testString: 'vide'
    }
  },
  computed: {
    totalOnAccounts (): number {
      let total = 0
      for (const account of this.$store.state.accounts) {
        total += account.amount
      }
      return total
    },
    testNombre (): number {
      return this.parseCommanb(this.testString)
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
      return number.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ' ')
    },
    parseComma (amount: string): string {
      if (amount.indexOf(',') !== -1) {
        console.log('on passe dans la boucle' + amount.indexOf(','))
        const parsed = amount.replace(/,/g, '.')
        console.log(amount)
        return parsed
      }
      return amount
    },
    parseCommanb (amount: string): number {
      if (amount.indexOf(',') !== -1) {
        const parsed = amount.replace(/,/g, '.')
        return Number(parsed)
      }
      return Number(amount)
    }
  }
})
</script>
