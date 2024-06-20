<template >
    <div v-if="storeLoaded" class="accountPage page" :class="css">
      <div class="fixedHeader">
        <AccountPageHeader :accountId="accountId" :totalAccount="totalAccount" :existingPendingOperation="pendingOperation()" :realAmountOnAccount="realAmount"/>
      </div>
      <div class="pageContent operationTable table-hover">
        <div class="centeredContent">
          <BankSyncCmpt :accountId="accountId"/>
          <div class="dualTab switchOperation">
            <button v-if="manualBloc" v-on:click="switchAddOperation('manual')" class="tabLeft active">{{ $t("ADD_MANUALLY") }}</button>
            <button v-else v-on:click="switchAddOperation('manual')" class="tabLeft">{{ $t("ADD_MANUALLY") }}</button>
            <button v-if="importBloc" v-on:click="switchAddOperation('import')" class="tabRight active">{{ $t("BANK_IMPORT") }}</button>
            <button v-else v-on:click="switchAddOperation('import')" class="tabRight">{{ $t("BANK_IMPORT") }}</button>
          </div>
          <ImportOfx v-if="importBloc" :accountId="accountId" @close-import="closeImport"/>
          <OperationForm v-if="manualBloc" class="operationForm container header" @close-form="closeForm" :accountId="accountId"/>
          <div v-on:click="onClickFilterButton" class="actionLabelIcon">
            <span class="illustration btn fas fa-filter"/>
            <div class="text">{{ $t("FILTER") }}</div>
          </div>
          <FilterCmpt v-if="filterBloc" @close-filter="closeFilter" @filtering-category="filter"/>
          <div class="operationList">
            <template v-for="operation in operations" :key="operation">
              <OperationForm v-if="operation.id === editing" class="operationForm inlineForm container" @close-update="closeUpdate" :accountId="accountId" :operation="operation"/>
              <!-- Operation without daugther -->
              <div v-else-if="operation.daughters.length == 0"  v-on:click="setAsEditing(operation)" class="operationListItem operation">
                <div class="date">{{ $d(getDayAsDate(operation.day), "day") }}</div>
                <button class="illustration btn fas fa-pen action edit" :title="$t('EDIT')" />
                <div class="category" :class="getClassDependingCategory(operation)">
                  {{ getCategoryById(operation.categoryId)?.name ?? $t("UNKNOWN_CATEGORY") }}
                </div>
                <div class="amount" :class="getClassDependingOnAmount(operation.amount)">
                  {{ centsToEurosDisplay(operation.amount) }} €
                </div>
                <span v-if="operation.pending" class="pending illustration btn fas fa-hourglass-half"></span>
                <div class="memo">
                  <div>{{ operation.memo }}</div>
                </div>
              </div>
              <!-- Operation with daugther -->
              <div v-else class="operationListItem hasDaughter operation" v-on:click="setAsEditing(operation)">
                <div class="date">{{ $d(getDayAsDate(operation.day), "day") }}</div>
                <button class="illustration btn fas fa-pen action edit" :title="$t('EDIT')" />
                <div class="memo">{{ operation.memo }}</div>
                <div class="amount" :class="getClassDependingOnAmount(operation.amount)">
                  {{ centsToEurosDisplay(operation.amount) }} €
                </div>
                <span v-if="operation.pending" class="pending illustration btn fas fa-hourglass-half"></span>
                <hr class="separator"/>
                <div class="daughters">
                <template v-for="daughter in operation.daughters" :key="daughter">
                  <div class=" daughter">
                    <div class="daughterCategory category" :class="getClassDependingCategoryDaughter(daughter.categoryId)">
                      {{ getCategoryById(daughter.categoryId)?.name ?? $t("UNKNOWN_CATEGORY") }}
                    </div>
                    <div class="amount" :class="getClassDependingOnAmount(daughter.amount)">
                      {{ centsToEurosDisplay(daughter.amount) }} €
                    </div>
                    <div class="daughterMemo">{{ (daughter.memo === 'null') ? '' : daughter.memo }}</div>
                  </div>
                </template>
                </div>
              </div>
            </template>
          </div>
          <div v-if="operations.length === maxDisplayed">
            <button v-on:click="increaseMaxDisplayed" class="actionButton">{{ $t("DISPLAY_MORE") }}</button>
          </div>
        </div>
      </div>
      <div class="fixedFooter">
        <NavMenu />
      </div>
    </div>
    <div v-else>
      <Loader class="loader"/>
    </div>
</template>

<script lang="ts">
import FilterCmpt from '@/components/FilterCmpt.vue';
import ImportOfx from '@/components/ImportOfx.vue';
import NavMenu from '@/components/NavigationMenu.vue';
import OperationForm from '@/components/forms/OperationForm.vue';
import AccountPageHeader from '@/components/headers/AccountPageHeader.vue';
import BankSyncCmpt from '@/components/BankSyncCmpt.vue';

import Loader from '@/components/utils/Loader.vue';
import type { Account, Category, Operation, OperationWithDaughters } from '@/model/model';
import { useBudgetStore } from '@/stores/budgetStore';
import { useOperationStore } from '@/stores/operationStore';
import { usePersonStore } from '@/stores/personStore';
import Time from '@/utils/Time';
import Utils from '@/utils/Utils';
import { defineComponent } from 'vue';

interface AccountPageData {
    importBloc: boolean;
    manualBloc: boolean;
    filterBloc: boolean;
    filteringCategoryId: string | null,
    existingPendingOperation: boolean,
    maxDisplayed: number,
    editing: string | null,
}

export default defineComponent({
  name: 'AccountView',
  components: {
    OperationForm,
    NavMenu,
    AccountPageHeader,
    BankSyncCmpt,
    ImportOfx,
    FilterCmpt,
    Loader
  },
  created: async function () {
    usePersonStore().init()
  },
  props: {
    accountId: {
      type: String,
      required: true
    }
  },
  data (): AccountPageData {
    return {
      importBloc: false,
      manualBloc: false,
      filterBloc: false,
      filteringCategoryId: null,
      existingPendingOperation: false,
      maxDisplayed: 100,
      editing: null,
    }
  },
  computed: {
    account (): Account | null {
      return useBudgetStore().getAccountById(this.accountId)
    },
    totalAccount (): number {
      return (this.account == null) ? 0 : this.account.amount
    },
    realAmount (): number {
      let value: number = this.account == null ? 0 : this.account.amount
      this.operations.forEach((operation) => {
        if (operation.pending === true) {
          value -= operation.amount
        }
      })
      return value
    },
    css (): string {
      return usePersonStore().css
    },
    operations(): OperationWithDaughters[] {
      const storeOperations = (this.filteringCategoryId === null)
        ? useOperationStore().getOperationByAccount(this.accountId)
        : useOperationStore().getOperationByAccountAndCategory(this.accountId, this.filteringCategoryId)
      const toDisplayOperations = storeOperations.slice(0, this.maxDisplayed)
      return toDisplayOperations
    },
    storeLoaded (): boolean {
      return useOperationStore().storeLoaded
    }
  },
  methods: {
    getDayAsDate (dayAsInt: number): Date {
      return Time.getDateFromDay(dayAsInt)
    },
    setAsEditing (operation: OperationWithDaughters) {
      this.editing = operation.id
    },
    getCategoryById (categoryId: string): Category | null {
      return useBudgetStore().getCategoryById(categoryId)
    },
    getClassDependingOnAmount (amount: number): string {
      if (amount > 0) {
        return 'positive'
      } else {
        return ''
      }
    },
    getClassDependingCategory (operation: Operation | OperationWithDaughters): string {
      return (operation.categoryId === null) ? 'negative' : ''
    },
    getClassDependingCategoryDaughter (categoryId: string): string {
      return categoryId === null ? 'negative' : ''
    },
    filter (categoryId: string) {
      this.filteringCategoryId = categoryId
    },
    switchAddOperation (type: string) {
      if (type === 'import') {
        this.importBloc = !this.importBloc
        if (this.importBloc === true && this.manualBloc === true) {
          this.manualBloc = false
        }
      } else if (type === 'manual') {
        this.manualBloc = !this.manualBloc
        if (this.importBloc === true && this.manualBloc === true) {
          this.importBloc = false
        }
      }
      this.filterBloc = false
    },
    pendingOperation (): boolean {
      let operationPending = false
      this.operations.forEach((operation) => {
        if (operation.pending === true) {
          operationPending = true
        }
      })
      return operationPending
    },
    closeImport () {
      this.importBloc = false
    },
    closeForm () {
      this.manualBloc = false
    },
    closeFilter () {
      this.filterBloc = false
      this.filteringCategoryId = null
    },
    closeUpdate () {
      this.editing = null
    },
    onClickFilterButton () {
      this.filterBloc = !this.filterBloc
      this.manualBloc = false
      this.importBloc = false
      if (!this.filterBloc) {
        this.filteringCategoryId = null
      }
    },
    centsToEurosDisplay (amount: number): string {
      return Utils.centsToEurosDisplay(amount)
    },
    increaseMaxDisplayed() {
      this.maxDisplayed += 200
    }
  }
})
</script>
