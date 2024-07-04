<template>
  <div id="budgetCmpt">
    <div class="fixedHeader">
      <BudgetHeader :month="budgetMonth" :totalAllocated="totalAllocated" :totalSpent="totalSpent" :totalAvailable="totalAvailable" :money="moneyToAllocate"
        @change-month="changeMonth" />
    </div>
    <div class="headedContent">
      <button v-if="!edit" v-on:click="editFunction" class="actionButton edition">{{ $t("CUSTOMIZE_CATEGORY") }}</button>
      <button v-else v-on:click="saveChange" class="actionButton edition">{{ $t("SAVE_CHANGE") }}</button>
      <button v-if="edit" v-on:click="createMasterCategory()" class="buttonGradation row">
        <span class="illustration fas fa-plus col-1"/>
        <span class="illustrationLabel col-11">{{ $t("ADD_MASTER_CATEGORY") }}</span>
      </button>
      <div id="budgetTables">
        <template v-for="masterCategory of masterCategories" :key="masterCategory" >
          <MasterCategoryCmpt class="budgetTable table" @update-allocation="updateAllocation" @empty-category="emptyCategory" @empty-master-category="emptyMasterCategory" :masterCategory="masterCategory" :categoryDataList="categoryDataList" :edit="edit"/>
        </template>
        <div class="budget-tools">
          <div v-on:click="archiveVisible = !archiveVisible" class="actionLabelIcon">
            <span v-if="archiveVisible" type="button" > > {{ $t("HIDE_ARCHIVE") }}</span>
            <span v-else type="button"> > {{ $t("SHOW_ARCHIVE") }}</span>
          </div>
        </div>
        <div v-if="archiveVisible" id="archive_section" >
          <div class="title">{{ $t("ARCHIVE") }}</div>
          <template v-for="masterCategory in masterCategories" :key="masterCategory" >
            <MasterCategoryCmpt @update-allocation="updateAllocation" @empty-category="emptyCategory" :masterCategory="masterCategory" :categoryDataList="categoryDataList" :edit="edit" :archived="true" />
          </template>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import BudgetHeader from '@/components/headers/BudgetHeader.vue'
import type { Account, Budget, CategoryDataList, MasterCategory } from '@/model/model'
import { CategoryData, newMasterCategoryName } from '@/model/model'
import AllocationService from '@/services/AllocationService'
import BudgetDataService from '@/services/BudgetDataService'
import MasterCategoryService from '@/services/MasterCategoryService'
import { useBudgetStore } from '@/stores/budgetStore'
import Time from '@/utils/Time'
import { defineComponent } from 'vue'
import MasterCategoryCmpt from './MasterCategoryCmpt.vue'

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
      return useBudgetStore().budget
    },
    accounts (): Account[] | null {
      return useBudgetStore().accounts
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
      return this.totalBudgetData.allocated
    },
    totalSpent (): number {
      return this.totalBudgetData.spent
    },
    totalAvailable (): number {
      return this.totalBudgetData.available
    },
    moneyToAllocate (): number {
      return this.toBeBudgeted
    },
    masterCategories (): MasterCategory[] {
      return useBudgetStore().getOrderedMasterCategory()
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
    createMasterCategory () {
      if (this.budget) {
        MasterCategoryService.createMasterCategory(newMasterCategoryName, this.budget).then(
          () => {
            useBudgetStore().updateMasterCategories()
            useBudgetStore().updateCategories()
          }
        )
      }
    },
    emptyCategory (categoryId: string) {
      if (this.categoryDataList[categoryId] && this.categoryDataList[categoryId].available !== 0) {
        this.categoryDataList[categoryId].allocated -= this.categoryDataList[categoryId].available
        this.formerAllocations[categoryId] -= this.categoryDataList[categoryId].available
        this.categoryDataList[categoryId].available = 0
        AllocationService.updateAllocation(this.budgetMonth, categoryId, this.formerAllocations[categoryId])
      }
    },
    emptyMasterCategory (masterCategoryId: string) {
      const masterCategory = useBudgetStore().getMasterCategoryById(masterCategoryId)
      if (masterCategory) {
        const categories = useBudgetStore().getCategoriesByMasterCategory(masterCategory, false)
        categories.forEach(category => {
          this.emptyCategory(category.id)
        })
      }
    },
    editFunction () {
      this.edit = !this.edit
    },
    saveChange () {
      this.editFunction()
    }
  }
})
</script>
