<template>
  <div class="form operationForm" :class={withDaugtherDisplay}>
    <div class="containerCross">
      <span class="cross fas fa-times-circle" v-on:click="closeForm()"/>
    </div>
    <div class="formItem date"> <!-- Date -->
      <label class="label">{{ $t("DATE") }}</label>
      <div><input id="operationDateInput" type="date" class="form-control" v-model="date"></div>
    </div>
    <div class="formItem status"> <!-- Status -->
      <label class="label">{{ $t("STATUS") }}</label>
      <div>
        <label class="customSwitch">
            <input class="switch-input" type="checkbox" v-on:click="inversePending" v-model="isPending"/>
            <span class="switch-label-pending"/>
            <span class="switch-handle-pending"/>
        </label>
        <div v-if="isPending" class="textPending">
          <div class="icon">
            <button class="illustration btn fas fa-hourglass-half"/>
          </div>
          <div class="iconLabel">{{ $t("PENDING") }}</div>
        </div>
        <div v-else class="textPending">
          <div class="icon">
            <button class="illustration btn fas fa-calendar-check"/>
          </div>
          <div class="iconLabel">{{ $t("DEBITED") }}</div>
        </div>
      </div>
    </div>
    <div v-if="daughtersData.length == 0" class="formItem category"> <!-- Envelope, not displayed if daughters -->
      <label class="label">{{ $t("ENVELOPE") }}</label>
      <div class="selectAutoComplete form-group ">
        <Multiselect
          v-model="categoryId"
          :groups="true"
          :searchable="true"
          :options="categories"
          :noResultsText="$t('NO_RESULT_FOUND')"
          :placeholder="$t('CHOOSE')"
        />
      </div>
    </div>
    <div v-if="daughtersData.length == 0" class="formItem amount"> <!-- Amount With No Daugther-->
      <label class="label">{{ $t("AMOUNT") }}</label>
      <div class="amountElement ">
        <div class="amountInput input-group">
          <label class="customSwitch">
            <input class="switch-input" type="checkbox" v-model="incoming"/>
            <span class="switch-label" data-on="+" data-off="-"/>
            <span class="switch-handle"/>
          </label>
          <input id="newOperationAmount" class="form-control" v-model="amountString">
        </div>
      </div>
    </div>
    <div v-else class="formItem amount"> <!-- Amount With Daugther-->
      <label class="label  ">{{ $t("AMOUNT") }}</label>
      <span class="sumAmountElement">
        <template v-if="amountStringIsUnset">
          {{centsToEurosDisplay(signedCentsDaughterSumAmount)}} €
        </template>
        <template v-else>
          {{ incoming ? "" : "-" }}{{ amountString }} €
        </template>
      </span>
    </div>
    <div class="formItem memo"> <!-- Memo -->
      <label class="label">{{ $t("MEMO") }}</label>
      <div class="textInput form-group">
        <input id="operationMemoInput" class="form-control" v-model="memo">
      </div>
    </div>
    <div v-if="daughtersData.length != 0" class="daughters"> <!-- Daugthers -->

      <hr>
      <p class="formSectionTitle">{{ $t("REPARTITION") }}</p>
      <div v-for="daughterOperation of daughtersData" :key="daughterOperation.id" class="form daughter">
        <div class="containerCross">
          <span class="cross fas fa-trash" v-on:click="removeDaughter(daughterOperation)"/>
        </div>
        <div class="formItem category"> <!-- Daugther Enveloppe -->
          <div class="label">{{ $t("ENVELOPE") }}</div>
          <div class="selectAutoComplete form-group ">
            <Multiselect
              v-model="daughterOperation.categoryId"
              :groups="true"
              :searchable="true"
              :options="categories"
              :noResultsText="$t('NO_RESULT_FOUND')"
              :placeholder="$t('CHOOSE')"
            />
          </div>
        </div>
        <div class="formItem amount"> <!-- Daugther Amount -->
          <div class="label">{{ $t("AMOUNT") }}</div>
          <div class="amountElement ">
            <div class="amountInput input-group">
              <label class="customSwitch">
                <input class="switch-input" type="checkbox" v-model="daughterOperation.incoming"/>
                <span class="switch-label" data-on="+" data-off="-"/>
                <span class="switch-handle"/>
              </label>
              <input id="newOperationAmount" class="form-control" v-model="daughterOperation.amountString">
            </div>
          </div>
        </div>
        <div class="formItem memo"> <!-- Daugther Memo -->
          <div class="label">{{ $t("MEMO") }}</div>
          <div class="textInput form-group">
            <input id="operationMemoInput" class="form-control" v-model="daughterOperation.memo">
          </div>
        </div>
      </div>
    </div>
    <div class="addDaughter">  <!-- Add Daugther Action -->
      <button v-if="daughtersData.length == 0" class="actionButton" v-on:click="addDaughter">{{ $t('CREATE_DAUGTHERS') }}</button>
      <button v-else class="actionButton" v-on:click="addDaughter">{{ $t('ADD_NEW_DAUGHTER') }}</button>
    </div>
    <DaughterRepartitionChecker class="daughterChecker" v-if="operation && hasDaughters" :toShare="getSignedCentsAmount(incoming, operation.amount)" :shared="signedCentsDaughterSumAmount"/>
    <div class="formAction" :class={withEditDisplay} v-if="operation"> <!-- Update/Delete Action -->
      <div>
        <button class="actionButton add" v-on:click="updateOperation(operation)" :title="$t('UPDATE')">{{ $t('SUBMIT') }}</button>
      </div>
      <div>
        <button class="actionButton delete" :title="$t('DELETE')" v-on:click="deleteOperation">{{ $t('DELETE') }}</button>
      </div>
    </div>
    <div v-else class=" formAction"> <!-- Create Action -->
      <div>
        <button  class="actionButton add" v-on:click="createOperation()" :title="$t('ADD')">{{ $t('SUBMIT') }}</button>
      </div>
      <div>
        <button class="actionButton cancel" :title="$t('CANCEL')"  v-on:click="closeForm()">{{ $t('CANCEL') }}</button>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import type { Account, Category, GroupSelectOption, MasterCategory, Operation, OperationWithDaughters, SelectOption } from '@/model/model'
import { incomeCategoryId, transfertCategoryId } from '@/model/model'
import OperationService from '@/services/OperationService'
import DaughterRepartitionChecker from '@/components/forms/components/DaughterRepartitionChecker.vue'
import { useBudgetStore } from '@/stores/budgetStore'
import Calcul from '@/utils/Calcul'
import Time from '@/utils/Time'
import Utils from '@/utils/Utils'
import Multiselect from '@vueform/multiselect'
import { defineComponent } from 'vue'

interface DaughterFormData {
  id: string;
  incoming: boolean;
  amountString: string;
  categoryId: string | undefined;
  memo: string | undefined;
}

interface OperationFormData {
  date: string;
  isPending: boolean;
  incoming: boolean;
  amountString: string;
  categoryId: string | undefined;
  memo: string | undefined;
  daughtersData: DaughterFormData[];
}

export default defineComponent({
  name: 'OperationForm',
  components: {
    Multiselect, DaughterRepartitionChecker
  },
  data (): OperationFormData {
    if (this.operation) {
      return {
        date: Time.getDateStringFromDay(this.operation.day),
        isPending: this.operation.pending,
        memo: this.operation.memo,
        categoryId: this.operation.categoryId,
        incoming: this.operation.amount > 0,
        amountString: Utils.centsToEurosDisplay(Math.abs(this.operation?.amount)),
        daughtersData: this.operation.daughters.map(daughter => { return this.daughtersToDaughterData(daughter) })
      }
    } else {
      return {
        date: Time.getCurrentDateString(),
        isPending: false,
        memo: undefined,
        categoryId: undefined,
        incoming: false,
        amountString: Math.abs(0).toString(),
        daughtersData: []
      }
    }
  },
  props: {
    accountId: {
      type: String,
      required: true
    },
    operation: {
      type: Object as () => OperationWithDaughters
    }
  },
  computed: {
    hasDaughters(): boolean {
      return this.daughtersData.length > 0
    },
    withDaugtherDisplay(): string {
      return (this.hasDaughters) ? "withDaughterForm" : ""
    },
    withEditDisplay(): string {
      return (this.operation) ? "isUpdateForm" : ""
    },
    amountStringIsUnset (): boolean {
      return this.amountString === Math.abs(0).toString()
    },
    incomeCategoryId (): string {
      return incomeCategoryId
    },
    transfertCategoryId (): string {
      return transfertCategoryId
    },
    categories (): GroupSelectOption[] {
      const optionsList = [
        {
          label: this.$t('DEFAULT'),
          options: [
            { value: incomeCategoryId, label: this.$t('I18N_INCOME') },
            { value: transfertCategoryId, label: this.$t('I18N_TRANSFER') }
          ]
        }
      ]
      const budgetStore = useBudgetStore()
      for (const masterCategory of budgetStore.masterCategories) {
        const categories = this.getCategoriesByMasterCategory(masterCategory)
        if (categories.length > 0) {
          optionsList.push(this.createOptionGroup(masterCategory, categories))
        }
      }
      return optionsList
    },
    account (): Account | null {
      return this.getAccountById(this.accountId)
    },
    signedCentsAmount (): number {
      if (this.daughtersData.length === 0) {
        return this.getSignedCentsAmountFromString(this.incoming, this.amountString)
      } else {
        return this.signedCentsDaughterSumAmount
      }
    },
    signedCentsDaughterSumAmount (): number {
      let sum = 0
      this.daughtersData.forEach(daughterOperation => {
        if (daughterOperation.incoming) {
          sum += this.computeStringToCents(daughterOperation.amountString)
        } else {
          sum -= this.computeStringToCents(daughterOperation.amountString)
        }
      })
      return sum
    },
    toShareAmountString (): number {
      if (this.incoming) {
        return this.computeStringToCents(this.amountString) - this.signedCentsDaughterSumAmount
      } else {
        return -1 * this.computeStringToCents(this.amountString) - this.signedCentsDaughterSumAmount
      }
    },
    preexistingDaughters(): Operation[] {
      return (this.operation) ? this.operation.daughters : []
    } 
  },
  emits: ['closeForm', 'closeUpdate'],
  methods: {
    daughtersToDaughterData (daughter: Operation): DaughterFormData {
      return {
        id: daughter.id,
        incoming: daughter.amount > 0,
        amountString: Utils.centsToEurosDisplay(Math.abs(daughter.amount)),
        categoryId: daughter.categoryId,
        memo: daughter.memo
      }
    },
    async deleteOperation () {
      if (this.operation) {
        OperationService.deleteOperation(this.operation.accountId, this.operation.id).then(
          (res) => {
            if (res.isOk()){
              useBudgetStore().updateAccounts(false)
            }
          }
        )
      }
    },
    getCategoriesByMasterCategory (masterCategory: MasterCategory): Category[] {
      return useBudgetStore().getCategoriesByMasterCategory(masterCategory, false)
    },
    createOptionGroup (masterCategory: MasterCategory, categories: Category[]): GroupSelectOption {
      const group: GroupSelectOption = {
        label: masterCategory.name,
        options: []
      }
      for (const category of categories) {
        const option: SelectOption = { value: category.id, label: category.name }
        group.options.push(option)
      }
      return group
    },
    inversePending () {
      this.isPending = !this.isPending
    },
    getAccountById (accountId: string): Account | null {
      return useBudgetStore().getAccountById(accountId)
    },
    closeForm () {
      if (this.operation) {
        this.$emit('closeUpdate')
      }
      this.$emit('closeForm')
    },
    addDaughter () {
      this.daughtersData.push(
        {
          id: '',
          incoming: false,
          amountString: '0',
          categoryId: undefined,
          memo: undefined
        }
      )
      if (this.daughtersData.length === 1) { // if the operation has daughter it should have minimum two
        this.addDaughter()
      }
    },
    removeDaughter (daughter: DaughterFormData) {
      const index = this.daughtersData.indexOf(daughter)
      this.daughtersData.splice(index, 1)
    },
    createOperation () {
      // no category for mother operation if it has daughter (overriding if needed)
      let categoryId = (this.hasDaughters) ? undefined : this.categoryId
      OperationService.addOperation(
          this.accountId,
          Time.getDayFromDateString(this.date),
          categoryId,
          this.signedCentsAmount,
          this.memo,
          this.isPending
        ).then(
          (res) => {
            if (res.isOk()){
              let motherOperation = res.value
              this.saveChangesToDaughters(motherOperation.id)
              useBudgetStore().updateAccounts(false)
              this.rebootAddOperationForm()
            }
          }
        )
    },
    updateOperation(operation: OperationWithDaughters) {
      // no category for mother operation if it has daughter (overriding if needed)
      let categoryId = (this.hasDaughters) ? undefined : this.categoryId
      // removeCategory enable the distinction between "category was not updated" and "category was updated to undefined"
      let removeCategory = (this.hasDaughters) ? true : (this.categoryId === undefined || this.categoryId === null)
      OperationService.updateOperation(
        operation.id,
        this.accountId,
        Time.getDayFromDateString(this.date),
        categoryId,
        removeCategory,
        this.signedCentsAmount,
        this.memo,
        this.isPending
      ).then(
        (res) => {
          if (res.isOk()){
            this.saveChangesToDaughters(operation.id)
            useBudgetStore().updateAccounts(false)
          }
        }
      )
      this.closeForm()
    },
    saveChangesToDaughters (motherOperationId: string) {
      this.daughtersData.forEach(daughter => {
        if (this.isPresent(daughter.id, this.preexistingDaughters)) {
          this.updatePreexistingDaughter(daughter)
        } else {
          this.addNewDaughter(daughter, motherOperationId)
        }
      })
      this.deleteRemovedPreexistingDaughter()
    },
    updatePreexistingDaughter(daughter: DaughterFormData) {
      const removeCategory = (daughter.categoryId === undefined || daughter.categoryId === null)
      const amountCent = this.getSignedCentsAmountFromString(daughter.incoming, daughter.amountString)
      OperationService.updateOperation(
        daughter.id,
        this.accountId,
        Time.getDayFromDateString(this.date),
        daughter.categoryId,
        removeCategory,
        amountCent,
        daughter.memo,
        this.isPending
      )
    },
    addNewDaughter(daughter:DaughterFormData, motherOperationId : string) {
      const amountCent = this.getSignedCentsAmountFromString(daughter.incoming, daughter.amountString)
      OperationService.addOperation(
        this.accountId,
        Time.getDayFromDateString(this.date),
        daughter.categoryId,
        amountCent,
        daughter.memo,
        this.isPending,
        motherOperationId
      )
    },
    deleteRemovedPreexistingDaughter() {
      this.preexistingDaughters.forEach(daughter => {
        if (this.operation) {
          // former daughter is not the the new list of daughter of the form
          if (!this.isPresent(daughter.id, this.daughtersData)) {
            OperationService.deleteDaughterOperation(daughter.accountId, daughter.id, daughter.motherOperationId)
          }
        }
      })
    },
    isPresent(daughterId: string, daugtherList: Operation[] | DaughterFormData[]): boolean {
      for (let operation of daugtherList) {
        if (operation.id === daughterId) {
          return true
        }
      }
      return false
    },
    rebootAddOperationForm () {
      this.date = Time.getCurrentDateString()
      this.isPending = false
      this.memo = undefined
      this.categoryId = undefined
      this.incoming = false
      this.amountString = Utils.centsToEurosDisplay(Math.abs(0))
      this.daughtersData = []
    },
    getSignedCentsAmountFromString (incoming: boolean, amountString: string): number {
      const amount = this.computeStringToCents(amountString)
      return getSignedCentsAmount(incoming, amount)
    },
    getSignedCentsAmount (incoming: boolean, amount: number): number {
      return (incoming) ? Math.abs(amount) : Math.abs(amount) * -1
    },
    computeStringToCents (amount: string): number {
      return Calcul.computeStringToCents(amount)
    },
    centsToEurosDisplay (amount: number): string {
      return Utils.centsToEurosDisplay(amount)
    }
  }
})
</script>
