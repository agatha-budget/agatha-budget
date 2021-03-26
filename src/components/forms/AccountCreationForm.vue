<template>
  <div id="accountCreationForm">
    <input id="newAccountName" class="form-control" v-model="name" :placeholder="$t('ACCOUNT_NAME')">
    <label for="newAccountAmount">{{ $t('INITIAL_AMOUNT') }}</label>
    <input id="newAccountAmount" class="form-control" v-model.number="amount">
    <button class="btn btn-outline-info" v-on:click="login">{{$t('CREATE_ACCOUNT')}}</button>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { Account } from '@/model/model'

export default defineComponent({
  name: 'AccountCreationForm',
  data () {
    return {
      name: '',
      amount: 0
    }
  },
  methods: {
    createAccount (): Account {
      return {
        id: 'eez',
        name: this.$data.name,
        amount: this.$data.amount
      }
    }
  }
})
</script>
