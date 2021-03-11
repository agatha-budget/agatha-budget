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
            <tr v-for="categorie in masterCategorie.categories" :key="categorie">
              <td>{{ categorie.name }}</td>
              <td>{{ categorie.allocated }}</td>
              <td>{{ categorie.spent }}</td>
              <td>{{ categorie.available }}</td>
            </tr>
        </table>
  </div>
</template>

<script lang="ts">
import { defineComponent, reactive } from 'vue'
import { budgetService } from '@/services/BudgetService'

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
