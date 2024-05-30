<template>
  <div id="bankAccountForm" class="form">
    <div class="inline_label_input">
      <label class="label" for="selectAccount">{{ $t('ASSOCIATE_TO') }}</label>
      <select id="selectAccount" class="form-select input" v-model="associatedAccountId">
        <option value="none">{{  }}</option>
        <template v-for="account of accounts" :key="account">
          <option :value=account.id>
          {{ account.name }}
          </option>
        </template>
      </select>
    </div>
    <div class="inline_label_input history">
        <label class="label" for="importHistory">{{ $t('IMPORT_HISTORY') }}</label>
        <div id="importHistory" class="input">
          <label class="customSwitch yesNo">
            <input class="switch-input" type="checkbox" v-model="importHistory"/>
            <span class="switch-label" :data-on="$t('YES')" :data-off="$t('NO')"/>
            <span class="switch-handle"/>
          </label>
        </div>
    </div>
    <button class="actionButton" v-on:click="associate">{{ $t('ASSOCIATE') }}</button>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { useBudgetStore } from '@/stores/budgetStore'
import type { Account, BankAccount } from '@/model/model'
import AccountService from '@/services/AccountService'

interface BankAccountFormData {
  associatedAccountId: string;
  importHistory: boolean
}

export default defineComponent({
  name: 'BankAccountForm',
  created: async function () {
  },
  props: {
    bankAccount: {
      type: Object as () => BankAccount,
      required: true
    },
    initiallyAssociatedAccount: {
      type: Object as () => Account,
    },
  },
  data (): BankAccountFormData {
    return {
      associatedAccountId: this.initiallyAssociatedAccount?.id || "none",
      importHistory: false
    }
  },
  computed: {
    accounts (): Account[] {
      return useBudgetStore().accounts
    }
  },
  watch: {
    associatedAccountId: async function () {
      console.log(this.associatedAccountId)
    },
  },
  emits: ['update'],
  methods: {
    associate() {
      // remove association to old account
      if (this.initiallyAssociatedAccount != undefined) {
        console.log("remove old")
        AccountService.updateAccountBankAssociation(this.initiallyAssociatedAccount.id, "none", this.importHistory)
      }
      // set association to new account
      if (this.associatedAccountId != "none") {
        console.log("set new")
        AccountService.updateAccountBankAssociation(this.associatedAccountId, this.bankAccount.id, this.importHistory)
      }
      this.$emit('update', this.importHistory)
    },
  }
})
</script>
