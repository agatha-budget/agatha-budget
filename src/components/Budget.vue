<template>
  <div class="hello">
    <h1>{{ month }}</h1>
    <p>
      Budget of today
    </p>
        <table id="budgetTable" v-for="masterCategory, masterCategoryId in budget" :key="masterCategory">
            <tr>
              <th>{{ budget[masterCategoryId].name }}</th>
               <th>{{ masterCategoriesData[masterCategoryId].allocated }}</th>
              <th>{{ masterCategoriesData[masterCategoryId].spent }}</th>
              <th>{{ masterCategoriesData[masterCategoryId].available }}</th>
            </tr>
            <tr v-for="category, categoryId in masterCategory.categories" :key="category">
              <td>{{ category.name }}</td>
              <td><input v-model.number=category.allocated type="number" v-on:change="updateAllocation(masterCategoryId, categoryId, category.allocated)" :placeholder=category.allocated></td>
              <td>{{ category.spent }}</td>
              <td>{{ category.available }}</td>
            </tr>
        </table>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { budgetService, MasterCategoryArray } from '@/services/BudgetService'

interface BudgetData {
  budget: MasterCategoryArray;
  formerAllocations: {
    [categoryId: string]: number;
  };
}

interface MasterCategoriesData {
  [masterCategoryId: string]: {
    allocated: number;
    spent: number;
    available: number;
  };
}

export default defineComponent({
  name: 'Budget',
  created: async function () {
    this.getCurrentBudget()
  },
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

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped lang="less">
h3 {
  margin: 40px 0 0;
}
ul {
  list-style-type: none;
  padding: 0;
}
li {
  display: inline-block;
  margin: 0 10px;
}
a {
  color: #42b983;
}
</style>
