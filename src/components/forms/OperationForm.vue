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
      <btn class="actionButton" v-on:click="addCategory">ajouter catégorie</btn>
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
    <div class="sumAmountElement col-4 col-sm-3 col-md-2">{{ displaySumAmount(totalAmount) }} €</div>
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
      <btn class="actionButton " v-on:click="addOperationMultipleCategories">valider</btn>
    </div>
    <div class="col-4 offset-2 col-md-3 offset-md-2">
      <btn class="actionButton" v-on:click="addCategory">ajouter catégorie</btn>
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
  date: string;
  categoryId: string;
  memo: string;
  incoming: boolean;
  amountString: string;
  isPending: boolean;
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

export default defineComponent({
  name: 'OperationForm',
  components: {
    Multiselect
  },
  data (): OperationFormData {
    return {
      date: this.operation ? Time.getDateStringFromDay(this.operation.day) : Time.getCurrentDateString(),
      categoryId: this.operation?.categoryId || '',
      memo: this.operation?.memo || '',
      incoming: this.operation?.amount ? this.operation.amount > 0 : false,
      amountString: Utils.getEurosAmount(Math.abs(this.operation?.amount || 0)).toString(),
      isPending: this.operation?.pending || false,
      dataOperation: {
        date: Time.getCurrentDateString(),
        isPending: false,
        memo: '',
        operationsData: [{
          incoming: false,
          amountString: '0',
          categoryId: '',
          memo: ''
        }]
      }
    }
  },
  props: {
    accountId: {
      type: String,
      required: true
    },
    operation: {
      type: Object as () => Operation
    }
  },
  computed: {
    incomeCategoryId (): string {
      return incomeCategoryId
    },
    transfertCategoryId (): string {
      return transfertCategoryId
    },
    signedCentsAmount (): number {
      return Utils.getCentsAmount((this.incoming) ? Math.abs(this.amount) : Math.abs(this.amount) * -1)
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
    amount (): number {
      return this.entireCalcul(this.amountString)
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
        OperationService.updateOperation(this.$store, this.operation, this.accountId, Time.getDayFromDateString(this.date), this.categoryId, this.signedCentsAmount, this.memo, this.isPending).then(
          () => {
            this.$emit('updateOperationList')
          }
        )
      } else {
        console.log('warning: tried to update without operation to update')
      }
    },
    addOperation () {
      const accountForTransfer = this.getAccountById(this.categoryId)
      if (this.account && accountForTransfer) {
        if (this.incoming) {
          this.categoryForTransfer(accountForTransfer, this.account)
        } else {
          this.categoryForTransfer(this.account, accountForTransfer)
        }
      } else {
        OperationService.addOperation(this.$store, this.accountId, Time.getDayFromDateString(this.date), this.categoryId, this.signedCentsAmount, this.memo, this.isPending)
      }
      this.$emit('updateOperationList')
    },
    getCategoriesByMasterCategory (masterCategory: MasterCategory): Category[] {
      return StoreHandler.getCategoriesByMasterCategory(this.$store, masterCategory, false)
    },
    getArchivedCategories (): Category[] {
      return StoreHandler.getCategoriesByArchivedStatus(this.$store, true)
    },
    rebootAddOperationForm () {
      this.date = Time.getCurrentDateString()
      this.memo = ''
      this.amountString = ''
      this.categoryId = ''
      this.incoming = false
      this.isPending = false
      this.dataOperation.operationsData.forEach(daughter => {
        daughter.incoming = false
        daughter.amountString = ''
        daughter.categoryId = ''
        daughter.memo = ''
      })
      this.dataOperation.date = Time.getCurrentDateString()
      this.dataOperation.isPending = false
      this.dataOperation.memo = ''
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
      this.isPending = !this.isPending
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
      OperationService.addOperation(this.$store, debitedAccount.id, Time.getDayFromDateString(this.date), transfertCategoryId, Utils.getCentsAmount(this.amount * -1), this.memo + this.$t('TRANSFER_TO') + creditedAccount.name)
      OperationService.addOperation(this.$store, creditedAccount.id, Time.getDayFromDateString(this.date), transfertCategoryId, Utils.getCentsAmount(this.amount), this.memo + this.$t('TRANSFER_FROM') + debitedAccount.name)
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
      console.log(this.dataOperation)
    },
    removeCategory (index: number) {
      this.dataOperation.operationsData.splice(index, 1)
      console.log(this.dataOperation)
    },
    displayData () {
      console.log(this.dataOperation)
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
      this.dataOperation.operationsData.forEach(daugtherOperation => {
        let amountCent
        if (daugtherOperation.incoming) {
          amountCent = Utils.getCentsAmount(this.entireCalcul(daugtherOperation.amountString))
        } else {
          amountCent = Utils.getCentsAmount(this.entireCalcul(daugtherOperation.amountString)) * -1
        }
        OperationService.addOperation(this.$store,
          this.accountId,
          Time.getDayFromDateString(this.dataOperation.date),
          daugtherOperation.categoryId,
          amountCent,
          daugtherOperation.memo,
          this.dataOperation.isPending,
          motherOperation.id
        )
      })
      this.rebootAddOperationForm()
    },
    displaySumAmount (number: number): string {
      return Utils.addSpacesInThousand(number)
    }
  }
})
</script>
