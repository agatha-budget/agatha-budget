<template>
  <div id="accountCreationForm">
    <input id="newAccountName" class="form-control" v-model="name" :placeholder="$t('ACCOUNT_NAME')">
    <label for="newAccountAmount">{{ $t('INITIAL_AMOUNT') }}</label>
    <input id="newAccountAmount" class="form-control" v-model.number="amount">
    <select class="form-control" v-model="currency">
      <option v-for="currency in availableCurrencies" :key="currency" :value=currency>
        {{ currency.name }} ({{ currency.symbol}})
      </option>
    </select>
    <button class="btn btn-outline-info" v-on:click="login">{{$t('CREATE_ACCOUNT')}}</button>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { currencies } from '@/model/enum'
import { Currency, Account } from '@/model/model'

export default defineComponent({
  name: 'AccountCreationForm',
  data () {
    return {
      name: '',
      amount: 0,
      currency: currencies.EUROS
    }
  },
  computed: {
    availableCurrencies (): Currency[] {
      const list: Currency[] = []
      for (const key in currencies) {
        list.push(currencies[key])
      }
      return list
    }
  },
  methods: {
    createAccount (): Account {
      return {
        id: 'eez',
        name: this.$data.name,
        amount: this.$data.amount,
        currency: this.$data.currency
      }
    }
  }
})
</script>
