<template>
  <table class="budgetTable table" v-if="categories.length > 0">
    <MasterCategoryForm v-if="edit" :masterCategory="masterCategory" :archived="archived" @create-category="createCategory" @empty-master-category="emptyMasterCategory"/>
    <thead v-else class="masterCategory" :style=style>
        <tr>
          <th class="col-6 name">
            <span>{{ masterCategory?.name }}</span>
            <span class="action">
            </span>
          </th>
          <th class="col-2 amountCol">{{ centsToEurosDisplay(masterCategoryData.allocated)}}</th>
          <th class="col-2 amountCol spent">{{ centsToEurosDisplay(masterCategoryData.spent) }}</th>
          <th class="col-2 amountCol available"><span :class="masterCategoryData.available < 0 ? 'negative' : ''">
          {{ centsToEurosDisplay(masterCategoryData.available) }}
        </span></th>
        </tr>
      </thead>
    <tbody >
      <template v-for="category of categories" :key="category">
        <CategoryForm class="categoryBudget" v-if="edit" :category="category" @empty-envelope="emptyEnvelope"/>
        <tr v-else class="categoryBudget">
          <td class="col-6 name">
            <div>
              <span>{{ category.name }}</span>
            </div>
          </td>
          <td class="col-2">
              <span v-if="archived">{{ centsToEurosDisplay(categoryDataList[category.id]?.allocated) ?? "" }}</span>
              <div v-else class="form-group numberInput">
              <input  type="textInput" class="form-control"
                v-bind:value="centsToEurosDisplay(categoryDataList[category.id]?.allocated ?? 0)"
                v-on:change="updateAllocationOnChange(category.id, computeStringToCents($event.target.value).toString())"
              >
              </div>
            </td>
            <td class="col-2 spent">
                {{ centsToEurosDisplay(categoryDataList[category.id]?.spent ?? "") }}
            </td>
            <td class="col-2 available">
              <span v-if="categoryDataList[category.id] && categoryDataList[category.id].available != 0" :class="categoryDataList[category.id]?.available < 0 ? 'negative' : ''">
              {{ centsToEurosDisplay(categoryDataList[category.id]?.available) }}
            </span>
          </td>
        </tr>
      </template>
      <tr v-if="!archived && edit" class="categoryBudget actionLabelIcon addCategoryRow">
        <span class="illustration btn fas fa-plus"/>
        <div v-on:click="createCategory" class="text">{{ $t("ADD_CATEGORY") }}</div>
      </tr>

    </tbody>
  </table>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import type { MasterCategory, CategoryDataList, Category } from '@/model/model'
import { CategoryData, newCategoryName } from '@/model/model'
import Utils from '@/utils/Utils'
import Calcul from '@/utils/Calcul'
import CategoryService from '@/services/CategoryService'
import CategoryForm from '@/components/forms/CategoryForm.vue'
import MasterCategoryForm from '@/components/forms/MasterCategoryForm.vue'
import { Color } from '@/utils/Color'
import { useBudgetStore } from '@/stores/budgetStore'

export default defineComponent({
  name: 'MasterCategoryCmpt',
  components: {
    CategoryForm,
    MasterCategoryForm
  },
  emits: ['updateAllocation', 'emptyCategory', 'emptyMasterCategory'],
  props: {
    masterCategory: {
      type: Object as () => MasterCategory,
      required: true
    },
    categoryDataList: {
      type: Object as () => CategoryDataList,
      required: true
    },
    archived: {
      type: Boolean as () => boolean,
      required: false,
      default: false
    },
    edit: {
      type: Boolean as () => boolean,
      required: false,
      default: false
    }
  },
  computed: {
    categories (): Category[] {
      return useBudgetStore().getCategoriesByMasterCategory(this.masterCategory, this.archived)
    },
    masterCategoryData () {
      const masterCategoryData = new CategoryData()
      for (const category of this.categories) {
        masterCategoryData.allocated += this.categoryDataList[category.id]?.allocated ?? 0
        masterCategoryData.spent += this.categoryDataList[category.id]?.spent ?? 0
        masterCategoryData.available += this.categoryDataList[category.id]?.available ?? 0
      }
      return masterCategoryData
    },
    style (): string {
      return this.masterCategory.color !== null ? 'background : linear-gradient(to right, ' + Color.shadeColor(this.masterCategory.color, -50) + ',' + this.masterCategory.color + ')' : ''
    }
  },
  methods: {
    updateAllocationOnChange (categoryId: string, value: string) {
      this.$emit('updateAllocation', categoryId, value)
    },
    createCategory () {
      CategoryService.createCategory(newCategoryName, this.masterCategory).then(
        () => {
          useBudgetStore().updateCategories()
        }
      )
    },
    emptyEnvelope (categoryId: string) {
      this.$emit('emptyCategory', categoryId)
    },
    emptyMasterCategory (masterCategoryId: string) {
      this.$emit('emptyMasterCategory', masterCategoryId)
    },
    centsToEurosDisplay (number: number): string {
      return Utils.centsToEurosDisplay(number)
    },
    computeStringToCents (amount: string): number {
      return Calcul.computeStringToCents(amount)
    }
  }
})
</script>
