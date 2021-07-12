<template>
  <tr id="operationForm" class="operation">
    <td class="date"><input id="newOperationDate" type="date" class="form-control" v-model="date"></td>
    <td class="category">
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
    </td>
    <td class="memo"><input id="newOperationMemo" class="form-control" v-model="memo"></td>
    <td class="amount">
      <div class="input-group flex-nowrap">
        <label class="switch">
          <input class="switch-input" type="checkbox" v-model="incoming"/>
          <span class="switch-label" data-on="+" data-off="-" style="border-radius: 8px"></span>
          <span class="switch-handle"></span>
        </label>
        <input id="newOperationAmount" class="form-control" v-model.number="amount">
      </div>
    </td>
    <td class="validation">
      <button v-if="this.operation" class="btn fas fa-check" v-on:click="updateOperation" :title="$t('UPDATE')"/>
      <button v-else class="btn fas fa-check" v-on:click="addOperation" :title="$t('ADD')"/>
      </td>
  </tr>
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
    }
  }
})
</script>
