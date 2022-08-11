<template>
  <div id="budgetCmpt" class="container-fluid col-12 offset-0 col-sm-10 offset-sm-1 col-md-8 offset-md-2 col-lg-6 offset-lg-1 col-xl-5 offset-xl-2">
    <div class="header fixed">
      <div class="col-12 offset-0 col-sm-10 offset-sm-1 col-md-8 offset-md-2 col-lg-12 offset-lg-0">
        <BudgetHeader :month="this.budgetMonth" :totalAllocated="totalAllocated" :totalSpent="totalSpent" :totalAvailable="totalAvailable" :money="moneyToAllocate"
        @change-month="changeMonth" />
      </div>
    </div>
    <div class="placeholder top">
      <BudgetHeader :month="this.budgetMonth" :totalAllocated="totalAllocated" :totalSpent="totalSpent" :totalAvailable="totalAvailable" :money="moneyToAllocate" />
    </div>
    <div class="content">
      <btn v-if="!edit" v-on:click="editFunction" class="actionButton">{{ $t("EDIT") }}</btn>
      <btn v-else v-on:click="saveChange" class="actionButton">{{ $t("SAVE_CHANGE") }}</btn>
      <btn v-if="edit" v-on:click="this.createMasterCategory()" class="buttonGradation row">
        <span class="illustration fas fa-plus col-1"/>
        <span class="illustrationLabel col-11">{{ $t("ADD_MASTER_CATEGORY") }}</span>
      </btn>
      <div id="budgetTables">
        <template class="budgetTable table" v-for="masterCategory of this.$store.state.masterCategories" :key="masterCategory" >
          <MasterCategoryCmpt @update-allocation="updateAllocation" @empty-category="emptyCategory" @empty-master-category="emptyMasterCategory" :masterCategory="masterCategory" :categoryDataList="this.categoryDataList" :edit="edit"/>
        </template>
        <div class="budget-tools">
          <div v-on:click="this.archiveVisible = !this.archiveVisible" class="actionLabelIcon">
            <span v-if="this.archiveVisible" type="button" > > {{ $t("HIDE_ARCHIVE") }}</span>
            <span v-else type="button"> > {{ $t("SHOW_ARCHIVE") }}</span>
          </div>
        </div>
        <div v-if="this.archiveVisible" id="archive_section" >
          <div class="title">{{ $t("ARCHIVE") }}</div>
          <template v-for="masterCategory in this.$store.state.masterCategories" :key="masterCategory" >
            <MasterCategoryCmpt @update-allocation="updateAllocation" @empty-category="emptyCategory" :masterCategory="masterCategory" :categoryDataList="this.categoryDataList" :edit="edit" :archived="true" />
          </template>
        </div>
      </div>
      <div class="placeholder bottom"></div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import BudgetDataService from '@/services/BudgetDataService'
import AllocationService from '@/services/AllocationService'
import { Account, Budget, CategoryData, CategoryDataList, newMasterCategoryName } from '@/model/model'
import MasterCategoryCmpt from './MasterCategoryCmpt.vue'
import Time from '@/utils/Time'
import Utils from '@/utils/Utils'
import MasterCategoryService from '@/services/MasterCategoryService'
import StoreHandler from '@/store/StoreHandler'
import BudgetHeader from '@/components/headers/BudgetHeader.vue'

interface BudgetCmptData {
    categoryDataList: CategoryDataList;
    formerAllocations: {
        [categoryId: string]: number;
    };
    budgetMonth: number;
    amountInBudget: number;
    archiveVisible: boolean;
    edit: boolean;
}

export default defineComponent({
  name: 'BudgetCmpt',
  components: {
    MasterCategoryCmpt,
    BudgetHeader
  },
  props: {
    month: {
      type: Number,
      required: true
    }
  },
  created: async function () {
    this.getBudgetData()
  },
  watch: {
    accounts: async function () {
      this.getBudgetData()
    },
    budget: async function () {
      this.getBudgetData()
    },
    budgetMonth: async function () {
      this.getBudgetData()
    }
  },
  data (): BudgetCmptData {
    return {
      categoryDataList: {},
      /* use former allocation to compute the new "available" value
        newAvailable = available + newAllocation - formerAllocation
        without asking the back-end to compute */
      formerAllocations: {},
      budgetMonth: this.$props.month,
      amountInBudget: 0,
      archiveVisible: false,
      edit: false
    }
  },
  computed: {
    budget (): Budget | null {
      return this.$store.state.budget
    },
    accounts (): Account[] | null {
      return this.$store.state.accounts
    },
    totalBudgetData () {
      const totalBudgetData = new CategoryData()
      for (const categoryId in this.categoryDataList) {
        totalBudgetData.allocated += this.categoryDataList[categoryId].allocated
        totalBudgetData.spent += this.categoryDataList[categoryId].spent
        totalBudgetData.available += this.categoryDataList[categoryId].available
      }
      return totalBudgetData
    },
    isThisYear (): boolean {
      return Time.monthIsThisYear(this.budgetMonth)
    },
    toBeBudgeted (): number {
      let toBeBudgeted = this.amountInBudget
      for (const categoryId in this.categoryDataList) {
        toBeBudgeted -= this.categoryDataList[categoryId].available
      }
      return toBeBudgeted
    },
    toBeBudgetedClass (): string {
      if (this.toBeBudgeted > 0) {
        return 'positive'
      } else if (this.toBeBudgeted < 0) {
        return 'negative'
      } else {
        return 'null'
      }
    },
    totalAllocated (): number {
      return this.getEurosAmount(this.totalBudgetData.allocated)
    },
    totalSpent (): number {
      return this.getEurosAmount(this.totalBudgetData.spent)
    },
    totalAvailable (): number {
      return this.getEurosAmount(this.totalBudgetData.available)
    },
    moneyToAllocate (): number {
      return this.getEurosAmount(this.toBeBudgeted)
    }
  },
  methods: {
    async getBudgetData () {
      if (this.budget) {
        BudgetDataService.getBudgetDataForMonth(this.budget, this.budgetMonth).then(
          (categoryDataList) => {
            this.categoryDataList = categoryDataList
            this.initFormerAllocation()
          }
        )
        BudgetDataService.getBudgetAmount(this.budget, this.budgetMonth).then(
          (amount) => {
            this.amountInBudget = amount
          }
        )
      }
    },
    initFormerAllocation () {
      let category
      for (const categoryId in this.categoryDataList) {
        category = this.categoryDataList[categoryId]
        this.formerAllocations[categoryId] = category.allocated
      }
    },
    updateAllocation (categoryId: string, newAllocation: number) {
      if (!this.categoryDataList[categoryId]) {
        this.categoryDataList[categoryId] = new CategoryData()
      }
      this.categoryDataList[categoryId].available +=
        newAllocation - (this.formerAllocations[categoryId] || 0)
      this.categoryDataList[categoryId].allocated = newAllocation
      this.formerAllocations[categoryId] = newAllocation
      AllocationService.updateAllocation(this.budgetMonth, categoryId, newAllocation)
    },
    getMonthAsDate (monthAsInt: number): Date {
      return Time.getMonthAsDate(monthAsInt)
    },
    changeMonth (message: string) {
      if (message === 'next') {
        this.budgetMonth = Time.getNextMonth(this.budgetMonth)
      } else {
        this.budgetMonth = Time.getLastMonth(this.budgetMonth)
      }
    },
    getEurosAmount (amount: number): number {
      return Utils.getEurosAmount(amount)
    },
    createMasterCategory () {
      if (this.budget) {
        MasterCategoryService.createMasterCategory(newMasterCategoryName, this.budget).then(
          () => {
            StoreHandler.updateMasterCategories(this.$store)
            StoreHandler.updateCategories(this.$store)
          }
        )
      }
    },
    emptyCategory (categoryId: string) {
      if (this.categoryDataList[categoryId].available !== 0) {
        this.categoryDataList[categoryId].allocated -= this.categoryDataList[categoryId].available
        this.formerAllocations[categoryId] -= this.categoryDataList[categoryId].available
        this.categoryDataList[categoryId].available = 0
        AllocationService.updateAllocation(this.budgetMonth, categoryId, this.formerAllocations[categoryId])
      }
    },
    emptyMasterCategory (masterCategoryId: string) {
      const masterCategory = StoreHandler.getMasterCategoryById(this.$store, masterCategoryId)
      if (masterCategory) {
        const categories = StoreHandler.getCategoriesByMasterCategory(this.$store, masterCategory, false)
        categories.forEach(category => {
          this.emptyCategory(category.id)
        })
      }
    },
    editFunction () {
      this.edit = !this.edit
      console.log(this.edit)
    },
    saveChange () {
      this.editFunction()
    }
  }
})
</script>
