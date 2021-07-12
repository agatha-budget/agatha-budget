<template>
  <div>
    <div id="budgetTables">
      <div class="row date">
        <div class="col-2 d-flex justify-content-center" ><button type="button" class="btn fas fa-chevron-left" v-on:click="this.goToLastMonth()"/></div>
        <div class="col-8 date-label" :class="this.toBeBudgetedClass">
          <span class="month">{{ $d(this.getMonthAsDate(budgetMonth), 'monthString') }} <span v-if="!this.isThisYear"> {{ $d(this.getMonthAsDate(budgetMonth), 'year') }}</span></span>
          <span class="toBeBudgeted" v-if="this.toBeBudgeted > 0"> : {{ getEurosAmount(this.toBeBudgeted) }} € {{$t('TO_BE_BUDGETED')}}</span>
          <span class="toBePulledOut" v-else-if="this.toBeBudgeted < 0"> : {{ getEurosAmount(-1*this.toBeBudgeted) }} € {{$t('TO_BE_PULLED_OUT')}}</span>
        </div>
        <div class="col-2 d-flex justify-content-center" ><button type="button" class="btn fas fa-chevron-right" v-on:click="this.goToNextMonth()"/></div>
      </div>
      <table id="totalTable" class="table">
          <tr>
            <th class="col-6 name"></th>
            <th class="col-2 allocated"><div>{{ $t("ALLOCATED") }}</div></th>
            <th class="col-2 spent"><div>{{ $t("SPENT") }}</div></th>
            <th class="col-2 available"><div>{{ $t("AVAILABLE") }}</div></th>
          </tr>
          <tbody>
          <tr>
            <td class="name"><div>{{ $t("TOTAL") }} <button type="button" class="btn fas fa-plus" v-on:click="this.createMasterCategory()"/></div></td>
            <td class="allocated">{{ getEurosAmount(this.totalBudgetData.allocated) }}</td>
            <td class="spent">{{ getEurosAmount(this.totalBudgetData.spent) }}</td>
            <td class="available">{{ getEurosAmount(this.totalBudgetData.available) }}</td>
          </tr>
        </tbody>
      </table>
      <div class="scrollable">
        <table class="budgetTable table"
        v-for="masterCategory of this.$store.state.masterCategories"
        :key="masterCategory"
        >
            <master-category-cmpt
              @update-allocation="updateAllocation"
              :masterCategory="masterCategory"
              :categoryDataList="this.categoryDataList"
            />
        </table>
        <div v-on:click="this.archiveVisible = !this.archiveVisible">
          <button v-if="this.archiveVisible" class="btn fas fa-chevron-down"/>
          <button v-else class="btn fas fa-chevron-right"/>
          <a>{{$t('ARCHIVE')}}</a>
        </div>
        <div v-if="this.archiveVisible" id="archive section" >
          <table class="budgetArchiveTable table"
          v-for="masterCategory in this.$store.state.masterCategories"
          :key="masterCategory"
          >
              <master-category-cmpt
                @update-allocation="updateAllocation"
                :masterCategory="masterCategory"
                :categoryDataList="this.categoryDataList"
                :archived="true"
              />
          </table>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import BudgetDataService from '@/services/BudgetDataService'
import AllocationService from '@/services/AllocationService'
import { Budget, CategoryData, CategoryDataList } from '@/model/model'
import MasterCategoryCmpt from './MasterCategoryCmpt.vue'
import Time from '@/utils/Time'
import Utils from '@/utils/Utils'
import MasterCategoryService from '@/services/MasterCategoryService'
import StoreHandler from '@/store/StoreHandler'

interface BudgetCmptData {
    categoryDataList: CategoryDataList;
    formerAllocations: {
        [categoryId: string]: number;
    };
    budgetMonth: number;
    amountInBudget: number;
    archiveVisible: boolean;
}

export default defineComponent({
  name: 'BudgetCmpt',
  components: {
    MasterCategoryCmpt
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
      archiveVisible: false
    }
  },
  computed: {
    budget (): Budget | null {
      return this.$store.state.budget
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
    goToNextMonth () {
      this.budgetMonth = Time.getNextMonth(this.budgetMonth)
    },
    goToLastMonth () {
      this.budgetMonth = Time.getLastMonth(this.budgetMonth)
    },
    getEurosAmount (amount: number): string {
      return Utils.getEurosAmount(amount)
    },
    createMasterCategory () {
      if (this.budget) {
        MasterCategoryService.createMasterCategory('New Master Category', this.budget).then(
          () => {
            StoreHandler.updateMasterCategories(this.$store)
            StoreHandler.updateCategories(this.$store)
          }
        )
      }
    }
  }
})
</script>
