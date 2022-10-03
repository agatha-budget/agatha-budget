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
import Utils from '@/utils/Utils'
import Calcul from '@/utils/Calcul'

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
      return this.entireCalcul(this.amountString)
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
    entireCalcul (amount: string): number {
      return Calcul.entireCalcul(amount)
    },
    closeForm () {
      this.$emit('closeForm')
    }
  }
})
</script>
