<template>
  <div :class="this.$store.state.css">
    <div class="chartPage menuLayout row col-12 offset-0 col-sm-12 offset-sm-0 col-md-10 offset-md-1 col-lg-12 offset-lg-0 col-xl-10 offset-xl-1 col-xxl-8 offset-xxl-2">
      <div class="header fixed title">
        <ChartPageHeader @change-graph="changeGraph"/>
      </div>
      <div class="placeholder top">
        <ChartPageHeader @change-graph="changeGraph"/>
      </div>
      <div class="mobileVersion">
        <DateNav :fromPage="'chart'" @change-month="changeMonth"/>
      </div>
      <div class="draw col-lg-7">
        <PieChart :chartData="pieChartData" v-if="currentGraph == 'pie'"/>
        <BarChart :chartData="barChartData" v-if="currentGraph == 'bar'"/>
      </div>
      <div class="computerVersion col-lg-5">
        <DateNav :fromPage="'chart'" @change-month="changeMonth"/>
        <RadioSelect v-if="currentGraph == 'pie'" :choices="choicesTypeInformationPie" @had-selection="changeTypeInformationPie"/>
        <div v-if="currentGraph == 'bar'" class="checkboxSelect typeSelection">
          <div class="form-check">
            <input class="form-check-input" type="checkbox" value="allocated" id="allocated" v-model="typeInformationBar">
            <label class="form-check-label" for="allocated">{{ $t('ALLOCATED') }}</label>
          </div>
          <div class="form-check">
            <input class="form-check-input" type="checkbox" value="spent" id="spent" v-model="typeInformationBar">
            <label class="form-check-label" for="spent">{{ $t('SPENT') }}</label>
          </div>
          <div class="form-check">
            <input class="form-check-input" type="checkbox" value="available" id="available" v-model="typeInformationBar">
            <label class="form-check-label" for="available">{{ $t('AVAILABLE') }}</label>
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
      </div>
      <div class="mobileVersion">
        <RadioSelect v-if="currentGraph == 'pie'" :choices="choicesTypeInformationPie" @had-selection="changeTypeInformationPie" class="radioSelect typeSelection"/>
        <div v-if="currentGraph == 'bar'" class="checkboxSelect typeSelection">
          <div class="form-check">
            <input class="form-check-input" type="checkbox" value="allocated" id="allocated" v-model="typeInformationBar">
            <label class="form-check-label" for="allocated">{{ $t('ALLOCATED') }}</label>
          </div>
          <div class="form-check">
            <input class="form-check-input" type="checkbox" value="spent" id="spent" v-model="typeInformationBar">
            <label class="form-check-label" for="spent">{{ $t('SPENT') }}</label>
          </div>
          <div class="form-check">
            <input class="form-check-input" type="checkbox" value="available" id="available" v-model="typeInformationBar">
            <label class="form-check-label" for="available">{{ $t('AVAILABLE') }}</label>
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
import ChartPageHeader from '@/components/headers/ChartPageHeader.vue'
import BarChart from '@/components/charts/BarChart.vue'
import PieChart from '@/components/charts/PieChart.vue'
import RadioSelect from '@/components/RadioSelect.vue'
import DateNav from '@/components/DateNavigation.vue'
import NavMenu from '@/components/NavigationMenu.vue'
import StoreHandler from '@/store/StoreHandler'
import BudgetDataService from '@/services/BudgetDataService'
import { CategoryDataList, Budget, GroupSelectOption, Category } from '@/model/model'
import Utils from '@/utils/Utils'
import Time from '@/utils/Time'
import { allocatedColor, spentColor, availableColor, redColor, blueColor, orangeColor, purpleColor, greenColor, yellowColor, navyColor, pinkColor, brownColor, blackColor } from '@/model/colorList'
import Multiselect from '@vueform/multiselect'

interface ChartPageData {
    barChartData: {
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
    choicesTypeInformationPie: {
      label: string;
      value: string;
      preSelected: boolean;
    }[];
}

export default defineComponent({
  name: 'Login',
  components: {
    ChartPageHeader,
    BarChart,
    PieChart,
    RadioSelect,
    DateNav,
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
  watch: {
    typeInformationPie: function () {
      this.drawPieChart()
    },
    typeInformationBar: function () {
      this.drawBarChart()
    }
  },
  data (): ChartPageData {
    return {
      barChartData: {
        labels: [],
        datasets: [
          { label: '', backgroundColor: [], data: [] }
        ]
      },
      categoryDataList: {},
      pieChartData: {
        labels: [],
        datasets: [
          { backgroundColor: [], data: [] }
        ]
      },
      typeInformationPie: 'available',
      typeInformationBar: ['allocated', 'spent', 'available'],
      masterCategoryId: '',
      currentGraph: 'pie',
      budgetMonth: Time.getCurrentMonth(),
      choicesTypeInformationPie: [
        { label: this.$t('ALLOCATED'), value: 'allocated', preSelected: false },
        { label: this.$t('SPENT'), value: 'spent', preSelected: false },
        { label: this.$t('AVAILABLE'), value: 'available', preSelected: true }
      ]
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
          }
        )
      }
    },
    getLegend (masterCategorySelectedId: string): string[] {
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
      return listName
    },
    getDatas (type: string, masterCategorySelectedId: string): number[] {
      const listData: number[] = []
      const masterCategorySelected = StoreHandler.getMasterCategoryById(this.$store, masterCategorySelectedId)
      if (masterCategorySelected) {
        const categories = StoreHandler.getCategoriesByMasterCategory(this.$store, masterCategorySelected, false)
        for (const category of categories) {
          listData.push(Utils.getEurosAmount(this.getCategoryDatas(type, category)))
        }
      } else {
        for (const masterCategory of this.$store.state.masterCategories) {
          let data = 0
          const categories = StoreHandler.getCategoriesByMasterCategory(this.$store, masterCategory, false)
          for (const category of categories) {
            data += this.getCategoryDatas(type, category)
          }
          listData.push(Utils.getEurosAmount(data))
        }
      }
      return listData
    },
    getCategoryDatas (type: string, category: Category): number {
      let data = 0
      switch (type) {
        case 'allocated':
          data = this.categoryDataList[category.id]?.allocated
          break
        case 'spent':
          data = this.categoryDataList[category.id]?.spent * (-1)
          break
        case 'available':
          data = this.categoryDataList[category.id]?.available
          break
      }
      return data
    },
    getColorsPieChart (): string[] {
      return [redColor, blueColor, orangeColor, purpleColor, greenColor, yellowColor, navyColor, pinkColor, brownColor, blackColor]
    },
    getColorsBarChart (allocated: boolean, spent: boolean, available: boolean): string[] {
      const listColor: string[] = []
      if (allocated) {
        listColor.push(allocatedColor)
      }
      if (spent) {
        listColor.push(spentColor)
      }
      if (available) {
        listColor.push(availableColor)
      }
      return listColor
    },
    drawPieChart () {
      const labelList = this.getLegend(this.masterCategoryId)
      const dataList = this.getDatas(this.typeInformationPie, this.masterCategoryId)
      const colorList = this.getColorsPieChart()
      this.pieChartData.labels = labelList
      this.pieChartData.datasets[0].data = dataList
      this.pieChartData.datasets[0].backgroundColor = colorList
    },
    drawBarChart () {
      this.barChartData.datasets = []
      if (this.typeInformationBar.indexOf('available') !== -1) {
        const datasetsAvailable = {
          label: this.$t('AVAILABLE'),
          backgroundColor: this.getColorsBarChart(false, false, true),
          data: this.getDatas('available', this.masterCategoryId)
        }
        this.barChartData.datasets.splice(0, 0, datasetsAvailable)
      }
      if (this.typeInformationBar.indexOf('spent') !== -1) {
        const datasetsSpent = {
          label: this.$t('SPENT'),
          backgroundColor: this.getColorsBarChart(false, true, false),
          data: this.getDatas('spent', this.masterCategoryId)
        }
        this.barChartData.datasets.splice(0, 0, datasetsSpent)
      }
      if (this.typeInformationBar.indexOf('allocated') !== -1) {
        const datasetsAllocation = {
          label: this.$t('ALLOCATED'),
          backgroundColor: this.getColorsBarChart(true, false, false),
          data: this.getDatas('allocated', this.masterCategoryId)
        }
        this.barChartData.datasets.splice(0, 0, datasetsAllocation)
      }
      this.barChartData.labels = this.getLegend(this.masterCategoryId)
    },
    recalculate () {
      this.drawPieChart()
      this.drawBarChart()
    },
    changeGraph (newGraph: string) {
      this.currentGraph = newGraph
    },
    changeTypeInformationPie (newTypeInformationPie: string) {
      this.typeInformationPie = newTypeInformationPie
    },
    async changeMonth (message: string) {
      if (message === 'next') {
        this.budgetMonth = Time.getNextMonth(this.budgetMonth)
      } else {
        this.budgetMonth = Time.getLastMonth(this.budgetMonth)
      }
      await this.getBudgetData()
      this.recalculate()
    }
  }
})
</script>
