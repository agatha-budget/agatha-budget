<template >
  <div :class="this.$store.state.css">
    <div class="accountPage row col-lg-10 offset-lg-1 col-xl-8 offset-xl-2">
      <div class="header fixed">
          <AccountPageHeader :accountId="account.id" :totalAccount="this.totalAccount" :existingPendingOperation="pendingOperation()" :realAmountOnAccount="this.realAmount"/>
      </div>
      <div class="placeholderTop">
        <AccountPageHeader :accountId="account.id" :totalAccount="this.totalAccount" :existingPendingOperation="pendingOperation()" :realAmountOnAccount="this.realAmount"/>
      </div>
      <div class="content container operationTable table-hover">
        <div class="dualTab switchOperation">
          <btn v-if="manualBloc" v-on:click="switchAddOperation('manual')" class="tabLeft active">{{ $t("ADD_MANUALLY") }}</btn>
          <btn v-else v-on:click="switchAddOperation('manual')" class="tabLeft">{{ $t("ADD_MANUALLY") }}</btn>
          <btn v-if="importBloc" v-on:click="switchAddOperation('import')" class="tabRight active">{{ $t("BANK_IMPORT") }}</btn>
          <btn v-else v-on:click="switchAddOperation('import')" class="tabRight">{{ $t("BANK_IMPORT") }}</btn>
        </div>
        <ImportOfx v-if="importBloc" :accountId="this.accountId" @close-import="closeImport"/>
        <OperationForm v-if="manualBloc" class="operationCreate container header" @update-operation-list="getAccountOperation" @close-form="closeForm" :accountId="this.accountId"/>
        <div v-on:click="onClickFilterButton" class="actionLabelIcon">
          <span class="illustration btn fas fa-filter"/>
          <div class="text">{{ $t("FILTER") }}</div>
        </div>
        <FilterCmpt v-if="filterBloc" @close-filter="closeFilter" @filtering-category="filter"/>
        <template v-for="operation in this.operations" :key="operation">
          <OperationForm class="inlineOperationForm container inline" v-if="operation.editing" @update-operation-list="getAccountOperation" @close-update="closeUpdate" :accountId="this.accountId" :operation="operation"/>
          <span v-else class="allOperation">
            <div v-if="operation.daughters.length == 0" class="operation">
              <div v-on:click="setAsEditing(operation)" :title="$t('EDIT')" class="row col-9">
                <div class="lineStart date col-6">
                  <div>{{ $d(this.getDayAsDate(operation.day), "day") }}</div>
                </div>
                <div class="col-6"/>
                <div class="lineStart category col-6" :class="getClassDependingCategory(operation)">
                  {{ this.getCategoryById(operation.categoryId)?.name ?? $t("UNKNOWN_CATEGORY") }}
                </div>
                <div class="amount col-5 offset-1 col-sm-3 offset-sm-3" :class="this.getClassDependingOnAmount(operation)">
                  {{ addSpacesInThousand(this.getEurosAmount(operation.amount)) }} €
                </div>
              </div>
              <div v-on:click="setAsEditing(operation)" :title="$t('EDIT')" class="action col-1">
                <button class="illustration btn fas fa-pen"/>
              </div>
              <div v-on:click="deleteOperation(operation)" :title="$t('DELETE')" class="action col-1">
                <button class="illustration btn fas fa-trash"/>
              </div>
              <div v-if="operation.pending" v-on:click="debited(operation)" :title="$t('DEBITED')" class="pending col-1">
                <button class="illustration btn fas fa-hourglass-half"/>
              </div>
              <div v-on:click="setAsEditing(operation)" :title="$t('EDIT')" class="lineStart memo col-12">{{ operation.memo }}</div>
            </div>
            <div v-else class="operation">
              <div class="lineStart date col-6">
                <div>{{ $d(this.getDayAsDate(operation.day), "day") }}</div>
              </div>
              <div class="amount col-5 offset-1 col-sm-3 offset-sm-3" :class="this.getClassDependingOnAmount(operation)">
                {{ addSpacesInThousand(this.getEurosAmount(operation.amount)) }} €
              </div>
              <div v-on:click="setAsEditing(operation)" :title="$t('EDIT')" class="lineStart memo col-12 row">{{ operation.memo }}</div>
              <div v-for="daughter in operation.daughters" :key="daughter" class="col-11 offset-1 flexForm">
                <span class="illustration btn fas fa-carrot col-1"/>
                <div class="category col-4" :class="getClassDependingCategoryDaugther(daughter.categoryId)">
                  {{ this.getCategoryById(daughter.categoryId)?.name ?? $t("UNKNOWN_CATEGORY") }}
                </div>
                <div class="amount col-4 offset-1" :class="this.getClassDependingOnAmount(operation)">
                  {{ addSpacesInThousand(this.getEurosAmount(daughter.amount)) }} €
                </div>
                <div class="memo col-11 offset-1">{{ daughter.memo }}</div>
              </div>
            </div>
          </span>
        </template>
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
import { redirectToLoginPageIfNotLogged } from '@/router'
import { Account, Category, Operation } from '@/model/model'
import Time from '@/utils/Time'
import StoreHandler from '@/store/StoreHandler'
import OperationService from '@/services/OperationService'
import OperationForm from '@/components/forms/OperationForm.vue'
import Utils from '@/utils/Utils'
import NavMenu from '@/components/NavigationMenu.vue'
import AccountPageHeader from '@/components/AccountPageHeader.vue'
import ImportOfx from '@/components/ImportOfx.vue'
import FilterCmpt from '@/components/FilterCmpt.vue'

interface AccountPageData {
    operations: EditableOperation[];
    daughterOperations: Operation[];
    importBloc: boolean;
    manualBloc: boolean;
    filterBloc: boolean;
    filteringCategoryId: string | null;
    existingPendingOperation: boolean;
}

interface EditableOperation extends Operation {
  editing: boolean;
  daughters: Operation[];
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
      await this.getDaughterOperations()
      console.log(this.operations)
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
      daughterOperations: [],
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
    totalAccount (): string {
      const value = this.account == null ? 0 : this.getEurosAmount(this.account.amount)
      return this.addSpacesInThousand(value)
    },
    realAmount (): string {
      let value: number = this.account == null ? 0 : this.account.amount
      this.operations.forEach((operation) => {
        if (operation.pending === true) {
          value -= operation.amount
        }
      })
      return this.addSpacesInThousand(value / 100)
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
    async getDaughterOperations () {
      console.log('bonjour')
      this.operations.forEach(async operation => {
        operation.daughters = await OperationService.getDaughterOperationByMother(operation.id)
        if (operation.daughters.length > 0) {
          console.log(operation.daughters)
        }
      })
    },
    getDayAsDate (dayAsInt: number): Date {
      return Time.getDateFromDay(dayAsInt)
    },
    deleteOperation (operation: Operation) {
      OperationService.deleteOperation(this.$store, operation).then(() => {
        this.getAccountOperation()
      })
    },
    setAsEditing (operation: EditableOperation) {
      console.log(operation.pending)
      operation.editing = true
    },
    operationToEditableOperation (operations: Operation[]): EditableOperation[] {
      const editableOperations: EditableOperation[] = []
      operations.forEach((operation) =>
        editableOperations.push({
          id: operation.id,
          day: operation.day,
          accountId: operation.accountId,
          categoryId: operation.categoryId,
          amount: operation.amount,
          memo: operation.memo,
          pending: operation.pending,
          editing: false,
          daughters: []
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
    addSpacesInThousand (number: number): string {
      return Utils.addSpacesInThousand(number)
    },
    getClassDependingCategory (operation: Operation): string {
      return (operation.categoryId === null) ? 'negative' : ''
    },
    getClassDependingCategoryDaugther (categoryId: string): string {
      return categoryId === null ? 'negative' : ''
    },
    filter (categoryId: string) {
      this.filteringCategoryId = categoryId
      this.getAccountOperation()
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
    },
    debited (operation: Operation) {
      if (operation) {
        OperationService.updateOperation(this.$store, operation, this.accountId, operation.day, operation.categoryId, operation.amount, operation.memo, false)
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
    closeFilter () {
      this.filterBloc = false
      this.filteringCategoryId = null
      this.getAccountOperation()
    },
    closeUpdate (operation: EditableOperation) {
      operation.editing = false
    },
    onClickFilterButton () {
      this.filterBloc = !this.filterBloc
      if (!this.filterBloc) {
        this.filteringCategoryId = null
        this.getAccountOperation()
      }
    }
  }
})
</script>
