<template>
  <div id="bankForm" class="container form header">
    <div class="containerCross col-12">
      <span class="cross fas fa-times-circle" v-on:click="closeForm()"/>
    </div>
    <div class="subtitle">{{ $t('ADD_BANK_AUTHORIZATION') }}</div>
    <p>{{ $t('WHY_ADD_AUTHORIZATION_TEXT_P1') }}</p>
    <p class="bold">{{ $t('WHY_ADD_AUTHORIZATION_TEXT_P2') }}</p>
    <p>{{ $t('WHY_ADD_AUTHORIZATION_TEXT_P3') }}</p>

    <Multiselect
      v-model="selectedBankId"
      :groups="false"
      :searchable="true"
      :strict="false"
      :options="bankOptions"
      :noResultsText="$t('NO_RESULT_FOUND')"
      :placeholder="$t('SELECT_BANK')"
    />
    <button class="actionButton" v-on:click="getBankAuthorization">{{ $t('ASK_AUTHORIZATION') }}</button>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import BankingService from '@/services/BankingService'
import Multiselect from '@vueform/multiselect'
import { useBudgetStore } from '@/stores/budgetStore'
import type { Budget, SelectOption, Bank } from '@/model/model'

interface BankFormData {
  selectedBankId: string|null;
}

export default defineComponent({
  name: 'BankForm',
  components: { Multiselect },
  props: {
    availableBanks: {
      type: Object as () => Bank[],
      required: true
    }
  },
  data (): BankFormData {
    return {
      selectedBankId: null
    }
  },
  computed: {
    budget (): Budget | null {
      return useBudgetStore().budget
    },
    bankOptions (): SelectOption[] {
      const optionsList = []
      for (const bank of this.availableBanks) {
        const option = { value: bank.id, label: bank.name }
        optionsList.push(option)
      }
      return optionsList
    },
  },
  emits: ['closeForm'],
  methods: {
    getBankAuthorization () {
      if (this.budget && this.selectedBankId) {
        BankingService.goToBankAgreement(this.budget, this.selectedBankId)
      }
    },
    closeForm () {
      this.$emit('closeForm')
    },
  }
})
</script>
