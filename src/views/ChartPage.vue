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
        <input type="radio" id="allocated" value="allocated" v-model="typeInformation">
        <label for="allocated">allocated</label>
        <input type="radio" id="spent" value="spent" v-model="typeInformation">
        <label for="spent">spent</label>
        <input type="radio" id="available" value="available" v-model="typeInformation">
        <label for="available">available</label>
        <span>Choisi : {{ typeInformation }}</span>
        <PieChart :chartData="pieChartData"/>

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
import { redirectToLoginPageIfNotLogged } from '@/router'
import BarChart from '@/components/charts/BarChart.vue'
import PieChart from '@/components/charts/PieChart.vue'
import NavMenu from '@/components/NavigationMenu.vue'
import StoreHandler from '@/store/StoreHandler'
import BudgetDataService from '@/services/BudgetDataService'
import { CategoryDataList, Budget, MasterCategory } from '@/model/model'
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
    pieChartData: {
      labels: string[];
      datasets: {
        backgroundColor: string[];
        data: number[];
      }[];
    };
    typeInformation: string;
}

export default defineComponent({
  name: 'Login',
  components: {
    BarChart,
    PieChart,
    NavMenu
  },
  props: { },
  beforeCreate: async function () {
    redirectToLoginPageIfNotLogged(this.$store)
  },
  created: async function () {
    StoreHandler.initStore(this.$store)
    await this.getBudgetData()
    this.drawBarChart()
    this.drawPieChart()
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
      categoryDataList: {},
      pieChartData: {
        labels: ['VueJs', 'EmberJs', 'ReactJs', 'AngularJs'],
        datasets: [
          {
            backgroundColor: ['#41B883', '#E46651', '#00D8FF', '#DD1B16'],
            data: [40, 50, 80, 10]
          }
        ]
      },
      typeInformation: ''
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
    getNames (masterCategorySelected: MasterCategory | null): string[] {
      const listName: string[] = []
      if (masterCategorySelected) {
        const categories = StoreHandler.getCategoriesByMasterCategory(this.$store, masterCategorySelected, false)
        for (const category of categories) {
          listName.push(category.name)
        }
      } else {
        for (const masterCategory of this.$store.state.masterCategories) {
          const categories = StoreHandler.getCategoriesByMasterCategory(this.$store, masterCategory, false)
          const archivedCategories = StoreHandler.getCategoriesByMasterCategory(this.$store, masterCategory, true)
          if (!(categories.length === 0 && archivedCategories.length > 0)) {
            listName.push(masterCategory.name)
          }
        }
      }
      console.log(listName) ///
      return listName
    },
    getDatas (type: string, masterCategorySelected: MasterCategory | null): number[] {
      const listData: number[] = []
      if (masterCategorySelected) {
        const categories = StoreHandler.getCategoriesByMasterCategory(this.$store, masterCategorySelected, false)
        for (const category of categories) {
          switch (type) {
            case 'allocation':
              listData.push(this.categoryDataList[category.id]?.allocated)
              break
            case 'spent':
              listData.push(this.categoryDataList[category.id]?.spent)
              break
            case 'available':
              listData.push(this.categoryDataList[category.id]?.available)
              break
          }
        }
      } else {
        for (const masterCategory of this.$store.state.masterCategories) {
          let data = 0
          const categories = StoreHandler.getCategoriesByMasterCategory(this.$store, masterCategory, false)
          for (const category of categories) {
            switch (type) {
              case 'allocation':
                data += this.categoryDataList[category.id]?.allocated
                break
              case 'spent':
                data += this.categoryDataList[category.id]?.spent * (-1)
                break
              case 'available':
                data += this.categoryDataList[category.id]?.available
                break
            }
          }
          listData.push(Utils.getEurosAmount(data))
        }
      }
      console.log(listData) ///
      return listData
    },
    getColor (allocated: boolean, spent: boolean, available: boolean): string[] {
      let listColor: string[] = []
      if (allocated) {
        listColor.push('#b2babb')
      }
      if (spent) {
        listColor.push('#dc7633')
      }
      if (available) {
        listColor.push('#45c1b8')
      }
      if (!allocated && !spent && !available) {
        listColor = ['#3498db', '#e74c3c', '#27ae60', '#f1c40f', '#fba619', '#8e44ad', '#fb19cb', '#935116', '#17202a', '#fdfefe']
      }
      return listColor
    },
    drawPieChart () {
      const labelList = this.getNames(null)
      const dataList = this.getDatas('spent', null)
      const colorList = this.getColor(false, false, false)
      this.pieChartData.labels = labelList
      this.pieChartData.datasets[0].data = dataList
      this.pieChartData.datasets[0].backgroundColor = colorList
    },
    drawBarChart () {
      const datasetsAllocation = {
        label: 'Allocation',
        backgroundColor: this.getColor(true, false, false),
        data: this.getDatas('allocation', null)
      }
      const datasetsSpent = {
        label: 'Dépensé',
        backgroundColor: this.getColor(false, true, false),
        data: this.getDatas('spent', null)
      }
      const datasetsAvailable = {
        label: 'Disponible',
        backgroundColor: this.getColor(false, false, true),
        data: this.getDatas('available', null)
      }
      this.chartData.labels = this.getNames(null)
      this.chartData.datasets = [datasetsAllocation, datasetsSpent, datasetsAvailable]
    }
  }

})
</script>
