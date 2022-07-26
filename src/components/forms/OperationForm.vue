<template>

  <div v-if="this.dataOperation.operationsData.length == 1">
  <div class="flexForm form">
    <div class="containerCross col-12">
      <span class="cross fas fa-times-circle" v-on:click="closeForm()"/>
    </div>
    <div class="label col-4 offset-0 col-sm-3 offset-sm-1 col-md-1 offset-md-1">{{ $t("DATE") }}</div>
    <div class="col-7 col-sm-6 col-md-3 col-xxl-2"><input id="newOperationDate" type="date" class="form-control" v-model="dataOperation.date"></div>
    <div class="label col-4 offset-0 col-sm-3 offset-sm-1 col-md-2 offset-md-1">{{ $t("ENVELOPE") }}</div>
    <div class="selectAutoComplete form-group col-7 col-sm-6 col-md-3 col-xxl-4">
      <Multiselect
        v-model="dataOperation.operationsData[0].categoryId"
        :groups="true"
        :searchable="true"
        :options="categories"
        :noResultsText="$t('NO_RESULT_FOUND')"
        :placeholder="$t('SELECT_CATEGORY')"
      />
    </div>
    <div class="label col-4 offset-0 col-sm-3 offset-sm-1 col-md-1 offset-md-1">{{ $t("MEMO") }}</div>
    <div class="textInput form-group col-7 col-sm-6 col-md-3 col-lg-3 col-xl-3 col-xxl-2">
      <input id="newOperationMemo" class="form-control" v-model="dataOperation.memo">
    </div>
    <div class="label col-4 offset-0 col-sm-3 offset-sm-1 col-md-2 offset-md-1">{{ $t("AMOUNT") }}</div>
    <div class="amountElement col-7 col-sm-6 col-md-3 col-xxl-4">
      <div class="amountInput input-group flex-nowrap">
        <label class="customSwitch">
          <input class="switch-input" type="checkbox" v-model="dataOperation.operationsData[0].incoming"/>
          <span class="switch-label" data-on="+" data-off="-"/>
          <span class="switch-handle"/>
        </label>
        <input id="newOperationAmount" class="form-control" v-model="dataOperation.operationsData[0].amountString">
      </div>
    </div>
    <div class="label col-4 offset-0 col-sm-3 offset-sm-1 col-md-1 offset-md-1">{{ $t("STATUS") }}</div>
    <div class="col-8 col-sm-6 col-md-6 inline">
      <label class="customSwitch">
          <input class="switch-input" type="checkbox" v-on:click="pending" v-model="dataOperation.isPending"/>
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
    <div class="col-5 offset-1 col-sm-4 col-md-3 offset-md-0">
      <btn class="actionButton" v-on:click="addCategory">{{ $t('ADD_NEW_CATEGORY') }}</btn>
    </div>
    <div class="action col-4 offset-1 col-md-2 offset-md-5">
      <btn v-if="this.operation" class="actionButton" v-on:click="updateOperation" :title="$t('UPDATE')">{{ $t('SUBMIT') }}</btn>
      <btn v-else class="actionButton" v-on:click="addOperation(); rebootAddOperationForm();" :title="$t('ADD')">{{ $t('SUBMIT') }}</btn>
    </div>
    </div>
  </div>

  <div v-else>
  <div class="flexForm form">
    <div class="containerCross col-12">
      <span class="cross fas fa-times-circle" v-on:click="closeForm()"/>
    </div>
    <div class="label col-4 offset-0 col-sm-3 offset-sm-1 col-md-1">{{ $t("DATE") }}</div>
    <div class="col-7 col-sm-6 col-md-3 col-xxl-2"><input id="newOperationDate" type="date" class="form-control" v-model="dataOperation.date"></div>
    <div class="label col-4 offset-0 col-sm-3 offset-sm-1 col-md-2">{{ $t("TOTAL_AMOUNT") }}</div>
    <div class="sumAmountElement col-4 col-sm-3 col-md-2">{{ addSpacesInThousand(totalAmount) }} €</div>
    <div class="col-4 col-sm-5 col-md-2"/>
    <div class="label col-4 offset-0 col-sm-3 offset-sm-1 col-md-1">{{ $t("MEMO") }}</div>
    <div class="textInput form-group col-7 col-sm-6 col-md-3 col-xxl-2">
      <input id="newOperationMemo" class="form-control" v-model="dataOperation.memo">
    </div>
    <div class="label col-4 offset-0 col-sm-3 offset-sm-1 col-md-2 offset-md-1">{{ $t("STATUS") }}</div>
    <div class="col-7 col-sm-6 col-md-3 col-xxl-4 inline">
      <label class="customSwitch">
          <input class="switch-input" type="checkbox" v-on:click="pending" v-model="dataOperation.isPending"/>
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

    <div v-for="daughterOperation in this.dataOperation.operationsData" :key="daughterOperation" class="flexForm form col-12">
    <div class="containerCross col-12 col-sm-11 col-md-12">
      <span class="cross fas fa-trash" v-on:click="removeCategory(dataOperation.operationsData.indexOf(daughterOperation))"/>
    </div>
      <div class="label col-4 offset-0 col-sm-3 offset-sm-1 col-md-2">{{ $t("ENVELOPE") }}</div>
      <div class="selectAutoComplete form-group col-7 col-sm-6 col-md-3">
        <Multiselect
          v-model="dataOperation.operationsData[dataOperation.operationsData.indexOf(daughterOperation)].categoryId"
          :groups="true"
          :searchable="true"
          :options="categories"
          :noResultsText="$t('NO_RESULT_FOUND')"
          :placeholder="$t('SELECT_CATEGORY')"
        />
      </div>
      <div class="label col-4 offset-0 col-sm-3 offset-sm-1 col-md-2 offset-md-0">{{ $t("AMOUNT") }}</div>
      <div class="amountElement col-7 col-sm-6 col-md-3">
        <div class="amountInput input-group flex-nowrap">
          <label class="customSwitch">
            <input class="switch-input" type="checkbox" v-model="dataOperation.operationsData[dataOperation.operationsData.indexOf(daughterOperation)].incoming"/>
            <span class="switch-label" data-on="+" data-off="-"/>
            <span class="switch-handle"/>
          </label>
          <input id="newOperationAmount" class="form-control" v-model="dataOperation.operationsData[dataOperation.operationsData.indexOf(daughterOperation)].amountString">
        </div>
      </div>
      <div class="label col-4 offset-0 col-sm-3 offset-sm-1 col-md-2">{{ $t("MEMO") }}</div>
      <div class="textInput form-group col-7 col-sm-6 col-md-3">
        <input id="newOperationMemo" class="form-control" v-model="dataOperation.operationsData[dataOperation.operationsData.indexOf(daughterOperation)].memo">
      </div>
    </div>

    <div class="col-4 offset-1 col-md-3 offset-md-2">
      <btn v-if="this.operation" class="actionButton" v-on:click="updateOperationMultipleCategories" :title="$t('UPDATE')">{{ $t('SUBMIT') }}</btn>
      <btn v-else class="actionButton" v-on:click="addOperationMultipleCategories()" :title="$t('ADD')">{{ $t('SUBMIT') }}</btn>
    </div>
    <div class="col-4 offset-2 col-md-3 offset-md-2">
      <btn class="actionButton" v-on:click="addCategory">{{ $t('ADD_NEW_CATEGORY') }}</btn>
    </div>

</div>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import OperationService from '@/services/OperationService'
import { Category, MasterCategory, Operation, Account, incomeCategoryId, transfertCategoryId, GroupSelectOption, SelectOption } from '@/model/model'
import Time from '@/utils/Time'
import StoreHandler from '@/store/StoreHandler'
import Utils from '@/utils/Utils'
import Calcul from '@/utils/Calcul'
import Multiselect from '@vueform/multiselect'

interface OperationFormData {
  dataOperation: {
    date: string;
    isPending: boolean;
    memo: string;
    operationsData: {
      incoming: boolean;
      amountString: string;
      categoryId: string;
      memo: string;
    }[];
  };
}

interface MotherOperation extends Operation {
  daughters: Operation[];
}

export default defineComponent({
  name: 'OperationForm',
  components: {
    Multiselect
  },
  data (): OperationFormData {
    return {
      dataOperation: {
        date: this.operation ? Time.getDateStringFromDay(this.operation.day) : Time.getCurrentDateString(),
        isPending: this.operation?.pending || false,
        memo: this.operation?.memo || '',
        operationsData: [{
          incoming: this.operation?.amount ? this.operation.amount > 0 : false,
          amountString: Utils.getEurosAmount(Math.abs(this.operation?.amount || 0)).toString(),
          categoryId: this.operation?.categoryId || '',
          memo: this.operation?.memo || ''
        }]
      }
    }
  },
  created: function () {
    if (this.operation && this.operation.daughters.length > 0) {
      this.operation.daughters.forEach(daughter => {
        const newOperationsData = {
          incoming: daughter.amount ? daughter.amount > 0 : false,
          amountString: Utils.getEurosAmount(Math.abs(daughter.amount)).toString(),
          categoryId: daughter.categoryId,
          memo: daughter.memo
        }
        if (this.operation) {
          this.dataOperation.operationsData[this.operation.daughters.indexOf(daughter)] = newOperationsData
        }
      })
    }
  },
  props: {
    accountId: {
      type: String,
      required: true
    },
    operation: {
      type: Object as () => MotherOperation
    }
  },
  computed: {
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
    totalAmount (): number {
      let sum = 0
      this.dataOperation.operationsData.forEach(daughterOperation => {
        if (daughterOperation.incoming) {
          sum += this.entireCalcul(daughterOperation.amountString)
        } else {
          sum -= this.entireCalcul(daughterOperation.amountString)
        }
      })
      return sum
    }
  },
  emits: ['updateOperationList', 'closeForm', 'closeUpdate'],
  methods: {
    updateOperation () {
      if (this.operation) {
        OperationService.updateOperation(this.$store,
          this.operation.id,
          this.accountId,
          Time.getDayFromDateString(this.dataOperation.date),
          this.dataOperation.operationsData[0].categoryId,
          this.signedCentsAmount(this.dataOperation.operationsData[0].incoming, this.dataOperation.operationsData[0].amountString),
          this.dataOperation.memo,
          this.dataOperation.isPending).then(
          () => {
            this.$emit('updateOperationList')
          }
        )
      } else {
        console.log('warning: tried to update without operation to update')
      }
    },
    addOperation () {
      const accountForTransfer = this.getAccountById(this.dataOperation.operationsData[0].categoryId)
      if (this.account && accountForTransfer) {
        if (this.dataOperation.operationsData[0].incoming) {
          this.categoryForTransfer(accountForTransfer, this.account)
        } else {
          this.categoryForTransfer(this.account, accountForTransfer)
        }
      } else {
        OperationService.addOperation(this.$store,
          this.accountId,
          Time.getDayFromDateString(this.dataOperation.date),
          this.dataOperation.operationsData[0].categoryId,
          this.signedCentsAmount(this.dataOperation.operationsData[0].incoming,
            this.dataOperation.operationsData[0].amountString),
          this.dataOperation.memo,
          this.dataOperation.isPending)
      }
      this.$emit('updateOperationList')
    },
    getCategoriesByMasterCategory (masterCategory: MasterCategory): Category[] {
      return StoreHandler.getCategoriesByMasterCategory(this.$store, masterCategory, false)
    },
    rebootAddOperationForm () {
      this.dataOperation.date = Time.getCurrentDateString()
      this.dataOperation.isPending = false
      this.dataOperation.memo = ''
      this.dataOperation.operationsData = [{
        incoming: false,
        amountString: '',
        categoryId: '',
        memo: ''
      }]
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
    pending () {
      this.dataOperation.isPending = !this.dataOperation.isPending
    },
    createOptionTransfer (accounts: Account[]): GroupSelectOption {
      const group: GroupSelectOption = {
        label: this.$t('I18N_TRANSFER'),
        options: []
      }
      for (const account of accounts) {
        if (account.id !== this.accountId) {
          const option: SelectOption = { value: account.id, label: account.name }
          group.options.push(option)
        }
      }
      return group
    },
    getAccountById (accountId: string): Account | null {
      return StoreHandler.getAccountById(this.$store, accountId)
    },
    categoryForTransfer (debitedAccount: Account, creditedAccount: Account) {
      const amount = Utils.getCentsAmount(this.entireCalcul(this.dataOperation.operationsData[0].amountString))
      OperationService.addOperation(this.$store, debitedAccount.id, Time.getDayFromDateString(this.dataOperation.date), transfertCategoryId, amount * -1, this.dataOperation.memo + this.$t('TRANSFER_TO') + creditedAccount.name)
      OperationService.addOperation(this.$store, creditedAccount.id, Time.getDayFromDateString(this.dataOperation.date), transfertCategoryId, amount, this.dataOperation.memo + this.$t('TRANSFER_FROM') + debitedAccount.name)
    },
    entireCalcul (amount: string): number {
      return Calcul.entireCalcul(amount)
    },
    closeForm () {
      if (this.operation) {
        this.$emit('closeUpdate', this.operation)
      }
      this.$emit('closeForm')
    },
    addCategory () {
      const newOperationData = {
        incoming: false,
        amountString: '0',
        categoryId: '',
        memo: ''
      }
      this.dataOperation.operationsData.push(newOperationData)
    },
    removeCategory (index: number) {
      this.dataOperation.operationsData.splice(index, 1)
    },
    async addOperationMultipleCategories () {
      // mother operation
      const motherOperation = await OperationService.addOperation(this.$store,
        this.accountId,
        Time.getDayFromDateString(this.dataOperation.date),
        undefined,
        Utils.getCentsAmount(this.totalAmount),
        this.dataOperation.memo,
        this.dataOperation.isPending,
        undefined
      )
      // daughters
      this.dataOperation.operationsData.forEach(daughterOperation => {
        let amountCent
        if (daughterOperation.incoming) {
          amountCent = Utils.getCentsAmount(this.entireCalcul(daughterOperation.amountString))
        } else {
          amountCent = Utils.getCentsAmount(this.entireCalcul(daughterOperation.amountString)) * (-1)
        }
        OperationService.addOperation(this.$store,
          this.accountId,
          Time.getDayFromDateString(this.dataOperation.date),
          daughterOperation.categoryId,
          amountCent,
          daughterOperation.memo,
          this.dataOperation.isPending,
          motherOperation.id
        )
      })
      this.rebootAddOperationForm()
    },
    async updateOperationMultipleCategories () {
      // mother operation
      if (this.operation) {
        await OperationService.updateOperation(this.$store,
          this.operation.id,
          this.accountId,
          Time.getDayFromDateString(this.dataOperation.date),
          undefined,
          Utils.getCentsAmount(this.totalAmount),
          this.dataOperation.memo,
          this.dataOperation.isPending,
          undefined
        )
        // daughters
        const daughtersDB = await OperationService.getDaughterOperationByMother(this.operation.id)
        let difference = daughtersDB.length - this.dataOperation.operationsData.length
        while (difference !== 0) {
          if (difference > 0) {
            const daughterOperations = await OperationService.getDaughterOperationByMother(this.operation.id)
            OperationService.deleteOperation(this.$store, daughterOperations[-1])
            difference--
          } else if (difference < 0) {
            daughtersDB.push(await OperationService.addOperation(this.$store, this.accountId))
            difference++
          } else {
            difference = 0
          }
        }
        this.dataOperation.operationsData.forEach(daughter => {
          const index = this.dataOperation.operationsData.indexOf(daughter)
          let amountCent
          if (daughter.incoming) {
            amountCent = Utils.getCentsAmount(this.entireCalcul(daughter.amountString))
          } else {
            amountCent = Utils.getCentsAmount(this.entireCalcul(daughter.amountString)) * -1
          }
          OperationService.updateOperation(this.$store,
            daughtersDB[index].id,
            this.accountId,
            Time.getDayFromDateString(this.dataOperation.date),
            daughter.categoryId,
            amountCent,
            daughter.memo,
            this.dataOperation.isPending
          )
        })
      }
    },
    signedCentsAmount (incoming: boolean, amountString: string): number {
      const amount = this.entireCalcul(amountString)
      return Utils.getCentsAmount((incoming) ? Math.abs(amount) : Math.abs(amount) * -1)
    },
    addSpacesInThousand (amount: number): string {
      return Utils.addSpacesInThousand(amount)
    }
  }
})
</script>
