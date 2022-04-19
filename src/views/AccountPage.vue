<template >
  <div :class="this.$store.state.css">
    <div class="accountPage row col-lg-10 offset-lg-1 col-xl-8 offset-xl-2">
      <div class="accountPageBody">
        <div class="fixed">
          <div class="col-lg-10 offset-lg-1 col-xl-8 offset-xl-2">
            <AccountPageHeader :accountId="account.id" :totalAccount="this.totalAccount" />
          </div>
        </div>
        <div class="scrollable operationTable table-hover">
          <div class="placeholderTop">
            <AccountPageHeader :accountId="account.id" :totalAccount="this.totalAccount"/>
          </div>
          <div class="dualTab switchOperation">
            <btn v-if="manualBloc" v-on:click="switchAddOperation('manual')" class="tabLeft active">Ajout manuel</btn>
            <btn v-else v-on:click="switchAddOperation('manual')" class="tabLeft">Ajout manuel</btn>
            <btn v-if="importBloc" v-on:click="switchAddOperation('import')" class="tabRight active">Import</btn>
            <btn v-else v-on:click="switchAddOperation('import')" class="tabRight">Import</btn>
          </div>
          <div v-if="importBloc" class="importOfx">
            <ImportOfx :accountId="this.accountId" @close-import="closeImport"/>
          </div>
          <OperationForm v-if="manualBloc" class="operationCreate container header" @update-operation-list="getAccountOperation" @close-form="closeForm" :accountId="this.accountId"/>
          <template v-for="operation in this.operations" :key="operation">
            <OperationForm class="modifyOperation container" v-if="operation.editing" @update-operation-list="getAccountOperation" @close-update="closeUpdate" :accountId="this.accountId" :operation="operation"/>
            <a v-on:click="setAsEditing(operation)" :title="$t('EDIT')" v-else class="operation storedOperation">
              <div class="date col-2 offset-1">
                <div>{{ $d(this.getDayAsDate(operation.day), "day") }}</div>
              </div>
              <div class="col-8"></div>
              <div class="category col-3 offset-1" :class="getClassDependingCategory(operation)">
                {{ this.getCategoryById(operation.categoryId)?.name ?? $t("UNKNOWN_CATEGORY") }}
              </div>
              <div class="amount col-3 offset-2 col-sm-2" :class="this.getClassDependingOnAmount(operation)">
                {{ addSpacesInThousand(this.getEurosAmount(operation.amount)) }} €
              </div>
              <div class="action col-1 offset-1 offset-sm-2">
                <button class="btn fas fa-pen"/>
                <button class="btn fas fa-trash" v-on:click="deleteOperation(operation)" :title="$t('DELETE')"/>
              </div>
              <div class="memo col-3 offset-1">{{ operation.memo }}</div>
            </a>
          </template>
          <div class="placeholderBottom"/>
        </div>
      </div>
      <NavMenu class="btnInAccountPage" />
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

interface AccountPageData {
    operations: EditableOperation[];
    importBloc: boolean;
    manualBloc: boolean;
}

interface EditableOperation extends Operation {
  editing: boolean;
}

export default defineComponent({
  name: 'AccountPage',
  components: {
    OperationForm,
    NavMenu,
    AccountPageHeader,
    ImportOfx
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
      importBloc: false,
      manualBloc: false
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
    getClassDependingOnAmount (operation: Operation): string {
      if (operation.amount > 0) {
        return 'positive'
      } else {
        return ''
      }
    },
    addSpacesInThousand (number: number): string {
      return number.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ' ')
    },
    getClassDependingCategory (operation: Operation): string {
      if (operation.categoryId === null) {
        return 'unknownCategory'
      } else {
        return 'categorySelected'
      }
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
    closeImport () {
      this.importBloc = false
    },
    closeForm () {
      this.manualBloc = false
    },
    closeUpdate (operation: EditableOperation) {
      operation.editing = false
    }
  }
})
</script>
