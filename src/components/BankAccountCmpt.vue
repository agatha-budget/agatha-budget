<template>
  <div class="bankAccount container bordered" :class="editingClass">
      <img class="illustration logo" alt="banklogo" :src="logo"/>
      <div class="bankAccountTitle">
        <span class="name bold subtitle">{{ bankAccount.name }}</span>
        <span class="balance subtitle">{{ diplayedBalance}}€</span>
        <span class="until subtext">
          {{ $t('AUTHORISED_UNTIL') }}
          <span class="bold">{{syncedUntil}}</span>
        </span>
      </div>
      <button v-if="!edit" v-on:click="updateEdit(false)" class="illustration btn fas fa-pen action hideableActionGuide" :title="$t('EDIT')" />
      <button v-else v-on:click="updateEdit(false)" class="illustration btn fas fa-times-circle action" :title="$t('CLOSE')" />

      <div class="details">
        <div v-if="!edit">
          <span v-if="importDone">{{ $t('IMPORT_DONE') }}</span>
          <button v-if="associatedAccount" class="navigationButton associated" v-on:click="goToAccountPage(associatedAccount)">
            {{ $t('ASSOCIATED_TO') }} <span class="bold">{{associatedAccount.name}}</span>
          </button>
          <button v-else class="actionButton" v-on:click="updateEdit(false)">{{ $t('ASSOCIATE_AGATHA') }}</button>

        </div>
        <div v-else>
          <BankAccountForm :bankAccount="bankAccount" :initiallyAssociatedAccount="initiallyAssociatedAccount" @update="updateEdit"/>
        </div>
      </div>
  </div>
</template>

<script lang="ts">
import router, { RouterPages } from '@/router'
import type { Account, BankAccount } from '@/model/model'
import Time from '@/utils/Time'
import { useBudgetStore } from '@/stores/budgetStore'
import BankAccountForm  from '@/components/forms/BankAccountForm.vue'
import { defineComponent } from 'vue'
import Utils from '@/utils/Utils'

interface BankAccountCmptData {
  edit: boolean;
  importDone: boolean;
}

export default defineComponent({
  name: 'BankAccountCmpt',
  components: { BankAccountForm },
  props: {
    bankAccount: {
      type: Object as () => BankAccount,
      required: true
    },
    logo: {
      type: String,
      required: true
    }
  },
  data (): BankAccountCmptData {
    return {
      edit: false,
      importDone: false
    }
  },
  computed: {
    accounts (): Account[] {
      return useBudgetStore().accounts
    },
    diplayedBalance() : String {
      if (this.bankAccount.balance) {
        return Utils.centsToEurosDisplay(this.bankAccount.balance)
      } else {
        return "Solde inconnu"
      }
    },
    associatedAccount(): Account|null {
      return useBudgetStore().getAccountByBankAccountId(this.bankAccount.id)
    },
    initiallyAssociatedAccount(): Account|undefined {
      return this.associatedAccount ? this.associatedAccount : undefined
    },
    syncedUntil(): String {
      return Time.getDateStringFromTimestamp(Time.after90Days(this.bankAccount.timestamp))
    },
    editingClass(): String {
      return this.edit ? 'editing' : ''
    }
  },
  methods: {
    goToAccountPage (account: Account) {
      router.push({ path: RouterPages.account, query: { accountId: account.id } })
    },
    updateEdit(withImport=false) {
      if (withImport) {
        this.importDone = true
      } else {
        this.importDone = false
      }
      this.edit = !this.edit
    },
  }
})
</script>
