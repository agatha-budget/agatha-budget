<template>
  <div class="hello">
    <h1>{{ month }}</h1>
    <p>
      Budget of today
    </p>
        <table id="budgetTable" v-for="masterCategorie in budget" :key="masterCategorie">
            <tr>
              <th>{{ masterCategorie.name }}</th>
              <th>{{ masterCategorie.allocated }}</th>
              <th>{{ masterCategorie.spent }}</th>
              <th>{{ masterCategorie.available }}</th>
            </tr>
            <tr v-for="category in masterCategorie.categories" :key="category">
              <td>{{ category.name }}</td>
              <td><input v-model.number=category.allocated type="number" v-on:change="updateAllocation(category)" :placeholder=category.allocated></td>
              <td>{{ category.spent }}</td>
              <td>{{ category.available }}</td>
            </tr>
        </table>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { budgetService, CategoryItem } from '@/services/BudgetService'

export default defineComponent({
  name: 'Budget',
  created: async function () {
    this.getCurrentBudget()
  },
  data () {
    return {
      budget: [{}]
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
    updateAllocation (category: CategoryItem) {
      console.log('new alloc for ' + category.name + ' (' + category.id + '): ' + category.allocated)
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
