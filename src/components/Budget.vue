<template>
  <div class="col-md-6">
    <h1 class="row">{{ month }}</h1>
    <div id="budgetTables">
    <table class="budgetTable" v-for="masterCategory, masterCategoryId in budget" :key="masterCategory">
        <tr>
          <th class="col-md-6">{{ masterCategoriesData[masterCategoryId].name }}</th>
          <th class="col-md-2">{{ masterCategoriesData[masterCategoryId].allocated }}</th>
          <th class="col-md-2">{{ masterCategoriesData[masterCategoryId].spent }}</th>
          <th class="col-md-2">{{ masterCategoriesData[masterCategoryId].available }}</th>
        </tr>
        <tr v-for="category, categoryId in masterCategory.categories" :key="category">
          <td class="col-md-6">{{ category.name }}</td>
          <td class="col-md-2">
            <input
              class="form-control"
              v-model.number=category.allocated
              type="number"
              v-on:change="updateAllocation(masterCategoryId, categoryId, category.allocated)"
              :placeholder=category.allocated
            >
          </td>
          <td class="col-md-2">{{ category.spent }}</td>
          <td class="col-md-2">{{ category.available }}</td>
        </tr>
    </table>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { budgetService } from '@/services/BudgetService'
import { MasterCategoriesData, MasterCategoryArray } from '@/model/model'

interface BudgetData {
    budget: MasterCategoryArray;
    formerAllocations: {
        [categoryId: string]: number;
    };
}

export default defineComponent({
  name: 'Budget',
  created: async function () {
    this.getCurrentBudget()
  },
  props: ['month'],
  data (): BudgetData {
    return {
      budget: {},
      formerAllocations: {} // use former budget to compute the "available" value from -formerBudget.available + budget.available without asking the back-end to compute
    }
  },
  computed: {
    masterCategoriesData () {
      const masterCategoriesData: MasterCategoriesData = {}
      let category
      for (const masterCategoryId in this.budget) {
        masterCategoriesData[masterCategoryId] = {
          name: this.budget[masterCategoryId].name,
          allocated: 0,
          spent: 0,
          available: 0
        }
        for (const categoryId in this.budget[masterCategoryId].categories) {
          category = this.budget[masterCategoryId].categories[categoryId]
          masterCategoriesData[masterCategoryId].allocated += category.allocated
          masterCategoriesData[masterCategoryId].spent += category.spent
          masterCategoriesData[masterCategoryId].available += category.available
        }
      }
      return masterCategoriesData
    }
  },
  methods: {
    async getCurrentBudget () {
      budgetService.getBudget().then(
        (budget) => {
          this.budget = budget
          this.initFormerAllocation()
        }
      )
    },
    initFormerAllocation () {
      let category
      for (const masterCategoryId in this.budget) {
        for (const categoryId in this.budget[masterCategoryId].categories) {
          category = this.budget[masterCategoryId].categories[categoryId]
          this.formerAllocations[categoryId] = category.allocated
        }
      }
    },
    updateAllocation (masterCategoryId: string, categoryId: string, newAllocation: number) {
      console.log('new alloc for ' + categoryId + ' of ' + masterCategoryId + ' : ' + newAllocation)
      this.budget[masterCategoryId].categories[categoryId].available += (newAllocation - this.formerAllocations[categoryId])
      this.formerAllocations[categoryId] = newAllocation
    }
  }
})
</script>

<style scoped lang="less">
#logo {
  height: 80px;
}
#budgetTables {
  //background: #C6F4FF;
  visibility: 40%;
}
</style>
