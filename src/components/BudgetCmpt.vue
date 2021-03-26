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
      <table class="budgetTable table" v-for="masterCategory, masterCategoryId in budgetData" :key="masterCategory">
          <tr class="masterCategory">
            <th class="col-6 name"><div>{{ masterCategoriesData[masterCategoryId].name }}</div></th>
            <th class="col-2 allocated">{{ masterCategoriesData[masterCategoryId].allocated }}</th>
            <th class="col-2 spent">{{ masterCategoriesData[masterCategoryId].spent }}</th>
            <th class="col-2 available">{{ masterCategoriesData[masterCategoryId].available }}</th>
          </tr>
          <tbody>
          <tr class="category" v-for="category, categoryId in masterCategory.categories" :key="category">
            <td class="name"><div>{{ category.name }}</div></td>
            <td class="allocated">
              <input
                class="form-control"
                v-model.number=category.allocated
                type="number"
                v-on:change="updateAllocation(masterCategoryId, categoryId, category.allocated)"
                :placeholder=category.allocated
              >
            </td>
            <td class="spent">{{ category.spent }}</td>
            <td class="available">{{ category.available }}</td>
          </tr>
          </tbody>
      </table>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { budgetService } from '@/services/BudgetService'
import { MasterCategoriesData, MasterCategoryArray } from '@/model/model'

interface BudgetData {
    budgetData: MasterCategoryArray;
    formerAllocations: {
        [categoryId: string]: number;
    };
}

export default defineComponent({
  name: 'BudgetCmpt',
  created: async function () {
    this.getCurrentBudget()
  },
  props: ['month', 'budget'],
  data (): BudgetData {
    return {
      budgetData: {},
      formerAllocations: {} // use former budget to compute the "available" value from -formerBudget.available + budget.available without asking the back-end to compute
    }
  },
  computed: {
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
    async getCurrentBudget () {
      budgetService.getBudgetData(this.$props.budget).then(
        (budgetData) => {
          this.budgetData = budgetData
          this.initFormerAllocation()
        }
      )
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
