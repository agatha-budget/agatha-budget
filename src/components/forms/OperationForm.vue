<template>
  <div class="flexForm form">
    <div class="containerCross col-12">
      <span class="cross fas fa-times-circle" v-on:click="closeForm()"/>
    </div>
    <div class="label col-3 offset-1 col-sm-2 offset-sm-2 col-md-1 offset-md-1">{{ $t("DATE") }}</div>
    <div class="col-7 col-sm-6 col-md-3 col-xxl-2"><input id="newOperationDate" type="date" class="form-control" v-model="date"></div>
    <div class="label col-3 offset-1 col-sm-2 offset-sm-2 col-md-2 offset-md-1">{{ $t("ENVELOPE") }}</div>
    <div class="selectAutoComplete form-group col-7 col-sm-6 col-md-3 col-xxl-4">
      <Multiselect
        v-model="categoryId"
        :groups="true"
        :searchable="true"
        :options="categories"
        :noResultsText="$t('NO_RESULT_FOUND')"
        :placeholder="$t('SELECT_CATEGORY')"
      />
    </div>
    <div class="label col-3 offset-1 col-sm-2 offset-sm-2 col-md-1 offset-md-1">{{ $t("MEMO") }}</div>
    <div class="textInput form-group col-7 col-sm-6 col-md-3 col-lg-3 col-xl-3 col-xxl-2">
      <input id="newOperationMemo" class="form-control" v-model="memo">
    </div>
    <div class="label col-3 offset-1 col-sm-2 offset-sm-2 col-md-2 offset-md-1">{{ $t("AMOUNT") }}</div>
    <div class="amountElement col-7 col-sm-6 col-md-3 col-xxl-4">
      <div class="amountInput input-group flex-nowrap">
        <label class="customSwitch">
          <input class="switch-input" type="checkbox" v-model="incoming"/>
          <span class="switch-label" data-on="+" data-off="-"/>
          <span class="switch-handle"/>
        </label>
        <input id="newOperationAmount" class="form-control" v-model="amountString">
      </div>
    </div>
    <div class="label col-3 offset-1 col-sm-2 offset-sm-2 col-md-1 offset-md-1">{{ $t("STATUS") }}</div>
    <div class="col-8 col-sm-6 col-md-7 col-xxl-8 inline">
      <label class="customSwitch">
          <input class="switch-input" type="checkbox" v-on:click="pending"/>
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
    <div class="action col-4 offset-4 col-md-2 offset-md-5">
      <btn v-if="this.operation" class="actionButton" v-on:click="updateOperation" :title="$t('UPDATE')">{{ $t('SUBMIT') }}</btn>
      <btn v-else class="actionButton" v-on:click="addOperation(); rebootAddOperationForm();" :title="$t('ADD')">{{ $t('SUBMIT') }}</btn>
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
      isPending: this.operation?.pending || false
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
      this.memo = ''
      this.amountString = ''
      this.categoryId = ''
      this.incoming = false
      this.isPending = false
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
    }
  }
})
</script>
