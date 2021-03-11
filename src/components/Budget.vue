<template>
  <div class="hello">
    <h1>{{ month }}</h1>
    <p>
      Budget of today
    </p>
        <table id="budgetTable" v-for="masterCategory, masterCategoryId in budget" :key="masterCategory">
            <tr>
              <th>{{ masterCategory.name }}</th>
              <th>{{ masterCategory.allocated }}</th>
              <th>{{ masterCategory.spent }}</th>
              <th>{{ masterCategory.available }}</th>
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
}

export default defineComponent({
  name: 'Budget',
  created: async function () {
    this.getCurrentBudget()
  },
  data (): BudgetData {
    return {
      budget: {}
    }
  },
  methods: {
    async getCurrentBudget () {
      budgetService.getBudget().then(
        (budget) => {
          this.budget = budget
        }
      )
    },
    updateAllocation (masterCategoryId: string, categoryId: string, newAllocation: number) {
      console.log('new alloc for ' + categoryId + ' of ' + masterCategoryId + ' : ' + newAllocation)
      this.udpateMasterCategoryAllocation(masterCategoryId)
    },
    udpateMasterCategoryAllocation (id: string) {
      const categories = this.budget[id].categories
      let allocated = 0
      for (const categoryID in categories) {
        allocated += categories[categoryID].allocated
      }
      this.budget[id].allocated = allocated
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
