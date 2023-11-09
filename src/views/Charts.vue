<template>
  <div :class="css">
    <div class="chartPage menuLayout row col-12 offset-0 col-sm-12 offset-sm-0 col-md-10 offset-md-1 col-lg-12 offset-lg-0 col-xl-10 offset-xl-1 col-xxl-8 offset-xxl-2">
      <div class="header fixed title">
        <ChartPageHeader @change-graph="changeGraph"/>
      </div>
      <div class="placeholder top">
        <ChartPageHeader @change-graph="changeGraph"/>
      </div>
      <div class="mobileVersion">
        <DateNav :fromPage="'chart'" @change-month="changeMonth"/>
        <div class="deficitMessage">{{ deficitMessage }}</div>
      </div>
      <div class="draw col-lg-7">
        <PieChart :chartData="pieChartData" v-if="currentGraph == 'pie'"/>
        <BarChart :chartData="barChartData" v-if="currentGraph == 'bar'"/>
      </div>
      <div class="computerVersion col-lg-5">
        <DateNav :fromPage="'chart'" @change-month="changeMonth"/>
        <RadioSelect v-if="currentGraph == 'pie'" :choices="choicesTypeInformationPie" @had-selection="changeTypeInformationPie"/>
        <CheckboxSelect v-if="currentGraph == 'bar'" :choices="choicesTypeInformationBar" @had-selection="changeTypeInformationBar" />
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
        <div class="deficitMessage ">{{ deficitMessage }}</div>
      </div>
      <div class="mobileVersion">
        <RadioSelect v-if="currentGraph == 'pie'" :choices="choicesTypeInformationPie" @had-selection="changeTypeInformationPie"/>
        <CheckboxSelect v-if="currentGraph == 'bar'" :choices="choicesTypeInformationBar" @had-selection="changeTypeInformationBar" />
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
import RadioSelect from '@/components/inputs/RadioSelect.vue'
import CheckboxSelect from '@/components/inputs/CheckboxSelect.vue'
import DateNav from '@/components/inputs/DateNavigation.vue'
import NavMenu from '@/components/NavigationMenu.vue'
import BudgetDataService from '@/services/BudgetDataService'
import type { CategoryDataList, Budget, GroupSelectOption, Category } from '@/model/model'
import Utils from '@/utils/Utils'
import Time from '@/utils/Time'
import { allocatedColor, spentColor, availableColor, redColor, blueColor, orangeColor, purpleColor, greenColor, yellowColor, navyColor, pinkColor, brownColor, blackColor, lightGreyColor, darkGreyColor, lightGreenColor, salmonColor, lavenderColor, bordeauxColor } from '@/utils/Color'
import Multiselect from '@vueform/multiselect'
import { usePersonStore } from '@/stores/personStore'
import { useBudgetStore } from '@/stores/budgetStore'

interface ChartPageData {
    barChartData: { labels: string[]; datasets: { label: string; backgroundColor: string;data: number[] }[] };
    categoryDataList: CategoryDataList;
    pieChartData: { labels: string[]; datasets: { backgroundColor: string[]; data: number[] }[] };
    typeInformationPie: string;
    typeInformationBar: string[];
    masterCategoryId: string;
    currentGraph: string;
    budgetMonth: number;
    choicesTypeInformationPie: { label: string; value: string; preSelected: boolean }[];
    choicesTypeInformationBar: { label: string; value: string; preSelected: boolean }[];
    predefinedListColor: string[];
    colorListMasterCategories: string[];
    deficitCategories: string[];
    deficitMessage: string;
}

export default defineComponent({
  name: 'ChartsView',
  components: {
    ChartPageHeader,
    BarChart,
    PieChart,
    RadioSelect,
    CheckboxSelect,
    DateNav,
    NavMenu,
    Multiselect
  },
  props: { },
  beforeCreate: async function () {
    redirectToLoginPageIfNotLogged()
  },
  created: async function () {
    usePersonStore().init()
    await this.getBudgetData()
    this.recalculate()
    this.getcolorsMasterCategories()
  },
  watch: {
    typeInformationPie: function () {
      this.drawPieChart()
    },
    typeInformationBar: function () {
      this.drawBarChart()
    },
    deficitCategories: function () {
      this.writeAlertMessage()
    }
  },
  data (): ChartPageData {
    return {
      barChartData: {
        labels: [],
        datasets: [{ label: '', backgroundColor: '', data: [] }]
      },
      categoryDataList: {},
      pieChartData: {
        labels: [],
        datasets: [{ backgroundColor: [], data: [] }]
      },
      typeInformationPie: 'available',
      typeInformationBar: ['allocated', 'spent'],
      masterCategoryId: '',
      currentGraph: 'pie',
      budgetMonth: Time.getCurrentMonth(),
      choicesTypeInformationPie: [
        { label: this.$t('ALLOCATED'), value: 'allocated', preSelected: false },
        { label: this.$t('SPENT'), value: 'spent', preSelected: false },
        { label: this.$t('AVAILABLE'), value: 'available', preSelected: true }
      ],
      choicesTypeInformationBar: [
        { label: this.$t('ALLOCATED'), value: 'allocated', preSelected: true },
        { label: this.$t('SPENT'), value: 'spent', preSelected: true },
        { label: this.$t('AVAILABLE'), value: 'available', preSelected: false }
      ],
      predefinedListColor: [redColor, blueColor, orangeColor, darkGreyColor, purpleColor, greenColor, pinkColor, navyColor, lightGreyColor, salmonColor, brownColor, blackColor, bordeauxColor, lavenderColor, yellowColor, lightGreenColor],
      colorListMasterCategories: [],
      deficitCategories: [],
      deficitMessage: ''
    }
  },
  computed: {
    budget (): Budget | null {
      return useBudgetStore().budget
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
      const budgetStore = useBudgetStore()
      for (const masterCategory of budgetStore.masterCategories) {
        const categories = budgetStore.getCategoriesByMasterCategory(masterCategory, false)
        if (categories.length > 0) {
          const newOption = { value: masterCategory.id, label: masterCategory.name }
          optionsList[0].options.push(newOption)
        }
      }
      return optionsList
    },
    css (): string {
      return usePersonStore().css
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
      const budgetStore = useBudgetStore()
      const masterCategorySelected = budgetStore.getMasterCategoryById(masterCategorySelectedId)
      if (masterCategorySelected) {
        const categories = budgetStore.getCategoriesByMasterCategory(masterCategorySelected, false)
        for (const category of categories) {
          listName.push(category.name)
        }
      } else {
        for (const masterCategory of budgetStore.masterCategories) {
          const categories = budgetStore.getCategoriesByMasterCategory(masterCategory, false)
          const archivedCategories = budgetStore.getCategoriesByMasterCategory(masterCategory, true)
          if (!(categories.length === 0 && archivedCategories.length > 0)) {
            listName.push(masterCategory.name)
          }
        }
      }
      return listName
    },
    getDatas (type: string, masterCategorySelectedId: string): number[] {
      this.deficitCategories = []
      const listData: number[] = []
      const budgetStore = useBudgetStore()
      const masterCategorySelected = budgetStore.getMasterCategoryById(masterCategorySelectedId)
      if (masterCategorySelected) {
        const categories = budgetStore.getCategoriesByMasterCategory(masterCategorySelected, false)
        for (const category of categories) {
          const data = this.getCategoryDatas(type, category)
          if (data >= 0 || this.currentGraph === 'bar') {
            listData.push(Utils.getEurosAmount(data))
          } else {
            listData.push(0)
            this.deficitCategories.push(category.name)
          }
        }
      } else {
        for (const masterCategory of budgetStore.masterCategories) {
          const categories = budgetStore.getCategoriesByMasterCategory(masterCategory, false)
          const archivedCategories = budgetStore.getCategoriesByMasterCategory(masterCategory, true)
          if (!(categories.length === 0 && archivedCategories.length > 0)) {
            let data = 0
            for (const category of categories) {
              data += this.getCategoryDatas(type, category)
            }
            if (data >= 0 || this.currentGraph === 'bar') {
              listData.push(Utils.getEurosAmount(data))
            } else {
              listData.push(0)
              this.deficitCategories.push(masterCategory.name)
            }
          }
        }
      }
      return listData
    },
    getCategoryDatas (type: string, category: Category): number {
      let data = 0
      switch (type) {
        case 'allocated':
          data = this.categoryDataList[category.id]?.allocated || 0
          break
        case 'spent':
          data = this.categoryDataList[category.id]?.spent * (-1) || 0
          break
        case 'available':
          data = this.categoryDataList[category.id]?.available || 0
          break
      }
      return data
    },
    getcolorsMasterCategories () {
      let indexColor = 0
      const budgetStore = useBudgetStore()
      for (const masterCategory of budgetStore.masterCategories) {
        const categories = budgetStore.getCategoriesByMasterCategory(masterCategory, false)
        const archivedCategories = budgetStore.getCategoriesByMasterCategory(masterCategory, true)
        if (!(categories.length === 0 && archivedCategories.length > 0)) {
          if (masterCategory.color === null) {
            this.colorListMasterCategories.push(this.predefinedListColor[indexColor])
            indexColor < 10 ? indexColor++ : indexColor = 0
          } else {
            this.colorListMasterCategories.push(masterCategory.color)
          }
        }
      }
    },
    getColorsBarChart (type: string): string {
      let color = ''
      switch (type) {
        case 'allocated':
          color = allocatedColor
          break
        case 'spent':
          color = spentColor
          break
        case 'available':
          color = availableColor
          break
      }
      return color
    },
    drawPieChart () {
      const labelList = this.getLegend(this.masterCategoryId)
      const dataList = this.getDatas(this.typeInformationPie, this.masterCategoryId)
      let colorList: string[] = this.colorListMasterCategories
      if (this.masterCategoryId !== '') {
        colorList = this.predefinedListColor
      }
      this.pieChartData.labels = labelList
      this.pieChartData.datasets[0].data = dataList
      this.pieChartData.datasets[0].backgroundColor = colorList
    },
    drawBarChart () {
      this.barChartData.datasets = []
      if (this.typeInformationBar.indexOf('available') !== -1) {
        this.getBarDatasets('available')
      }
      if (this.typeInformationBar.indexOf('spent') !== -1) {
        this.getBarDatasets('spent')
      }
      if (this.typeInformationBar.indexOf('allocated') !== -1) {
        this.getBarDatasets('allocated')
      }
      this.barChartData.labels = this.getLegend(this.masterCategoryId)
    },
    getBarDatasets (type: string) {
      let legend = ''
      switch (type) {
        case 'allocated':
          legend = this.$t('ALLOCATED')
          break
        case 'spent':
          legend = this.$t('SPENT')
          break
        case 'available':
          legend = this.$t('AVAILABLE')
          break
      }
      const newDatasets = {
        label: legend,
        backgroundColor: this.getColorsBarChart(type),
        data: this.getDatas(type, this.masterCategoryId)
      }
      this.barChartData.datasets.splice(0, 0, newDatasets)
    },
    recalculate () {
      if (this.currentGraph === 'pie') {
        this.drawPieChart()
      } else {
        this.drawBarChart()
      }
    },
    changeGraph (newGraph: string) {
      this.currentGraph = newGraph
      this.recalculate()
    },
    changeTypeInformationPie (newTypeInformationPie: string) {
      this.typeInformationPie = newTypeInformationPie
    },
    changeTypeInformationBar (newTypeInformationBar: string[]) {
      this.typeInformationBar = newTypeInformationBar
    },
    async changeMonth (message: string) {
      if (message === 'next') {
        this.budgetMonth = Time.getNextMonth(this.budgetMonth)
      } else {
        this.budgetMonth = Time.getLastMonth(this.budgetMonth)
      }
      await this.getBudgetData()
      this.recalculate()
    },
    writeAlertMessage () {
      if (this.deficitCategories.length === 0) {
        this.deficitMessage = ''
      } else if (this.deficitCategories.length === 1) {
        this.deficitMessage = this.$t('THE_CATEGORY') + this.deficitCategories[0] + this.$t('IS_IN_DEFICIT')
      } else {
        this.deficitMessage = this.$t('CATEGORIES') + this.deficitCategories[0]
        for (let i = 1; i < this.deficitCategories.length; i++) {
          this.deficitMessage = this.deficitMessage + ', ' + this.deficitCategories[i]
        }
        this.deficitMessage += this.$t('ARE_IN_DEFICIT')
      }
    }
  }
})
</script>
