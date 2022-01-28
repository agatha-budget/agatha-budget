<template>
<div id="operationForm" class="operation">
    <div class="dateTitle col-3 offset-1 col-sm-2 offset-sm-2 col-md-1 offset-md-1 col-lg-1 offset-lg-1 col-xl-1 offset-xl-1 col-xxl-1 offset-xxl-1">{{ $t("DATE") }}</div>
    <div class="dateElement col-7 col-sm-6 col-md-3 col-lg-3 col-xl-3 col-xxl-2"><input id="newOperationDate" type="date" class="form-control" v-model="date"></div>
    <div class="categoryTitle col-3 offset-1 col-sm-2 offset-sm-2 col-md-2 offset-md-1 col-lg-2 offset-lg-1 col-xl-2 offset-xl-1 col-xxl-2 offset-xxl-1">{{ $t("CATEGORY") }}</div>
    <div class="categoryElement col-7 col-sm-6 col-md-3 col-lg-3 col-xl-3 col-xxl-4">
      <select id="newOperationCategory" class="form-control" v-model="categoryId" >
        <option disabled value="">{{$t('SELECT_CATEGORY')}}</option>
        <option v-bind:value="incomeCategoryId">{{$t('I18N_INCOME')}}</option>
        <option v-bind:value="transfertCategoryId">{{$t('I18N_TRASNFERT')}}</option>
        <optgroup v-for="masterCategory of this.$store.state.masterCategories" :key="masterCategory" v-bind:value="masterCategory.id" :label="masterCategory.name">
          <option v-for="category of this.getCategoriesByMasterCategory(masterCategory)" :key="category" v-bind:value="category.id">{{category.name}}</option>
        </optgroup>
        <optgroup :label="$t('ARCHIVED_CATEGORIES')">
          <option v-for="category of this.getArchivedCategories()" :key="category" v-bind:value="category.id">{{category.name}}</option>
        </optgroup>
      </select>
    </div>
    <div class="memoTitle col-3 offset-1 col-sm-2 offset-sm-2 col-md-1 offset-md-1 col-lg-1 offset-lg-1 col-xl-1 offset-xl-1 col-xxl-1 offset-xxl-1">{{ $t("MEMO") }}</div>
    <div class="memoElement col-7 col-sm-6 col-md-3 col-lg-3 col-xl-3 col-xxl-2"><input id="newOperationMemo" class="form-control" v-model="memo"></div>
    <div class="amountTitle col-3 offset-1 col-sm-2 offset-sm-2 col-md-2 offset-md-1 col-lg-2 offset-lg-1 col-xl-2 offset-xl-1 col-xxl-2 offset-xxl-1">{{ $t("AMOUNT") }}</div>
    <div class="amountElement col-7 col-sm-6 col-md-3 col-lg-3 col-xl-3 col-xxl-4"><div class="input-group flex-nowrap">
      <label class="switch">
        <input class="switch-input" type="checkbox" v-model="incoming"/>
          <span class="switch-label" data-on="+" data-off="-" style="border-radius: 8px"></span>
          <span class="switch-handle"></span>
      </label>
        <input id="newOperationAmount" class="form-control" v-model.number="amount">
    </div>
    </div>
  <div class="action col-1 offset-6">
    <button v-if="this.operation" class="btn fas fa-check" v-on:click="updateOperation" :title="$t('UPDATE')"/>
    <button v-else class="btn fas fa-check" v-on:click="addOperation(); rebootAddOperationForm();" :title="$t('ADD')"/>
  </div>
</div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import OperationService from '@/services/OperationService'
import { Category, MasterCategory, Operation, incomeCategoryId, transfertCategoryId } from '@/model/model'
import Time from '@/utils/Time'
import StoreHandler from '@/store/StoreHandler'
import Utils from '@/utils/Utils'

interface OperationFormData {
  date: string;
  categoryId: string;
  memo: string;
  incoming: boolean;
  amount: number;
}

export default defineComponent({
  name: 'OperationForm',
  data (): OperationFormData {
    return {
      date: this.operation ? Time.getDateStringFromDay(this.operation.day) : Time.getCurrentDateString(),
      categoryId: this.operation?.categoryId || '',
      memo: this.operation?.memo || '',
      incoming: this.operation?.amount ? this.operation.amount > 0 : false,
      amount: Utils.getEurosAmount(Math.abs(this.operation?.amount || 0))
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
    }
  },
  emits: ['updateOperationList'],
  methods: {
    updateOperation () {
      if (this.operation) {
        OperationService.updateOperation(this.$store, this.operation, this.accountId, Time.getDayFromDateString(this.date), this.categoryId, this.signedCentsAmount, this.memo).then(
          () => {
            this.$emit('updateOperationList')
          }
        )
      } else {
        console.log('warning: tried to update without operation to update')
      }
    },
    addOperation () {
      OperationService.addOperation(this.$store, this.accountId, Time.getDayFromDateString(this.date), this.categoryId, this.signedCentsAmount, this.memo).then(
        () => {
          this.$emit('updateOperationList')
        }
      )
    },
    getCategoriesByMasterCategory (masterCategory: MasterCategory): Category[] {
      return StoreHandler.getCategoriesByMasterCategory(this.$store, masterCategory, false)
    },
    getArchivedCategories (): Category[] {
      return StoreHandler.getCategoriesByArchivedStatus(this.$store, true)
    },
    rebootAddOperationForm () {
      this.memo = ''
      this.amount = 0
      this.categoryId = ''
      this.incoming = false
    }
  }
})
</script>
