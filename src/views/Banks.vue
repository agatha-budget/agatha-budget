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
          <div class="title">{{ $t('ACCOUNT_ASSOCIATION') }}</div>
        </div>

        <template v-for="account of this.accounts" :key="account">
            {{ account.name }}
            <select class="form-select" v-model="bankAssociation[account.id]">
              <option value=null >{{ $t('NO_ASSOCIATED_BANK_ACCOUNT') }}</option>
              <template v-for="bankAccount in this.bankAccounts" :key="bankAccount">
                <option :value=bankAccount.id>
                {{ bankAccount.name }}
                </option>
              </template>
            </select>
        </template>

        <btn class="actionButton" v-on:click="saveAssociation()">{{ $t('SAVE') }}</btn>

        <div class="banner">
          <div class="title">{{ $t('SYNCHRONISED_BANKS') }}</div>
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
import { Bank, BankAccount, Budget, Account } from '@/model/model'
import AccountService from '@/services/AccountService'

interface BankAssociationList {
  [accountId: string]: string | null;
}

interface BanksData {
  availableBanks: Bank[];
  bankAccounts: BankAccount[];
  bankAssociation: BankAssociationList;
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
      bankAccounts: [],
      bankAssociation: {}
    }
  },
  watch: {
    budget: async function () {
      this.getSynchronisedAccount()
    },
    accounts: async function () {
      this.updateAssociationData()
    }
  },
  computed: {
    budget (): Budget | null {
      return this.$store.state.budget
    },
    accounts (): Account[] | null {
      return this.$store.state.accounts
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
    },
    updateAssociationData () {
      if (this.accounts) {
        for (const i in this.accounts) {
          const account = this.accounts[i]
          this.bankAssociation[account.id] = account.bankAccountId || null
        }
      }
    },
    saveAssociation () {
      if (this.accounts) {
        for (const i in this.accounts) {
          const account = this.accounts[i]
          const bankAccountId = this.bankAssociation[account.id] || undefined
          if (account.bankAccountId !== bankAccountId) {
            AccountService.updateAccount(this.$store, account.id, undefined, bankAccountId)
          }
        }
        StoreHandler.updateAccounts(this.$store)
      }
    }
  }
})
</script>
