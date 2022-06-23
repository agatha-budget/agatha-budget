<template>
  <div :class="this.$store.state.css">
    <div class="profilePage menuLayout row col-md-4 offset-md-4 col-xl-8 offset-xl-2">
        <div class="header fixed title">
          {{ $t('CHART') }}
        </div>
        <div class="placeholder top">
          {{ $t('CHART') }}
        </div>

        <button v-on:click="getMCategoriesAsLabels">clique moi</button>
        <button v-on:click="drawSpent">moi aussi wesh</button>

        <BarChart :chartData="chartData"/>
        <div class="placeholder bottom">
            <NavMenu :page="'chart'" />
        </div>
        <div class="footer fixed">
            <NavMenu :page="'chart'" />
        </div>
      </div>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import BarChart from '@/components/charts/BarChart.vue'
import NavMenu from '@/components/NavigationMenu.vue'
import StoreHandler from '@/store/StoreHandler'
import BudgetDataService from '@/services/BudgetDataService'
import { CategoryDataList, Budget } from '@/model/model'
import { store } from '@/store'

interface ChartPageData {
    chartData: {
      labels: string[];
      datasets: {
        label: string;
        backgroundColor: string[];
        data: number[];
      }[];
    };
    categoryDataList: CategoryDataList;
    formerAllocations: {
        [categoryId: string]: number;
    };
}

export default defineComponent({
  name: 'Login',
  components: {
    BarChart,
    NavMenu
  },
  props: { },
  created: async function () {
    this.getBudgetData()
  },
  data (): ChartPageData {
    return {
      chartData: {
        labels: ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'],
        datasets: [
          {
            label: 'GitHub Commits',
            backgroundColor: ['#f87979', '#345654'],
            data: [40, 20, 12, 39, 10, 40, 39, 80, 40, 20, 12, 11]
          }
        ]
      },
      categoryDataList: {},
      formerAllocations: {}
    }
  },
  computed: {
    budget (): Budget | null {
      return this.$store.state.budget
    }
  },
  methods: {
    async getBudgetData () {
      if (this.budget) {
        BudgetDataService.getBudgetDataForMonth(this.budget, 202206).then(
          (categoryDataList) => {
            this.categoryDataList = categoryDataList
            console.log(categoryDataList)
          }
        )
      }
    },
    getMCategoriesAsLabels () {
      const mCategory: string[] = []
      console.log(this.$store.state.masterCategories)
      for (const masterCategory of this.$store.state.masterCategories) {
        console.log(masterCategory.name)
        mCategory.push(masterCategory.name)
      }
      this.chartData.labels = mCategory
      console.log(mCategory)
    },
    drawSpent () {
      const spentByMCategory = []
      for (const masterCategory of this.$store.state.masterCategories) {
        let spent = 0
        const categories = StoreHandler.getCategoriesByMasterCategory(this.$store, masterCategory, false)
        for (const category of categories) {
          console.log(category.name, category.masterCategoryId, category.id)
          console.log(this.categoryDataList[category.id]?.spent)
          spent += this.categoryDataList[category.id]?.spent
        }
        spentByMCategory.push(spent)
        const archivedCategories = StoreHandler.getCategoriesByMasterCategory(this.$store, masterCategory, true)
        if (categories.length === 0 && archivedCategories.length > 0) {
          console.log(masterCategory.name)
        }
      }
      console.log(spentByMCategory)
      this.chartData.datasets[0].data = spentByMCategory
    }
  }

})
</script>
