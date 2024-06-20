<template>
  <div :class="css">
    <div class="bankPage page ">
      <div class="fixedHeader">
        <h1 class="title">{{ $t('BANKS') }}</h1>
      </div>
      <div class="pageContent">
        <div v-if="bankLoaded" class="centeredContent">
          <BankForm v-if="displayBankForm" :availableBanks="availableBanks" @close-form="updateOpenBankForm"/>
          
          <template v-if="!displayBankForm">
            <button id="addBankBtn" v-on:click="updateOpenBankForm" class="actionButton">{{$t('ADD_BANK_ACCOUNT')}}</button>
            <template v-for="bankAccount of bankAccounts" :key="bankAccount">
              <BankAccountCmpt :bankAccount="bankAccount" :logo="getLogo(bankAccount.bankId)"/>
            </template>
          </template>
        </div>
        <div v-else>
          <Loader class="loader" :message="$t('CONTACTING_BANKS')"/>
        </div>
      </div>
      <div class="fixedFooter">
        <NavMenu />
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import NavMenu from '@/components/NavigationMenu.vue'
import type { Account, Bank, BankAccount, Budget} from '@/model/model'
import router, { RouterPages } from '@/router'
import AccountService from '@/services/AccountService'
import BankForm from '@/components/forms/BankForm.vue'
import BankAccountCmpt from '@/components/BankAccountCmpt.vue'
import BankingService from '@/services/BankingService'
import { useBudgetStore } from '@/stores/budgetStore'
import { usePersonStore } from '@/stores/personStore'
import Time from '@/utils/Time'
import { defineComponent } from 'vue'
import Loader from '@/components/utils/Loader.vue';


interface BankAccountByTimestampList {
  [timestamp: number]: BankAccount[];
}

interface BankAuthorizationList {
  [bankId: string]: BankAccountByTimestampList;
}

interface BankAssociationData {
  bankAccountId: string;
  importHistory: boolean;
}

interface BankAssociationList {
  [accountId: string]: BankAssociationData;
}

interface BanksData {
  availableBanks: Bank[];
  bankAccounts: BankAccount[];
  bankAssociation: BankAssociationList;
  selectedBankId: string|null;
  openBankForm: Boolean;
  bankLoaded: Boolean;
}

export default defineComponent({
  name: 'BanksView',
  components: { NavMenu, BankForm, BankAccountCmpt, Loader },
  created: async function () {
    useBudgetStore().init()
    this.getAuthorizedAccounts()
    this.updateIfAgreement()
    this.updateAssociationData()
    this.getAvailableBanks()
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
      bankAssociation: {},
      selectedBankId: null,
      openBankForm: false,
      bankLoaded: false,
    }
  },
  watch: {
    budget: async function () {
      this.getAuthorizedAccounts()
    },
    accounts: async function () {
      this.updateAssociationData()
    }
  },
  computed: {
    budget (): Budget | null {
      return useBudgetStore().budget
    },
    accounts (): Account[] | null {
      return useBudgetStore().accounts
    },
    authorizedBanks (): BankAuthorizationList | null {
      return this.groupAccountByBankAndTimestamp(this.bankAccounts)
    },
    css (): string {
      return usePersonStore().css
    },
    displayBankForm() {
      return this.openBankForm || this.bankAccounts.length < 1 
    }
  },
  methods: {
    getAuthorizedAccounts () {
      if (this.budget) {
        BankingService.getAuthorizedAccounts(this.budget).then(
          (bankAccountList) => {
            this.bankAccounts = bankAccountList
            this.bankLoaded = true
          }
        )
      }
    },
    updateOpenBankForm() {
      this.openBankForm = !this.openBankForm
    },
    updateIfAgreement () {
      if (this.$props.query != null) {
        const agreementId = this.$props.query.split('?')[0]
        BankingService.updateBankAccountList(agreementId)
        router.push(RouterPages.banks)
      }
    },
    updateAssociationData () {
      if (this.accounts) {
        for (const i in this.accounts) {
          const account = this.accounts[i]
          this.bankAssociation[account.id] = {
            bankAccountId: account.bankAccountId || 'none',
            importHistory: false
          }
        }
      }
    },
    displayImportHistoryOption (account: Account): boolean {
      return (this.bankAssociation[account.id].bankAccountId !== account.bankAccountId && this.bankAssociation[account.id].bankAccountId !== 'none')
    },
    saveAssociation () {
      if (this.accounts) {
        for (const i in this.accounts) {
          const account = this.accounts[i]
          const bankAccountId = this.bankAssociation[account.id].bankAccountId
          const importHistory = this.bankAssociation[account.id].importHistory
          if (account.bankAccountId !== bankAccountId) {
            AccountService.updateAccountBankAssociation(account.id, bankAccountId, importHistory)
          }
        }
        useBudgetStore().updateAccounts()
      }
    },
    groupAccountByBankAndTimestamp (bankAccounts: BankAccount[]): BankAuthorizationList {
      const banks: BankAuthorizationList = {}
      bankAccounts.forEach(function (bankAccount) {
        if (bankAccount.bankId in banks) {
          const bankIdList = banks[bankAccount.bankId]
          if (bankAccount.timestamp in bankIdList) {
            bankIdList[bankAccount.timestamp].push(bankAccount)
          } else {
            bankIdList[bankAccount.timestamp] = [bankAccount]
          }
        } else {
          const timeStampList: BankAccountByTimestampList = {}
          timeStampList[bankAccount.timestamp] = [bankAccount]
          banks[bankAccount.bankId] = timeStampList
        }
      })
      return banks
    },
    getAvailableBanks () {
      BankingService.getAvailableBanks().then(
        (bankList) => {
          this.availableBanks = bankList
        }
      )
    },
    getLogo (bankId: string): string {
      for (const bank of this.availableBanks) {
        if (bank.id === bankId) {
          return bank.logo
        }
      }
      return 'not found'
    },
    getExpirationDateFromTimestamp (timestamp: number): string {
      const expirationTimestamp = Time.after90Days(timestamp)
      return Time.getDateStringFromTimestamp(expirationTimestamp)
    }
  }
})
</script>
