<template>
  <div :class="this.$store.state.css">
    <div class="chartPage menuLayout row col-md-4 offset-md-4 col-xl-8 offset-xl-2">
      <div class="header fixed title">{{ $t('CHART') }}</div>
      <div class="placeholder top">{{ $t('CHART') }}</div>
      <btn class="actionButton" v-on:click="changeGraph">{{ currentGraph }}</btn>

      <div v-if="currentGraph == 'pie'" class="flexForm">
        <div class="trialTab">
          <btn v-if="typeInformation == 'allocated'" class="tabLeft active" >Alloué</btn>
          <btn v-else class="tabLeft" v-on:click="switchTypeInformation('allocated')">Alloué</btn>
          <btn v-if="typeInformation == 'spent'" class="tabCenter active" >Dépensé</btn>
          <btn v-else class="tabCenter" v-on:click="switchTypeInformation('spent')">Dépensé</btn>
          <btn v-if="typeInformation == 'available'" class="tabRight active">Disponible</btn>
          <btn v-else class="tabRight" v-on:click="switchTypeInformation('available')">Disponible</btn>
        </div>
      </div>

      <Multiselect
        v-on:change="recalculate"
        v-model="masterCategoryId"
        :groups="true"
        :searchable="true"
        :options="masterCategories"
        :noResultsText="$t('NO_RESULT_FOUND')"
        :placeholder="$t('SELECT_MASTER_CATEGORY')"
        @select="recalculate"
      />

      <PieChart :chartData="pieChartData" v-if="currentGraph == 'pie'"/>
      <BarChart :chartData="chartData" v-if="currentGraph == 'bar'"/>

      <div class="row dateNav">
        <div class="col-2 d-flex justify-content-center" ><button type="button" class="btn fas fa-chevron-left" v-on:click="this.goToLastMonth()"/></div>
        <div class="col-8 date-label">
            <p class="title">{{ $d(this.getMonthAsDate(budgetMonth), 'monthString') }} <span v-if="!this.isThisYear"> {{ $d(this.getMonthAsDate(budgetMonth), 'year') }}</span></p>
        </div>
        <div class="col-2 d-flex justify-content-center" ><button type="button" class="btn fas fa-chevron-right" v-on:click="this.goToNextMonth()"/></div>
      </div>

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
import { CategoryDataList, Budget, MasterCategory, GroupSelectOption } from '@/model/model'
import Utils from '@/utils/Utils'
import Time from '@/utils/Time'
import Multiselect from '@vueform/multiselect'

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
    masterCategoryId: string;
    currentGraph: string;
    budgetMonth: number;
}

export default defineComponent({
  name: 'Login',
  components: {
    BarChart,
    PieChart,
    NavMenu,
    Multiselect
  },
  props: { },
  beforeCreate: async function () {
    redirectToLoginPageIfNotLogged(this.$store)
  },
  created: async function () {
    StoreHandler.initStore(this.$store)
    await this.getBudgetData()
    this.recalculate()
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
        labels: [],
        datasets: [
          {
            backgroundColor: [],
            data: []
          }
        ]
      },
      typeInformation: 'spent',
      masterCategoryId: '',
      currentGraph: 'pie',
      budgetMonth: 202207
    }
  },
  computed: {
    budget (): Budget | null {
      return this.$store.state.budget
    },
    masterCategories (): GroupSelectOption[] {
      const optionsList = [
        {
          label: this.$t('CHOOSE'),
          options: [
            { value: '', label: this.$t('ALL_BUDGET') }
          ]
        }
      ]
      for (const masterCategory of this.$store.state.masterCategories) {
        const categories = StoreHandler.getCategoriesByMasterCategory(this.$store, masterCategory, false)
        if (categories.length > 0) {
          const newOption = { value: masterCategory.id, label: masterCategory.name }
          optionsList[0].options.push(newOption)
        }
      }
      return optionsList
    }
  },
  methods: {
    async getBudgetData () {
      if (this.budget) {
        await BudgetDataService.getBudgetDataForMonth(this.budget, this.budgetMonth).then(
          (categoryDataList) => {
            this.categoryDataList = categoryDataList
            console.log(categoryDataList)
          }
        )
      }
    },
    getNames (masterCategorySelectedId: string): string[] {
      const listName: string[] = []
      const masterCategorySelected = StoreHandler.getMasterCategoryById(this.$store, masterCategorySelectedId)
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
    getDatas (type: string, masterCategorySelectedId: string): number[] {
      const listData: number[] = []
      const masterCategorySelected = StoreHandler.getMasterCategoryById(this.$store, masterCategorySelectedId)
      if (masterCategorySelected) {
        const categories = StoreHandler.getCategoriesByMasterCategory(this.$store, masterCategorySelected, false)
        for (const category of categories) {
          switch (type) {
            case 'allocated':
              listData.push(Utils.getEurosAmount(this.categoryDataList[category.id]?.allocated))
              break
            case 'spent':
              listData.push(Utils.getEurosAmount(this.categoryDataList[category.id]?.spent * (-1)))
              break
            case 'available':
              listData.push(Utils.getEurosAmount(this.categoryDataList[category.id]?.available))
              break
          }
        }
      } else {
        for (const masterCategory of this.$store.state.masterCategories) {
          let data = 0
          const categories = StoreHandler.getCategoriesByMasterCategory(this.$store, masterCategory, false)
          for (const category of categories) {
            switch (type) {
              case 'allocated':
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
      const labelList = this.getNames(this.masterCategoryId)
      const dataList = this.getDatas(this.typeInformation, this.masterCategoryId)
      const colorList = this.getColor(false, false, false)
      this.pieChartData.labels = labelList
      this.pieChartData.datasets[0].data = dataList
      this.pieChartData.datasets[0].backgroundColor = colorList
    },
    drawBarChart () {
      const datasetsAllocation = {
        label: 'Allocation',
        backgroundColor: this.getColor(true, false, false),
        data: this.getDatas('allocated', this.masterCategoryId)
      }
      const datasetsSpent = {
        label: 'Dépensé',
        backgroundColor: this.getColor(false, true, false),
        data: this.getDatas('spent', this.masterCategoryId)
      }
      const datasetsAvailable = {
        label: 'Disponible',
        backgroundColor: this.getColor(false, false, true),
        data: this.getDatas('available', this.masterCategoryId)
      }
      this.chartData.labels = this.getNames(this.masterCategoryId)
      this.chartData.datasets = [datasetsAllocation, datasetsSpent, datasetsAvailable]
    },
    changeGraph () {
      switch (this.currentGraph) {
        case 'pie':
          this.currentGraph = 'bar'
          break
        case 'bar':
          this.currentGraph = 'stackedBar'
          break
        default:
          this.currentGraph = 'pie'
      }
    },
    getMonthAsDate (monthAsInt: number): Date {
      return Time.getMonthAsDate(monthAsInt)
    },
    async goToNextMonth () {
      this.budgetMonth = Time.getNextMonth(this.budgetMonth)
      console.log(this.budgetMonth)
      await this.getBudgetData()
      this.recalculate()
    },
    async goToLastMonth () {
      this.budgetMonth = Time.getLastMonth(this.budgetMonth)
      console.log(this.budgetMonth)
      await this.getBudgetData()
      this.recalculate()
    },
    recalculate () {
      this.drawPieChart()
      this.drawBarChart()
    },
    switchTypeInformation (newType: string) {
      switch (newType) {
        case 'allocated':
          this.typeInformation = 'allocated'
          this.recalculate()
          break
        case 'spent':
          this.typeInformation = 'spent'
          this.recalculate()
          break
        case 'available':
          this.typeInformation = 'available'
          this.recalculate()
          break
        default:
          this.typeInformation = ''
      }
    }
  }

})
</script>
