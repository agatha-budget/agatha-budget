<template>
  <tr id="operationForm" class="operation">
    <td class="date"><input id="newOperationDate" type="date" class="form-control" v-model="date"></td>
    <td class="category">
      <select id="newOperationCategory" class="form-control" v-model="categoryId" >
        <option disabled value="">{{$t('SELECT_CATEGORY')}}</option>
        <option v-for="category in this.$store.state.categories" :key="category" v-bind:value="category.id">{{category.name}}</option>
      </select>
    </td>
    <td class="memo"><input id="newOperationMemo" class="form-control" v-model="memo"></td>
    <td class="amount"><input id="newOperationAmount" class="form-control" v-model.number="amount"></td>
    <td class="validation"><button class="btn btn-outline-info" v-on:click="addOperation">{{$t('ADD_OPERATION')}}</button></td>
  </tr>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import OperationService from '@/services/OperationService'
import { CategoryList } from '@/model/model'

export default defineComponent({
  name: 'OperationForm',
  data () {
    return {
      date: '',
      categoryId: '',
      memo: '',
      amount: 0
    }
  },
  props: {
    accountId: {
      type: String,
      required: true
    }
  },
  computed: {
    categories (): CategoryList {
      return this.$store.state.categories
    }
  },
  emits: ['updateOperationList'],
  methods: {
    addOperation () {
      OperationService.addOperation(this.accountId, this.date, this.categoryId, this.amount, this.memo).then(
        () => {
          this.$emit('updateOperationList')
        }
      )
    }
  }
})
</script>
