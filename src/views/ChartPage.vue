<template>
  <div :class="this.$store.state.css">
    <div class="chartPage menuLayout row col-md-4 offset-md-4 col-xl-8 offset-xl-2">
      <div class="header fixed title">{{ $t('CHART') }}</div>
      <div class="placeholder top">{{ $t('CHART') }}</div>

      <div class="dualTab" v-on:click="changeGraph">
        <btn v-if="currentGraph == 'pie'" class="tabLeft active" >Camembert</btn>
        <btn v-else class="tabLeft" >Camembert</btn>
        <btn v-if="currentGraph == 'bar'" class="tabRight active">Barres</btn>
        <btn v-else class="tabRight">Barres</btn>
      </div>

      <div v-if="currentGraph == 'pie'" class="flexForm">
        <div class="trialTab">
          <btn v-if="typeInformationPie == 'allocated'" class="tabLeft active" >Alloué</btn>
          <btn v-else class="tabLeft" v-on:click="switchTypeInformationPie('allocated')">Alloué</btn>
          <btn v-if="typeInformationPie == 'spent'" class="tabCenter active" >Dépensé</btn>
          <btn v-else class="tabCenter" v-on:click="switchTypeInformationPie('spent')">Dépensé</btn>
          <btn v-if="typeInformationPie == 'available'" class="tabRight active">Disponible</btn>
          <btn v-else class="tabRight" v-on:click="switchTypeInformationPie('available')">Disponible</btn>
        </div>
      </div>

      <div v-if="currentGraph == 'bar'" class="flexForm">
        <div class="trialTab">
          <btn v-if="typeInformationBar.indexOf('allocated') != -1" class="tabLeft active" v-on:click="switchTypeInformationBar('allocated')">Alloué</btn>
          <btn v-else class="tabLeft" v-on:click="switchTypeInformationBar('allocated')">Alloué</btn>
          <btn v-if="typeInformationBar.indexOf('spent') != -1" class="tabCenter active" v-on:click="switchTypeInformationBar('spent')">Dépensé</btn>
          <btn v-else class="tabCenter" v-on:click="switchTypeInformationBar('spent')">Dépensé</btn>
          <btn v-if="typeInformationBar.indexOf('available') != -1" class="tabRight active" v-on:click="switchTypeInformationBar('available')">Disponible</btn>
          <btn v-else class="tabRight" v-on:click="switchTypeInformationBar('available')">Disponible</btn>
        </div>
      </div>

      <div class="multiselect">
        <Multiselect
          v-on:change="recalculate"
          v-model="masterCategoryId"
          :groups="true"
          :searchable="true"
          :options="masterCategories"
          :noResultsText="$t('NO_RESULT_FOUND')"
          :placeholder="$t('SELECT_MASTER_CATEGORY')"
          :show-labels="false"
          @select="recalculate"
        />
      </div>

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
    typeInformationPie: string;
    typeInformationBar: string[];
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
      typeInformationPie: 'spent',
      typeInformationBar: ['allocated', 'spent', 'available'],
      masterCategoryId: '',
      currentGraph: 'pie',
      budgetMonth: Time.getCurrentMonth()
    }
  },
  computed: {
    budget (): Budget | null {
      return this.$store.state.budget
    },
    masterCategories (): GroupSelectOption[] {
      const optionsList = [
        {
          label: this.$t('SELECT'),
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
      const dataList = this.getDatas(this.typeInformationPie, this.masterCategoryId)
      const colorList = this.getColor(false, false, false)
      this.pieChartData.labels = labelList
      this.pieChartData.datasets[0].data = dataList
      this.pieChartData.datasets[0].backgroundColor = colorList
    },
    drawBarChart () {
      this.chartData.datasets = []
      if (this.typeInformationBar.indexOf('available') !== -1) {
        const datasetsAvailable = {
          label: 'Disponible',
          backgroundColor: this.getColor(false, false, true),
          data: this.getDatas('available', this.masterCategoryId)
        }
        this.chartData.datasets.splice(0, 0, datasetsAvailable)
      }
      if (this.typeInformationBar.indexOf('spent') !== -1) {
        const datasetsSpent = {
          label: 'Dépensé',
          backgroundColor: this.getColor(false, true, false),
          data: this.getDatas('spent', this.masterCategoryId)
        }
        this.chartData.datasets.splice(0, 0, datasetsSpent)
      }
      if (this.typeInformationBar.indexOf('allocated') !== -1) {
        const datasetsAllocation = {
          label: 'Allocation',
          backgroundColor: this.getColor(true, false, false),
          data: this.getDatas('allocated', this.masterCategoryId)
        }
        this.chartData.datasets.splice(0, 0, datasetsAllocation)
      }
      this.chartData.labels = this.getNames(this.masterCategoryId)
    },
    changeGraph () {
      switch (this.currentGraph) {
        case 'pie':
          this.currentGraph = 'bar'
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
    switchTypeInformationPie (newType: string) {
      switch (newType) {
        case 'allocated':
          console.log(this.typeInformationBar.indexOf('allocated'))
          this.typeInformationPie = 'allocated'
          this.recalculate()
          break
        case 'spent':
          console.log(this.typeInformationBar.indexOf('spent'))
          this.typeInformationPie = 'spent'
          this.recalculate()
          break
        case 'available':
          console.log(this.typeInformationBar.indexOf('available'))
          this.typeInformationPie = 'available'
          this.recalculate()
          break
        default:
          this.typeInformationPie = ''
      }
    },
    switchTypeInformationBar (newType: string) {
      console.log(this.typeInformationBar)
      console.log(newType)
      switch (newType) {
        case 'allocated':
          console.log(this.typeInformationBar.indexOf('allocated'))
          if (this.typeInformationBar.indexOf('allocated') === -1) {
            this.typeInformationBar.splice(0, 0, 'allocated')
          } else {
            this.typeInformationBar.splice(this.typeInformationBar.indexOf('allocated'), 1)
          }
          this.recalculate()
          break
        case 'spent':
          console.log(this.typeInformationBar.indexOf('spent'))
          if (this.typeInformationBar.indexOf('spent') === -1) {
            this.typeInformationBar.splice(0, 0, 'spent')
          } else {
            this.typeInformationBar.splice(this.typeInformationBar.indexOf('spent'), 1)
          }
          this.recalculate()
          break
        case 'available':
          console.log(this.typeInformationBar.indexOf('available'))
          if (this.typeInformationBar.indexOf('available') === -1) {
            this.typeInformationBar.splice(0, 0, 'available')
          } else {
            this.typeInformationBar.splice(this.typeInformationBar.indexOf('available'), 1)
          }
          this.recalculate()
          break
        default:
          this.typeInformationPie = ''
      }
    }
  }

})
</script>
