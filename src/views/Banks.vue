<template>
  <div :class="this.$store.state.css">
    <div class="bankPage menuLayout row col-md-8 offset-md-2 col-xxl-6 offset-xxl-3">
        <div class="header fixed title">
          {{ $t('BANKS') }}
        </div>
        <div class="placeholder top">
          {{ $t('BANKS') }}
        </div>

        <div class="banner">
          <div class="title">{{ $t('SYNCHRONISED_ACCOUNTS') }}</div>
        </div>

        <template v-for="bankAccount in this.bankAccounts" :key="bankAccount">
            <span>{{ bankAccount.name }}</span>
        </template>

        <div class="banner">
          <div class="title">{{ $t('ADD_A_BANK') }}</div>
        </div>
        <p>{{ $t('SELECT_BANK_TO_AUTHORIZE_SYNCHRONISATION') }}</p>
        <template v-for="bank in this.availableBanks" :key="bank">
          <btn class="navigationButton bankButton" v-on:click="getBankAuthorization(bank.id)">
            <img class="illustration col-2" alt="banklogo" :src=bank.logo />
            <span class="illustrationLabel col-10">{{ bank.name }}</span>
          </btn>
        </template>

        <div class="placeholder bottom">
          <NavMenu :page="'profile'" />
        </div>
        <div class="footer fixed">
          <NavMenu :page="'profile'" />
        </div>
      </div>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import StoreHandler from '@/store/StoreHandler'
import NavMenu from '@/components/NavigationMenu.vue'
import BankingService from '@/services/BankingService'
import { Bank, BankAccount, Budget } from '@/model/model'

interface BanksData {
    availableBanks: Bank[];
    bankAccounts: BankAccount[];
}

export default defineComponent({
  name: 'banksPage',
  components: { NavMenu },
  created: async function () {
    StoreHandler.initBudget(this.$store)
    this.getAvailableBanks()
    this.getSynchronisedAccount()
    this.updateIfAgreement()
  },
  props: {
    query: {
      type: String,
      required: false
    }
  },
  data (): BanksData {
    return {
      availableBanks: [],
      bankAccounts: []
    }
  },
  watch: {
    budget: async function () {
      this.getSynchronisedAccount()
    }
  },
  computed: {
    budget (): Budget | null {
      return this.$store.state.budget
    }
  },
  methods: {
    async getAvailableBanks () {
      BankingService.getAvailableBanks().then(
        (bankList) => {
          this.availableBanks = bankList
        }
      )
    },
    async getSynchronisedAccount () {
      if (this.budget) {
        BankingService.getSynchronisedAccount(this.budget).then(
          (bankAccountList) => {
            this.bankAccounts = bankAccountList
          }
        )
      }
    },
    getBankAuthorization (bankId: string) {
      if (this.budget) {
        BankingService.goToBankAgreement(this.budget, bankId)
      }
    },
    updateIfAgreement () {
      if (this.$props.query != null) {
        const agreementId = this.$props.query.split('?')[0]
        BankingService.updateBankAccountList(agreementId)
      }
    }
  }
})
</script>
