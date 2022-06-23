<template>
  <div :class="this.$store.state.css">
    <div class="profilePage menuLayout row col-md-4 offset-md-4 col-xl-8 offset-xl-2">
        <div class="header fixed title">
          {{ $t('CHART') }}
        </div>
        <div class="placeholder top">
          {{ $t('CHART') }}
        </div>

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
import Utils from '@/utils/Utils'

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
}

export default defineComponent({
  name: 'Login',
  components: {
    BarChart,
    NavMenu
  },
  props: { },
  created: async function () {
    await this.getBudgetData()
    this.drawAllocationSpent()
  },
  data (): ChartPageData {
    return {
      chartData: {
        labels: [],
        datasets: [
          {
            label: '',
            backgroundColor: [],
            data: []
          }
        ]
      },
      categoryDataList: {}
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
        await BudgetDataService.getBudgetDataForMonth(this.budget, 202206).then(
          (categoryDataList) => {
            this.categoryDataList = categoryDataList
            console.log(categoryDataList)
          }
        )
      }
    },
    drawAllocationSpent () {
      const allocationByMCategory: number[] = []
      const spentByMCategory: number[] = []
      const nameMCategory: string[] = []
      for (const masterCategory of this.$store.state.masterCategories) {
        let spent = 0
        let allocation = 0
        const categories = StoreHandler.getCategoriesByMasterCategory(this.$store, masterCategory, false)
        for (const category of categories) {
          spent += this.categoryDataList[category.id]?.spent * (-1)
          allocation += this.categoryDataList[category.id]?.allocated
        }
        allocationByMCategory.push(Utils.getEurosAmount(allocation))
        spentByMCategory.push(Utils.getEurosAmount(spent))
        const archivedCategories = StoreHandler.getCategoriesByMasterCategory(this.$store, masterCategory, true)
        if (!(categories.length === 0 && archivedCategories.length > 0)) {
          nameMCategory.push(masterCategory.name)
        }
      }
      const datasetsAllocation = {
        label: 'Allocation',
        backgroundColor: ['#17c825'],
        data: allocationByMCategory
      }
      const datasetsSpent = {
        label: 'Dépensé',
        backgroundColor: ['#ee2525'],
        data: spentByMCategory
      }
      this.chartData.labels = nameMCategory
      this.chartData.datasets = [datasetsAllocation, datasetsSpent]
    }
  }

})
</script>
