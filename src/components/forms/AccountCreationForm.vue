<template>
  <div id="accountCreationForm">
    <div class="accountCreationInput">
      <div class="containerCross col-12">
        <span class="cross fas fa-times-circle" v-on:click="closeForm"/>
      </div>
      <div class="form">
        <input id="newAccountName" class="form-control" v-model="name" :placeholder="$t('ACCOUNT_NAME')">
        <input id="newAccountAmount" class="form-control" v-model="amountString" :placeholder="$t('CURRENT_AMOUNT')">
      </div>
    </div>
    <button class="actionButton" v-on:click="createAccount">{{$t('CREATE_ACCOUNT')}}</button>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import AccountService from '@/services/AccountService'
import Calcul from '@/utils/Calcul'
import { useBudgetStore } from '@/stores/budgetStore'


export default defineComponent({
  name: 'AccountCreationForm',
  data () {
    return {
      name: '',
      amountString: ''
    }
  },
  computed: {
    amount (): number {
      return this.computeStringToCents(this.amountString)
    }
  },
  emits: ['updateAccountList', 'closeForm'],
  methods: {
    createAccount () {
      const budget = useBudgetStore().budget
      if (budget) {
        AccountService.createAccount(budget, this.name, this.amount).then(
          () => {
            this.$emit('updateAccountList')
            this.$emit('closeForm')
          }
        )
      }
    },
    computeStringToCents (amount: string): number {
      return Calcul.computeStringToCents(amount)
    },
    closeForm () {
      this.$emit('closeForm')
    }
  }
})
</script>
