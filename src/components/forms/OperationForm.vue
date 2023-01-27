<template>
  <div class="flexForm form operationForm">
    <div class="containerCross col-12">
      <span class="cross fas fa-times-circle" v-on:click="closeForm()"/>
    </div>
    <div class="formItem col-12 col-md-6 inline"> <!-- Date -->
      <label class="label col-4">{{ $t("DATE") }}</label>
      <div class="col-8"><input id="operationDateInput" type="date" class="form-control" v-model="date"></div>
    </div>
    <div class="formItem col-12 col-md-6 inline"> <!-- Status -->
      <label class="label col-4">{{ $t("STATUS") }}</label>
      <div class="col-8 inline">
        <label class="customSwitch">
            <input class="switch-input" type="checkbox" v-on:click="inversePending" v-model="isPending"/>
            <span class="switch-label-pending"/>
            <span class="switch-handle-pending"/>
        </label>
        <div v-if="isPending" class="inline textPending">
          <div class="icon">
            <button class="illustration btn fas fa-hourglass-half"/>
          </div>
          <div>{{ $t("PENDING") }}</div>
        </div>
        <div v-else class="inline textPending">
          <div class="icon">
            <button class="illustration btn fas fa-calendar-check"/>
          </div>
          <div>{{ $t("DEBITED") }}</div>
        </div>
      </div>
    </div>
    <div v-if="this.daughtersData.length == 0" class="formItem col-12 col-md-6 inline"> <!-- Envelope, not displayed if daughters -->
      <label class="label col-4">{{ $t("ENVELOPE") }}</label>
      <div class="selectAutoComplete form-group col-8">
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
    <div v-if="this.daughtersData.length == 0" class="formItem col-12 col-md-6 inline"> <!-- Amount With No Daugther-->
      <label class="label col-4">{{ $t("AMOUNT") }}</label>
      <div class="amountElement col-8">
        <div class="amountInput input-group flex-nowrap">
          <label class="customSwitch">
            <input class="switch-input" type="checkbox" v-model="incoming"/>
            <span class="switch-label" data-on="+" data-off="-"/>
            <span class="switch-handle"/>
          </label>
          <input id="newOperationAmount" class="form-control" v-model="amountString">
        </div>
      </div>
    </div>
    <div v-else class="formItem col-12 inline"> <!-- Amount With Daugther-->
      <label class="label col-4 col-md-2">{{ $t("AMOUNT") }}</label>
      <div class="sumAmountElement col-8 col-md-10">
        <template v-if="this.amountStringIsUnset">
          {{centsToEurosDisplay(signedCentsDaughterSumAmount)}} €
        </template>
        <template v-else>
          {{ incoming ? "" : "-" }}{{ amountString }} € ( {{centsToEurosDisplay(signedCentsDaughterSumAmount)}} € {{$t('SHARED')}}, {{centsToEurosDisplay(toShareAmountString)}} € {{$t('TO_SHARE')}} )
        </template>
      </div>
    </div>
    <div class="formItem col-12 inline"> <!-- Memo -->
      <label class="label col-4 col-md-2">{{ $t("MEMO") }}</label>
      <div class="textInput form-group col-8 col-md-10">
        <input id="operationMemoInput" class="form-control" v-model="memo">
      </div>
    </div>
    <div v-if="this.daughtersData.length != 0"> <!-- Daugthers -->

      <hr>
      <p class="formSectionTitle">{{ $t("REPARTITION") }}</p>
      <div v-for="daughterOperation of this.daughtersData" :key="daughterOperation" class="flexForm form col-12">
        <div class="containerCross col-12">
          <span class="cross fas fa-trash" v-on:click="removeDaughter(daughterOperation)"/>
        </div>
        <div class="formItem col-12 col-md-6 inline"> <!-- Daugther Enveloppe -->
          <div class="label col-4">{{ $t("ENVELOPE") }}</div>
          <div class="selectAutoComplete form-group col-8">
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
        <div class="formItem col-12 col-md-6 inline"> <!-- Daugther Amount -->
          <div class="label col-4">{{ $t("AMOUNT") }}</div>
          <div class="amountElement col-8">
            <div class="amountInput input-group flex-nowrap">
              <label class="customSwitch">
                <input class="switch-input" type="checkbox" v-model="daughterOperation.incoming"/>
                <span class="switch-label" data-on="+" data-off="-"/>
                <span class="switch-handle"/>
              </label>
              <input id="newOperationAmount" class="form-control" v-model="daughterOperation.amountString">
            </div>
          </div>
        </div>
        <div class="formItem col-12 inline"> <!-- Daugther Memo -->
          <div class="label col-4 col-md-2">{{ $t("MEMO") }}</div>
          <div class="textInput form-group col-8 col-md-10">
            <input id="operationMemoInput" class="form-control" v-model="daughterOperation.memo">
          </div>
        </div>
      </div>
    </div>
    <div class="col-12">  <!-- Add Daugther Action -->
      <btn v-if="this.daughtersData.length == 0" class="actionButton" v-on:click="addDaughter">{{ $t('CREATE_DAUGTHERS') }}</btn>
      <btn v-else class="actionButton" v-on:click="addDaughter">{{ $t('ADD_NEW_DAUGHTER') }}</btn>
    </div>
    <div class="col-12 row formAction" v-if="this.operation"> <!-- Update/Delete Action -->
      <div class="col-6">
        <btn  class="actionButton" v-on:click="saveOperation" :title="$t('UPDATE')">{{ $t('SUBMIT') }}</btn>
      </div>
      <div class="col-6">
        <btn class="actionButton" :title="$t('DELETE')" v-on:click="deleteOperation">{{ $t('DELETE') }}</btn>
      </div>
    </div>
    <div v-else class="col-12 formAction"> <!-- Create Action -->
      <btn  class="actionButton" v-on:click="saveOperation" :title="$t('ADD')">{{ $t('SUBMIT') }}</btn>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import OperationService from '@/services/OperationService'
import { Category, MasterCategory, OperationWithDaughters, Account, incomeCategoryId, transfertCategoryId, GroupSelectOption, SelectOption, Operation } from '@/model/model'
import Time from '@/utils/Time'
import StoreHandler from '@/store/StoreHandler'
import Utils from '@/utils/Utils'
import Calcul from '@/utils/Calcul'
import Multiselect from '@vueform/multiselect'

interface OperationFormData {
  date: string;
  isPending: boolean;
  incoming: boolean;
  amountString: string;
  categoryId: string;
  memo: string;
  daughtersData: DaughterFormData[];
}

interface DaughterFormData {
  id: string;
  incoming: boolean;
  amountString: string;
  categoryId: string;
  memo: string;
}

export default defineComponent({
  name: 'OperationForm',
  components: {
    Multiselect
  },
  data (): OperationFormData {
    if (this.operation) {
      return {
        date: Time.getDateStringFromDay(this.operation.day),
        isPending: this.operation.pending,
        memo: (this.operation.memo === 'null') ? '' : this.operation.memo,
        categoryId: this.operation.categoryId,
        incoming: this.operation.amount > 0,
        amountString: Utils.centsToEurosDisplay(Math.abs(this.operation?.amount)),
        daughtersData: this.operation.daughters.map(daughter => { return this.daughtersToDaughterData(daughter) })
      }
    } else {
      return {
        date: Time.getCurrentDateString(),
        isPending: false,
        memo: '',
        categoryId: '',
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
            { value: incomeCategoryId, label: this.$t('I18N_INCOME') }
          ]
        }
      ]
      const allAccounts = this.$store.state.accounts
      optionsList.push(this.createOptionTransfer(allAccounts))
      for (const masterCategory of this.$store.state.masterCategories) {
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
        return this.getSignedCentsAmount(this.incoming, this.amountString)
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
      return -1 * this.computeStringToCents(this.amountString) - this.signedCentsDaughterSumAmount
    }
  },
  emits: ['updateOperationList', 'closeForm', 'closeUpdate'],
  methods: {
    daughtersToDaughterData (daughter: Operation): DaughterFormData {
      return {
        id: daughter.id,
        incoming: daughter.amount > 0,
        amountString: Utils.centsToEurosDisplay(Math.abs(daughter.amount)),
        categoryId: daughter.categoryId,
        memo: (daughter.memo === 'null') ? '' : daughter.memo
      }
    },
    async deleteOperation () {
      if (this.operation) {
        OperationService.deleteOperation(this.$store, this.operation.id).then(
          () => {
            this.$emit('updateOperationList')
          }
        )
      }
    },
    getCategoriesByMasterCategory (masterCategory: MasterCategory): Category[] {
      return StoreHandler.getCategoriesByMasterCategory(this.$store, masterCategory, false)
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
    createOptionTransfer (accounts: Account[]): GroupSelectOption {
      const group: GroupSelectOption = {
        label: this.$t('I18N_TRANSFER'),
        options: []
      }
      let option: SelectOption = { value: transfertCategoryId, label: this.$t('I18N_TRANSFER') }
      group.options.push(option)
      for (const account of accounts) {
        if (account.id !== this.accountId) {
          option = { value: account.id, label: account.name }
          group.options.push(option)
        }
      }
      return group
    },
    getAccountById (accountId: string): Account | null {
      return StoreHandler.getAccountById(this.$store, accountId)
    },
    createOperationForTransfert (transfertAccount: Account) {
      OperationService.addOperation(
        this.$store,
        this.accountId,
        Time.getDayFromDateString(this.date),
        transfertCategoryId,
        this.signedCentsAmount,
        this.addTransfertNoteToMemo(this.memo, transfertAccount)
      )
    },
    addTransfertNoteToMemo (memo: string, account: Account): string {
      const regex = new RegExp('\\[.*' + this.$t('TRANSFER_TO') + '.*\\]', 'g')
      memo = memo.replace(regex, '')
      return '[ ' + this.$t('TRANSFER_TO') + account.name + '] ' + memo
    },
    closeForm () {
      if (this.operation) {
        this.$emit('closeUpdate', this.operation)
      }
      this.$emit('closeForm')
    },
    addDaughter () {
      this.daughtersData.push(
        {
          id: '',
          incoming: false,
          amountString: '0',
          categoryId: '',
          memo: ''
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
    async saveOperation () {
      // if transfert use specific category and memo
      const accountForTransfer = this.getAccountById(this.categoryId)
      let categoryId: string | undefined = (accountForTransfer) ? transfertCategoryId : this.categoryId
      let memo = (accountForTransfer) ? this.addTransfertNoteToMemo(this.memo, accountForTransfer) : this.memo

      // no category for mother operation if it has daughter  (overriding transfer data if needed)
      if (this.daughtersData.length !== 0) {
        categoryId = undefined
        memo = this.memo
      }
      if (this.operation) {
        console.log(this.daughtersData)
        OperationService.updateOperation(this.$store,
          this.operation.id,
          this.accountId,
          Time.getDayFromDateString(this.date),
          categoryId,
          this.signedCentsAmount,
          memo,
          this.isPending
        )
        this.saveChangesToDaughters(this.operation.id)
      } else {
        console.log(this.daughtersData)
        const motherOperation = await OperationService.addOperation(this.$store,
          this.accountId,
          Time.getDayFromDateString(this.date),
          this.categoryId,
          this.signedCentsAmount,
          this.memo,
          this.isPending
        )
        this.saveChangesToDaughters(motherOperation.id)
        this.rebootAddOperationForm()
      }
      this.$emit('updateOperationList')
    },
    saveChangesToDaughters (motherOperationId: string) {
      const preexistingDaughters = (this.operation) ? this.operation.daughters : []

      this.daughtersData.forEach(daughter => {
        // if transfert use specific category and memo
        const accountForTransfer = this.getAccountById(daughter.categoryId)
        const categoryId: string | undefined = (accountForTransfer) ? transfertCategoryId : daughter.categoryId
        const memo = (accountForTransfer) ? this.addTransfertNoteToMemo(daughter.memo, accountForTransfer) : daughter.memo

        const amountCent = this.getSignedCentsAmount(daughter.incoming, daughter.amountString)
        // update an existing daughter
        if (this.daughterAlreadyExist(daughter, preexistingDaughters)) {
          OperationService.updateOperation(this.$store,
            daughter.id,
            this.accountId,
            Time.getDayFromDateString(this.date),
            categoryId,
            amountCent,
            memo,
            this.isPending
          )
        // or create new daughter
        } else {
          OperationService.addOperation(this.$store,
            this.accountId,
            Time.getDayFromDateString(this.date),
            categoryId,
            amountCent,
            memo,
            this.isPending,
            motherOperationId
          )
        }
      })

      // check if preexisting daughters were deleted
      preexistingDaughters.forEach(daughter => {
        if (this.operation) {
          if (this.daughterWasDeleted(daughter, this.daughtersData)) {
            OperationService.deleteOperation(this.$store, daughter.id)
          }
        }
      })
    },
    daughterAlreadyExist (daughter: DaughterFormData, list: Operation[]): boolean {
      list.forEach(operation => {
        if (operation.id === daughter.id) {
          return true
        }
      })
      return false
    },
    daughterWasDeleted (daughter: Operation, list: DaughterFormData[]): boolean {
      list.forEach(operation => {
        if (operation.id === daughter.id) {
          return false
        }
      })
      return true
    },
    rebootAddOperationForm () {
      this.date = Time.getCurrentDateString()
      this.isPending = false
      this.memo = ''
      this.categoryId = ''
      this.incoming = false
      this.amountString = Utils.centsToEurosDisplay(Math.abs(0))
      this.daughtersData = []
    },
    getSignedCentsAmount (incoming: boolean, amountString: string): number {
      const amount = this.computeStringToCents(amountString)
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
