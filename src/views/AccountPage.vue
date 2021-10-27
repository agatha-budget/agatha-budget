<template >
  <div :class="this.$store.state.css">
    <div class="accountPage row col-lg-10 offset-lg-1 col-xl-8 offset-xl-2">
        <div v-if="this.editingTitle" class="row">
          <span class="name col-4 offset-4">
            <input id="accountName" class="form-control" v-model="nameComputed">
          </span>
        <span class="validation col">
           <button class="btn fas fa-check" v-on:click="newName()"/>
           <button class="btn fas fa-times" v-on:click="this.cancelEditing()"/>
        </span>
        </div>
          <div v-else class="editableNameAccount">
        <a v-on:click="this.displayTitleEditing()">
          <h1> {{ (this.account) ? this.account.name : ''}} : {{ (this.account) ? getEurosAmount(this.account.amount) : ''}}€
            <button class="btn fas fa-pen"/>
          </h1>
        </a>
      </div>
      <table class="operationTable table table-hover" >
          <tr class="">
            <th class="date col-1 "><div>{{ $t("DATE") }}</div></th>
            <th class="category col-10 col-sm-6 col-md-5 col-lg-4">{{ $t("CATEGORY") }}</th>
            <th class="memo col-6 col-sm-1 col-md-2 col-lg-4">{{ $t("MEMO") }}</th>
            <th class="amount col-5 col-sm-3 col-md-3 col-lg-2">{{ $t("AMOUNT") }}</th>
            <th class="action col-1">{{ $t("ACTION") }}</th>
          </tr>
          <tbody>
          <OperationForm @update-operation-list="getAccountOperation" :accountId="this.accountId" />
          <template v-for="operation in this.operations" :key="operation">
            <OperationForm v-if="operation.editing" @update-operation-list="getAccountOperation" :accountId="this.accountId" :operation="operation"/>
            <tr class="operation storedOperation" v-else>
              <td class="date"><div>{{ $d(this.getDayAsDate(operation.day), 'day') }}</div></td>
              <td class="category">{{ this.getCategoryById(operation.categoryId)?.name ?? $t("UNKNOWN_CATEGORY") }}</td>
              <td class="memo">{{ operation.memo }}</td>
              <td class="amount">{{ this.getEurosAmount(operation.amount) }}</td>
              <td class="action">
                <button class="btn fas fa-pen" v-on:click="setAsEditing(operation)" :title="$t('EDIT')"/>
                <button class="btn fas fa-trash" v-on:click="deleteOperation(operation)" :title="$t('DELETE')"/>
              </td>
            </tr>
          </template>
          </tbody>
      </table>
      <table class="operationTable table table-hover" >
          <tbody>
          <OperationForm @update-operation-list="getAccountOperation" :accountId="this.accountId" />
          <template v-for="operation in this.operations" :key="operation">
            <OperationForm v-if="operation.editing" @update-operation-list="getAccountOperation" :accountId="this.accountId" :operation="operation"/>
            <div class="operation storedOperation" v-else>
            <tr>
              <td class="date"><div>{{ $d(this.getDayAsDate(operation.day), 'day') }}</div></td>
              <td class="category">{{ this.getCategoryById(operation.categoryId)?.name ?? $t("UNKNOWN_CATEGORY") }}</td>
              <td rowspan="2" class="action">
                <button class="btn fas fa-pen" v-on:click="setAsEditing(operation)" :title="$t('EDIT')"/>
                <button class="btn fas fa-trash" v-on:click="deleteOperation(operation)" :title="$t('DELETE')"/>
              </td>
              <td class="memo">{{ operation.memo }}</td>
              <td class="amount">{{ this.getEurosAmount(operation.amount) }}</td>
            </tr>
            </div>
          </template>
          </tbody>
      </table>
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

interface AccountPageData {
    operations: EditableOperation[];
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
      operations: []
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
    }
  },
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
      OperationService.deleteOperation(this.$store, operation).then(
        () => {
          this.getAccountOperation()
        }
      )
    },
    setAsEditing (operation: EditableOperation) {
      operation.editing = true
    },
    operationToEditableOperation (operations: Operation[]): EditableOperation[] {
      const editableOperations: EditableOperation[] = []
      operations.forEach((operation) =>
        editableOperations.push(
          {
            id: operation.id,
            day: operation.day,
            accountId: operation.accountId,
            categoryId: operation.categoryId,
            amount: operation.amount,
            memo: operation.memo,
            editing: false
          }
        )
      )
      return editableOperations
    },
    getCategoryById (categoryId: string): Category | null {
      return StoreHandler.getCategoryById(this.$store, categoryId)
    },
    getEurosAmount (amount: number): number {
      return Utils.getEurosAmount(amount)
    }
  }
})
</script>
