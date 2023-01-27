<template >
  <div :class="this.$store.state.css">
    <div class="accountPage row">
      <div class="header fixed">
          <AccountPageHeader :accountId="account.id" :totalAccount="this.totalAccount" :existingPendingOperation="pendingOperation()" :realAmountOnAccount="this.realAmount"/>
      </div>
      <div class="placeholderTop">
        <AccountPageHeader :accountId="account.id" :totalAccount="this.totalAccount" :existingPendingOperation="pendingOperation()" :realAmountOnAccount="this.realAmount"/>
      </div>
      <div class="content container operationTable table-hover">
        <div class="tripleTab switchOperation">
          <button v-if="manualBloc" v-on:click="switchAddOperation('manual')" class="tabLeft active">{{ $t("ADD_MANUALLY") }}</button>
          <button v-else v-on:click="switchAddOperation('manual')" class="tabLeft">{{ $t("ADD_MANUALLY") }}</button>
          <button v-on:click="goToBanksPage" class="tabCenter">{{ $t("SYNC_BANK") }}</button>
          <button v-if="importBloc" v-on:click="switchAddOperation('import')" class="tabRight active">{{ $t("BANK_IMPORT") }}</button>
          <button v-else v-on:click="switchAddOperation('import')" class="tabRight">{{ $t("BANK_IMPORT") }}</button>
        </div>
        <ImportOfx v-if="importBloc" :accountId="this.accountId" @close-import="closeImport"/>
        <OperationForm v-if="manualBloc" class="operationForm container header" @update-operation-list="getAccountOperation" @close-form="closeForm" :accountId="this.accountId"/>
        <div v-on:click="onClickFilterButton" class="actionLabelIcon">
          <span class="illustration btn fas fa-filter"/>
          <div class="text">{{ $t("FILTER") }}</div>
        </div>
        <FilterCmpt v-if="filterBloc" @close-filter="closeFilter" @filtering-category="filter"/>
        <div class="operationList">
          <template v-for="operation in this.operations" :key="operation">
            <OperationForm v-if="operation.editing" class="operationForm inlineOperationForm container inline"  @update-operation-list="getAccountOperation" @close-update="closeUpdate" :accountId="this.accountId" :operation="operation"/>
            <!-- Operation without daugther -->
            <div v-else-if="operation.daughters.length == 0"  v-on:click="setAsEditing(operation)" class="operationListItem operation row">
                <div class="row">
                  <div class="date col-6">{{ $d(this.getDayAsDate(operation.day), "day") }}</div>
                  <div class="col-1 offset-5"><button class="illustration btn fas fa-pen action" :title="$t('EDIT')" /></div>
                </div>
                <div class="row">
                  <div class="category col-8" :class="getClassDependingCategory(operation)">
                    {{ this.getCategoryById(operation.categoryId)?.name ?? $t("UNKNOWN_CATEGORY") }}
                  </div>
                  <div class="amount col-3" :class="this.getClassDependingOnAmount(operation)">
                    {{ eurosToEurosDisplay(this.getEurosAmount(operation.amount)) }} €
                  </div>
                  <div class="col-1">
                    <span v-if="operation.pending" class="pending illustration btn fas fa-hourglass-half"></span>
                  </div>
                </div>
                <div class="memo row">
                  <div>{{ operation.memo }}</div>
                </div>
            </div>
            <!-- Operation with daugther -->
            <div v-else class="operationListItem operation row" v-on:click="setAsEditing(operation)">
              <div class="row">
                <div class="date col-6">{{ $d(this.getDayAsDate(operation.day), "day") }}</div>
                <div class="col-1 offset-5"><button class="illustration btn fas fa-pen action" :title="$t('EDIT')" /></div>
              </div>
              <div class="row beforeDaughter">
                <div class="memo col-8">
                  {{ operation.memo }}
                </div>
                <div class="amount col-3" :class="this.getClassDependingOnAmount(operation)">
                  {{ eurosToEurosDisplay(this.getEurosAmount(operation.amount)) }} €
                </div>
                <div class="col-1">
                  <span v-if="operation.pending" class="pending illustration btn fas fa-hourglass-half"></span>
                </div>
              </div>
              <hr>
              <template v-for="daughter in operation.daughters" :key="daughter">
                <div class="row daughter">
                  <div class="daughterCategory category col-8" :class="getClassDependingCategoryDaughter(daughter.categoryId)">
                    {{ this.getCategoryById(daughter.categoryId)?.name ?? $t("UNKNOWN_CATEGORY") }}
                  </div>
                  <div class="amount col-3" :class="this.getClassDependingOnAmount(operation)">
                    {{ eurosToEurosDisplay(this.getEurosAmount(daughter.amount)) }} €
                  </div>
                </div>
                <div class="daughterMemo">{{ (daughter.memo === 'null') ? '' : daughter.memo }}</div>
              </template>
            </div>
          </template>
        </div>
      </div>
      <div class="placeholder bottom">
        <NavMenu/>
      </div>
      <div class="footer fixed">
        <NavMenu/>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import router, { redirectToLoginPageIfNotLogged, RouterPages } from '@/router'
import { Account, Category, Operation, OperationWithDaughters } from '@/model/model'
import Time from '@/utils/Time'
import StoreHandler from '@/store/StoreHandler'
import OperationService from '@/services/OperationService'
import OperationForm from '@/components/forms/OperationForm.vue'
import Utils from '@/utils/Utils'
import NavMenu from '@/components/NavigationMenu.vue'
import AccountPageHeader from '@/components/headers/AccountPageHeader.vue'
import ImportOfx from '@/components/ImportOfx.vue'
import FilterCmpt from '@/components/FilterCmpt.vue'

interface AccountPageData {
    operations: EditableOperation[];
    importBloc: boolean;
    manualBloc: boolean;
    filterBloc: boolean;
    filteringCategoryId: string | null;
    existingPendingOperation: boolean;
}

interface EditableOperation extends OperationWithDaughters {
  editing: boolean;
}

export default defineComponent({
  name: 'AccountPage',
  components: {
    OperationForm,
    NavMenu,
    AccountPageHeader,
    ImportOfx,
    FilterCmpt
  },
  beforeCreate: async function () {
    redirectToLoginPageIfNotLogged(this.$store)
  },
  created: async function () {
    StoreHandler.initStore(this.$store)
    this.getAccountOperation()
  },
  watch: {
    account: async function () {
      await this.getAccountOperation()
    }
  },
  props: {
    accountId: {
      type: String,
      required: true
    }
  },
  data (): AccountPageData {
    return {
      operations: [],
      importBloc: false,
      manualBloc: false,
      filterBloc: false,
      filteringCategoryId: null,
      existingPendingOperation: false
    }
  },
  computed: {
    account (): Account | null {
      for (const account of this.$store.state.accounts) {
        if (account.id === this.accountId) {
          return account
        }
      }
      return null
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
    }
  },
  methods: {
    async getAccountOperation () {
      if (this.account) {
        return OperationService.getOperations(this.account, this.filteringCategoryId).then(
          (operations) => {
            this.operations = this.operationToEditableOperation(operations)
          }
        )
      }
    },
    async getAccountOperationFilter () {
      if (this.account) {
        const filteredOperations = await OperationService.getOperations(this.account, this.filteringCategoryId)
        this.operations = this.operationToEditableOperation(filteredOperations)
      }
    },
    getDayAsDate (dayAsInt: number): Date {
      return Time.getDateFromDay(dayAsInt)
    },
    setAsEditing (operation: EditableOperation) {
      operation.editing = true
    },
    operationToEditableOperation (operations: OperationWithDaughters[]): EditableOperation[] {
      const editableOperations: EditableOperation[] = []
      operations.forEach((operation) =>
        editableOperations.push({
          id: operation.id,
          day: operation.day,
          accountId: operation.accountId,
          categoryId: operation.categoryId,
          amount: operation.amount,
          memo: (operation.memo === 'null') ? '' : operation.memo,
          pending: operation.pending,
          daughters: operation.daughters,
          editing: false
        })
      )
      return editableOperations
    },
    getCategoryById (categoryId: string): Category | null {
      return StoreHandler.getCategoryById(this.$store, categoryId)
    },
    getEurosAmount (amount: number): number {
      return Utils.getEurosAmount(amount)
    },
    getClassDependingOnAmount (operation: Operation): string {
      if (operation.amount > 0) {
        return 'positive'
      } else {
        return ''
      }
    },
    eurosToEurosDisplay (number: number): string {
      return Utils.eurosToEurosDisplay(number)
    },
    getClassDependingCategory (operation: Operation): string {
      return (operation.categoryId === null) ? 'negative' : ''
    },
    getClassDependingCategoryDaughter (categoryId: string): string {
      return categoryId === null ? 'negative' : ''
    },
    async filter (categoryId: string) {
      this.filteringCategoryId = categoryId
      await this.getAccountOperationFilter()
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
    async debited (operation: OperationWithDaughters) {
      if (operation && this.account) {
        const daughters = operation.daughters
        if (daughters && daughters.length !== 0) {
          daughters.forEach(daughter => {
            OperationService.updateOperation(this.$store, daughter.id, this.accountId, undefined, undefined, undefined, undefined, false)
          })
        }
        OperationService.updateOperation(this.$store, operation.id, this.accountId, undefined, undefined, undefined, undefined, false)
      }
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
    async closeFilter () {
      this.filterBloc = false
      this.filteringCategoryId = null
      await this.getAccountOperation()
    },
    closeUpdate (operation: EditableOperation) {
      operation.editing = false
    },
    async onClickFilterButton () {
      this.filterBloc = !this.filterBloc
      this.manualBloc = false
      this.importBloc = false
      if (!this.filterBloc) {
        this.filteringCategoryId = null
        await this.getAccountOperation()
      }
    },
    goToBanksPage () {
      router.push(RouterPages.banks)
    }
  }
})
</script>
