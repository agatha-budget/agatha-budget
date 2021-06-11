<template>
  <div>
    <div id="budgetTables">
      <div class="row">
        <div class="col-2" ><button type="button" class="btn fas fa-chevron-left" v-on:click="this.goToLastMonth()"></button></div>
        <h1 class="col-8">{{ $d(this.getMonthAsDate(budgetMonth), 'monthString') }} <span v-if="!this.isThisYear"> {{ $d(this.getMonthAsDate(budgetMonth), 'year') }}</span></h1>
        <div class="col-2" ><button type="button" class="btn fas fa-chevron-right" v-on:click="this.goToNextMonth()"></button></div>
      </div>
      <div class="row">
        <h2 class="col-8"> {{$t('TO_BE_BUDGETED')}} : {{ this.toBeBudgeted }} </h2>
      </div>
      <table id="totalTable"  class="table">
          <tr>
            <th class="col-6 name"></th>
            <th class="col-2 allocated"><div>{{ $t("ALLOCATED") }}</div></th>
            <th class="col-2 spent"><div>{{ $t("SPENT") }}</div></th>
            <th class="col-2 available"><div>{{ $t("AVAILABLE") }}</div></th>
          </tr>
          <tbody>
          <tr>
            <td class="name"><div>{{ $t("TOTAL") }}</div></td>
            <td class="allocated">{{ this.totalBudgetData.allocated}}</td>
            <td class="spent">{{this.totalBudgetData.spent}}</td>
            <td class="available">{{this.totalBudgetData.available}}</td>
          </tr>
        </tbody>
      </table>
      <table class="budgetTable table"
       v-for="masterCategoryId in Object.keys(this.$store.state.categoriesIdByMasterCategoriesId)"
       :key="masterCategoryId"
      >
          <master-category-cmpt
            @update-allocation="updateAllocation"
            :masterCategoryId="masterCategoryId"
            :categoryDataList="this.categoryDataList"
          />
      </table>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import BudgetDataService from '@/services/BudgetDataService'
import AllocationService from '@/services/AllocationService'
import { Budget, CategoryData, CategoryDataList, incomeCategoryId } from '@/model/model'
import MasterCategoryCmpt from './MasterCategoryCmpt.vue'
import Time from '@/utils/Time'
import Utils from '@/utils/Utils'

interface BudgetCmptData {
    categoryDataList: CategoryDataList;
    formerAllocations: {
        [categoryId: string]: number;
    };
    budgetMonth: number;
}

export default defineComponent({
  name: 'BudgetCmpt',
  components: {
    MasterCategoryCmpt
  },
  props: {
    month: {
      type: Number,
      required: true
    }
  },
  created: async function () {
    this.getBudgetData()
  },
  watch: {
    budget: async function () {
      this.getBudgetData()
    },
    budgetMonth: async function () {
      this.getBudgetData()
    }
  },
  data (): BudgetCmptData {
    return {
      categoryDataList: {},
      /* use former allocation to compute the new "available" value
        newAvailable = available + newAllocation - formerAllocation
        without asking the back-end to compute */
      formerAllocations: {},
      budgetMonth: this.$props.month
    }
  },
  computed: {
    budget (): Budget | null {
      return this.$store.state.budget
    },
    totalBudgetData () {
      const totalBudgetData = new CategoryData()
      for (const categoryId in this.categoryDataList) {
        totalBudgetData.allocated += this.categoryDataList[categoryId].allocated
        totalBudgetData.spent += this.categoryDataList[categoryId].spent
        totalBudgetData.available += this.categoryDataList[categoryId].available
      }
      // use round number in case
      totalBudgetData.allocated = Utils.getRoundedAmount(totalBudgetData.allocated)
      totalBudgetData.spent = Utils.getRoundedAmount(totalBudgetData.spent)
      totalBudgetData.available = Utils.getRoundedAmount(totalBudgetData.available)
      return totalBudgetData
    },
    isThisYear (): boolean {
      return Time.monthIsThisYear(this.budgetMonth)
    },
    toBeBudgeted (): number {
      const incomeForMonth = this.categoryDataList[incomeCategoryId]?.spent
      let toBeBudgeted = incomeForMonth
      for (const categoryId in this.categoryDataList) {
        toBeBudgeted -= this.categoryDataList[categoryId].allocated
      }
      return Utils.getRoundedAmount(toBeBudgeted)
    }
  },
  methods: {
    async getBudgetData () {
      if (this.budget) {
        BudgetDataService.getBudgetDataForMonth(this.budget, this.budgetMonth).then(
          (categoryDataList) => {
            this.categoryDataList = categoryDataList
            this.initFormerAllocation()
          }
        )
      }
    },
    initFormerAllocation () {
      let category
      for (const categoryId in this.categoryDataList) {
        category = this.categoryDataList[categoryId]
        this.formerAllocations[categoryId] = category.allocated
      }
    },
    updateAllocation (categoryId: string, newAllocation: number) {
      if (!this.categoryDataList[categoryId]) {
        this.categoryDataList[categoryId] = new CategoryData()
      }
      this.categoryDataList[categoryId].available +=
        newAllocation - (this.formerAllocations[categoryId] || 0)
      this.categoryDataList[categoryId].allocated = newAllocation
      this.formerAllocations[categoryId] = newAllocation
      AllocationService.updateAllocation(this.budgetMonth, categoryId, newAllocation)
    },
    getMonthAsDate (monthAsInt: number): Date {
      return Time.getMonthAsDate(monthAsInt)
    },
    goToNextMonth () {
      this.budgetMonth = Time.getNextMonth(this.budgetMonth)
    },
    goToLastMonth () {
      this.budgetMonth = Time.getLastMonth(this.budgetMonth)
    }
  }
})
</script>
