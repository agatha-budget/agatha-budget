<template>
  <div>
    <div id="budgetTables">
      <h1>{{ $t(month) }}</h1>
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
            :masterCategoryData="masterCategoriesData[masterCategoryId]"
          />
      </table>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import BudgetService from '@/services/BudgetService'
import { MasterCategoriesData, BudgetData, Budget } from '@/model/model'
import MasterCategoryCmpt from './MasterCategoryCmpt.vue'

interface BudgetCmptData {
    budgetData: BudgetData;
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
      budgetData: {},
      formerAllocations: {} // use former budget to compute the "available" value from -formerBudget.available + budget.available without asking the back-end to compute
    }
  },
  computed: {
    budget (): Budget | null {
      return this.$store.state.budget
    },
    masterCategoriesData () {
      const masterCategoriesData: MasterCategoriesData = {}
      let category
      for (const masterCategoryId in this.budgetData) {
        masterCategoriesData[masterCategoryId] = {
          name: this.budgetData[masterCategoryId].name,
          allocated: 0,
          spent: 0,
          available: 0
        }
        for (const categoryId in this.budgetData[masterCategoryId].categories) {
          category = this.budgetData[masterCategoryId].categories[categoryId]
          masterCategoriesData[masterCategoryId].allocated += category.allocated
          masterCategoriesData[masterCategoryId].spent += category.spent
          masterCategoriesData[masterCategoryId].available += category.available
        }
      }
      return masterCategoriesData
    },
    totalBudgetData () {
      const totalBudgetData = {
        allocated: 0,
        spent: 0,
        available: 0
      }
      for (const masterCategoryId in this.masterCategoriesData) {
        totalBudgetData.allocated += this.masterCategoriesData[masterCategoryId].allocated
        totalBudgetData.spent += this.masterCategoriesData[masterCategoryId].spent
        totalBudgetData.available += this.masterCategoriesData[masterCategoryId].available
      }
      return totalBudgetData
    }
  },
  methods: {
    async getBudgetData () {
      if (this.budget) {
        BudgetService.getBudgetData(this.budget).then(
          (budgetData) => {
            this.budgetData = budgetData
            this.initFormerAllocation()
          }
        )
      }
    },
    initFormerAllocation () {
      let category
      for (const masterCategoryId in this.budgetData) {
        for (const categoryId in this.budgetData[masterCategoryId].categories) {
          category = this.budgetData[masterCategoryId].categories[categoryId]
          this.formerAllocations[categoryId] = category.allocated
        }
      }
    },
    updateAllocation (masterCategoryId: string, categoryId: string, newAllocation: number) {
      console.log('new alloc for ' + categoryId + ' of ' + masterCategoryId + ' : ' + newAllocation)
      this.budgetData[masterCategoryId].categories[categoryId].available += (newAllocation - this.formerAllocations[categoryId])
      this.formerAllocations[categoryId] = newAllocation
    }
  }
})
</script>
