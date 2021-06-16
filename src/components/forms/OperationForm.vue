<template>
  <tr id="operationForm" class="operation">
    <td class="date"><input id="newOperationDate" type="date" class="form-control" v-model="date"></td>
    <td class="category">
      <select id="newOperationCategory" class="form-control" v-model="categoryId" >
        <option disabled value="">{{$t('SELECT_CATEGORY')}}</option>
        <option v-for="category of this.$store.state.categories" :key="category" v-bind:value="category.id">{{category.name}}</option>
      </select>
    </td>
    <td class="memo"><input id="newOperationMemo" class="form-control" v-model="memo"></td>
    <td class="amount"><input id="newOperationAmount" class="form-control" v-model.number="amount"></td>
    <td class="validation">
      <button v-if="this.operation" class="btn btn-outline-info" v-on:click="updateOperation">
      {{$t('UPDATE_OPERATION')}}
      </button>
      <button v-else class="btn btn-outline-info" v-on:click="addOperation">
      {{$t('ADD_OPERATION')}}
      </button>
      </td>
  </tr>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import OperationService from '@/services/OperationService'
import { Operation } from '@/model/model'
import Time from '@/utils/Time'

interface OperationFormData {
  date: string;
  categoryId: string;
  memo: string;
  amount: number;
}

export default defineComponent({
  name: 'OperationForm',
  data (): OperationFormData {
    return {
      date: this.operation ? Time.getDateStringFromDay(this.operation.day) : Time.getCurrentDateString(),
      categoryId: this.operation?.categoryId || '',
      memo: this.operation?.memo || '',
      amount: this.operation?.amount || 0
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
  emits: ['updateOperationList'],
  methods: {
    updateOperation () {
      if (this.operation) {
        OperationService.updateOperation(this.operation, this.accountId, Time.getDayFromDateString(this.date), this.categoryId, this.amount, this.memo).then(
          () => {
            this.$emit('updateOperationList')
          }
        )
      } else {
        console.log('warning: tried to update without operation to update')
      }
    },
    addOperation () {
      OperationService.addOperation(this.accountId, Time.getDayFromDateString(this.date), this.categoryId, this.amount, this.memo).then(
        () => {
          this.$emit('updateOperationList')
        }
      )
    }
  }
})
</script>
