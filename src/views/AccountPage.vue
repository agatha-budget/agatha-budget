<template >
  <div :class="this.$store.state.css">
    <div class="accountPage row col-lg-10 offset-lg-1 col-xl-8 offset-xl-2">
      <div v-if="this.editingTitle" class="row">
        <span class="name col-4 offset-4">
            <input id="accountName" class="form-control" :placeholder=this.currentName v-model="name">
        </span>
        <span class="validation col">
           <button class="btn fas fa-check" v-on:click="updateName()"/>
           <button class="btn fas fa-times" v-on:click="this.cancelEditing()"/>
        </span>
      </div>
      <div v-else class="editableNameAccount">
        <a v-on:click="this.displayTitleEditing()">
          <h1>
            {{ this.account ? this.account.name : "" }} :
            {{ this.account ? getEurosAmount(this.account.amount) : "" }}€
            <button class="btn fas fa-pen" />
          </h1>
        </a>
      </div>
      <div class="operationTable table-hover">
        <OperationForm class="operationCreate" @update-operation-list="getAccountOperation" :accountId="this.accountId"/>
        <template v-for="operation in this.operations" :key="operation">
          <OperationForm v-if="operation.editing" @update-operation-list="getAccountOperation" :accountId="this.accountId" :operation="operation"/>
          <div v-else class="operation storedOperation">
            <div class="date col-2 offset-1">
              <div>{{ $d(this.getDayAsDate(operation.day), "day") }}</div>
            </div>
            <div class="col-8"></div>
            <div class="category col-3 offset-1">
              {{ this.getCategoryById(operation.categoryId)?.name ?? $t("UNKNOWN_CATEGORY") }}
            </div>
            <div class="amount col-2 offset-2">
              {{ this.getEurosAmount(operation.amount) }} €
            </div>
            <div class="action col-1 offset-1">
              <button class="btn fas fa-pen" v-on:click="setAsEditing(operation)" :title="$t('EDIT')"/>
              <button class="btn fas fa-trash" v-on:click="deleteOperation(operation)" :title="$t('DELETE')"/>
            </div>
            <div class="memo col-3 offset-1">{{ operation.memo }}</div>
          </div>
        </template>
        <ul id="actionsList" class="list-group list-group-horizontal d-flex justify-content-center">
            <li><button v-on:click="goHomePage" class="btn fas fa-home" :title="$t('HOME')"/></li>
            <li><button class="btn fas fa-chart-line disabled" :title="$t('GRAPH_AND_REPORT')"/></li>
            <li><button class="btn fas fa-cog disabled" :title="$t('PREFERENCES')"/></li>
            <li><button v-on:click="logout" class="btn fas fa-sign-out-alt" :title="$t('LOGOUT')"/></li>
          </ul>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import router, { RouterPages, redirectToLoginPageIfNotLogged } from '@/router'
import { Account, Category, Operation } from '@/model/model'
import Time from '@/utils/Time'
import StoreHandler from '@/store/StoreHandler'
import OperationService from '@/services/OperationService'
import AccountService from '@/services/AccountService'
import OperationForm from '@/components/forms/OperationForm.vue'
import Utils from '@/utils/Utils'
import PersonService from '@/services/PersonService'

interface AccountPageData {
    operations: EditableOperation[];
    name: string;
    editingTitle: boolean;
}

interface EditableOperation extends Operation {
  editing: boolean;
}

export default defineComponent({
  name: 'AccountPage',
  components: {
    OperationForm
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
      this.getAccountOperation()
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
      name: '',
      editingTitle: false
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
    currentName (): string {
      return this.account?.name || ''
    }
  },
  emits: ['loosesFocus'],
  methods: {
    async getAccountOperation () {
      if (this.account) {
        return OperationService.getOperations(this.account).then(
          (operations) => {
            this.operations = this.operationToEditableOperation(operations)
          }
        )
      }
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
    updateName () {
      AccountService.updateAccount(this.$store, this.accountId, this.name)
      this.editingTitle = false
    },
    displayTitleEditing () {
      this.editingTitle = true
    },
    cancelEditing () {
      this.editingTitle = false
    },
    logout () {
      PersonService.deleteSession(this.$store)
    },
    async goHomePage (): Promise<void> {
      router.push(RouterPages.home)
    }
  }
})
</script>
