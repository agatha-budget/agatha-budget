<template>
  <div id="accountWidget">
    <div class="row">
    <h3 class="col-10">{{$t('MY_ACCOUNTS')}}</h3>
    </div>
    <ul>
      <li class="account" v-for="account, accountId in this.accountsList" :key="accountId">
        {{ account.name }} - {{account.amount}} {{account.currency.symbol}}
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
import { Account } from '@/model/model'
import { currencies } from '@/model/enum'
import AccountCreationForm from '@/components/forms/AccountCreationForm.vue'

interface AccountsWidgetData {
    accountCreationFormIsDisplayed: boolean;
    accountsList: {
        [accountId: string]: Account;
    };
}

export default defineComponent({
  name: 'AccountsWidget',
  components: {
    AccountCreationForm
  },
  created: async function () {
    this.getAccounts()
  },
  data (): AccountsWidgetData {
    return {
      accountCreationFormIsDisplayed: false,
      accountsList: {
        idaccount1: {
          id: 'id',
          name: 'courrant',
          amount: 125.25,
          currency: currencies.EUROS
        },
        idaccount2: {
          id: 'id2',
          name: 'epargne',
          amount: 1250,
          currency: currencies.EUROS
        }
      }
    }
  },
  methods: {
    async getAccounts () {
      console.log('get accounts')
    },
    changeAccountCreationFormDisplay () {
      this.$data.accountCreationFormIsDisplayed = !this.$data.accountCreationFormIsDisplayed
    }
  }
})
</script>
