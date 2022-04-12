<template>
  <div id="accountCreationForm">
    <div class="accountCreationInput">
      <label for="newAccountName">{{ $t('ACCOUNT_NAME') }}</label>
      <input id="newAccountName" class="form-control" v-model="name" :placeholder="$t('CHECKING_ACCOUNT')">
      <label for="newAccountAmount">{{ $t('INITIAL_AMOUNT') }}</label>
      <input id="newAccountAmount" class="form-control" v-model="amountString">
    </div>
    <button class="btn" v-on:click="createAccount">{{$t('CREATE_ACCOUNT')}}</button>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import AccountService from '@/services/AccountService'
import Utils from '@/utils/Utils'

export default defineComponent({
  name: 'AccountCreationForm',
  data () {
    return {
      name: '',
      amountString: '0'
    }
  },
  computed: {
    amount (): number {
      return this.parenthesis(this.amountString)
    }
  },
  emits: ['updateAccountList', 'closeForm'],
  methods: {
    createAccount () {
      if (this.$store.state.budget) {
        AccountService.createAccount(this.$store, this.$store.state.budget, this.name, Utils.getCentsAmount(this.amount)).then(
          () => {
            this.$emit('updateAccountList')
            this.$emit('closeForm')
          }
        )
      }
    },
    parenthesis (amount: string): number {
      return Utils.parenthesis(amount)
    }
  }
})
</script>
