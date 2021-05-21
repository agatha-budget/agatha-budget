<template>
  <div>
    <div id="budgetTables">
      <h1>{{ month }}</h1>
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
            <td class="allocated">{{this.totalBudgetData.allocated}}</td>
            <td class="spent">{{this.totalBudgetData.spent}}</td>
            <td class="available">{{this.totalBudgetData.available}}</td>
          </tr>
        </tbody>
      </table>
      <table class="budgetTable table" v-for="masterCategoryId in Object.keys(this.$store.state.categoriesIdByMasterCategoriesId)" :key="masterCategoryId">
          <master-category-cmpt
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
import { Budget, CategoryDataList } from '@/model/model'
import MasterCategoryCmpt from './MasterCategoryCmpt.vue'

interface BudgetCmptData {
    categoryDataList: CategoryDataList;
    formerAllocations: {
        [categoryId: string]: number;
    };
}

export default defineComponent({
  name: 'BudgetCmpt',
  components: {
    MasterCategoryCmpt
  },
  props: ['month'],
  created: async function () {
    this.getBudgetData()
  },
  watch: {
    budget: async function () {
      this.getBudgetData()
    }
  },
  data (): BudgetCmptData {
    return {
      categoryDataList: {},
      formerAllocations: {} // use former budget to compute the "available" value from -formerBudget.available + budget.available without asking the back-end to compute
    }
  },
  computed: {
    budget (): Budget | null {
      return this.$store.state.budget
    },
    totalBudgetData () {
      const totalBudgetData = {
        allocated: 0,
        spent: 0,
        available: 0
      }
      for (const categoryId in this.categoryDataList) {
        totalBudgetData.allocated += this.categoryDataList[categoryId].allocated
        totalBudgetData.spent += this.categoryDataList[categoryId].spent
        totalBudgetData.available += this.categoryDataList[categoryId].available
      }
      return totalBudgetData
    }
  },
  methods: {
    async getBudgetData () {
      if (this.budget) {
        BudgetDataService.getBudgetDataForMonth(this.budget, this.month).then(
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
      console.log('new alloc for ' + categoryId + ' : ' + newAllocation)
      this.categoryDataList[categoryId].available += (newAllocation - this.formerAllocations[categoryId])
      this.formerAllocations[categoryId] = newAllocation
    }
  }
})
</script>
