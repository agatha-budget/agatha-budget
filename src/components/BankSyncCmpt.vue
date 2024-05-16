<template>
  <div id="BankSyncCmpt">
    <button v-if="!account?.syncedUntil" v-on:click="goToBanksPage" className="navigationButton">{{ $t("NOT_SYNCED_GO_TO_BANKS") }}</button>
    <div v-if="isSynced()">
      {{ $t("SYNCED_UNTIL") +" "+ syncedUntil}}
    </div>
    <button v-if="isRecentlyUnsynced()" v-on:click="goToBanksPage" className="navigationButton">{{ $t("UNSYNCED_GO_TO_BANKS") }}</button>
  </div>
</template>

<script lang="ts">
import type { Account } from '@/model/model'
import router, { RouterPages } from '@/router'
import { useBudgetStore } from '@/stores/budgetStore'
import Time from '@/utils/Time'
import { defineComponent } from 'vue'


interface BankSyncCmpt {
    accountCreationFormIsDisplayed: boolean;
    fromPage: string;
}

export default defineComponent({
  name: 'BankSyncCmpt',
  components: {
    
  },
  props: {
    accountId: {
      type: String,
      required: true
    }
  },
  computed: {
    account (): Account | null {
      return useBudgetStore().getAccountById(this.accountId)
    },
    syncedUntil(): String {
      return (this.account?.syncedUntil) ? Time.getDateStringFromTimestamp(this.account.syncedUntil) : ""
    }
  },
  methods: {
    goToBanksPage () {
      router.push(RouterPages.banks)
    },
    isSynced(): Boolean {
      return (this.account?.syncedUntil != null) && (this.account?.syncedUntil > Time.now())
    },
    isRecentlyUnsynced(): Boolean {
      return (this.account?.syncedUntil != null) && !this.isSynced() && (this.account?.syncedUntil > Time.get30DaysAgo())
    }
  }
})
</script>
