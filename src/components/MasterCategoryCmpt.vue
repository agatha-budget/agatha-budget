<template>
  <template v-if="this.categories.length > 0">
    <thead class="container header masterCategory">
        <tr>
          <th class="col-6">
            <MasterCategoryForm v-if="focusOn === masterCategory.id" :masterCategory="masterCategory" :archived="archived" @looses-focus="loosesFocus" @create-category="createCategory"/>
            <div v-else class="name">
                <span v-on:click="this.putFocusOn(masterCategory.id)">{{ masterCategory?.name }}</span>
                <span class="action">
                  <button class="illustration btn fas fa-pen" v-on:click="this.putFocusOn(masterCategory.id)"/>
                  <button class="illustration btn fas fa-plus" v-on:click="createCategory"/>
                </span>
            </div>
          </th>
          <th class="col-2">{{ addSpacesInThousand(getEurosAmount(masterCategoryData.allocated))}}</th>
          <th class="col-2 spent">{{ addSpacesInThousand(getEurosAmount(masterCategoryData.spent)) }}</th>
          <th class="col-2"><span :class="masterCategoryData.available < 0 ? 'negative' : ''">
          {{ addSpacesInThousand(getEurosAmount(masterCategoryData.available)) }}
        </span></th>
        </tr>
      </thead>
    <tbody class="categoryBudget">
      <tr v-for="category of this.categories" :key="category">
        <td class="col-6">
          <div>
            <CategoryForm v-if="focusOn === category.id" :category="category" @looses-focus="loosesFocus" @empty-envelope="emptyEnvelope"/>
            <span class="name" v-else v-on:click="this.putFocusOn(category.id)">{{ category.name}} <button class="action illustration btn fas fa-pen"/></span>
          </div>
        </td>
        <td class="col-2">
          <span v-if="archived">{{ getEurosAmount(this.categoryDataList[category.id]?.allocated ?? "") }}</span>
          <div v-else class="form-group numberInput">
          <input  type="text" class="form-control"
            v-bind:value="this.getEurosAmount(this.categoryDataList[category.id]?.allocated ?? 0)"
            v-on:change="updateAllocationOnChange(category.id, this.entireCalcul($event.target.value))"
          >
          </div>
        </td>
        <td class="col-2 spent">
            {{ addSpacesInThousand(getEurosAmount(this.categoryDataList[category.id]?.spent ?? "")) }}
        </td>
        <td class="col-2">
          <span v-if="this.categoryDataList[category.id] && this.categoryDataList[category.id].available != 0" :class="this.categoryDataList[category.id]?.available < 0 ? 'negative' : ''">
            {{ addSpacesInThousand(getEurosAmount(this.categoryDataList[category.id]?.available)) }}
          </span>
        </td>
      </tr>
    </tbody>
  </template>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { MasterCategory, CategoryDataList, CategoryData, Category, newCategoryName } from '@/model/model'
import Utils from '@/utils/Utils'
import Calcul from '@/utils/Calcul'
import CategoryService from '@/services/CategoryService'
import StoreHandler from '@/store/StoreHandler'
import CategoryForm from '@/components/forms/CategoryForm.vue'
import MasterCategoryForm from '@/components/forms/MasterCategoryForm.vue'

export default defineComponent({
  name: 'MasterCategoryCmpt',
  components: {
    CategoryForm,
    MasterCategoryForm
  },
  emits: ['updateAllocation', 'emptyCategory'],
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
    }
  },
  data () {
    return {
      focusOn: ''
    }
  },
  computed: {
    categories (): Category[] {
      return StoreHandler.getCategoriesByMasterCategory(this.$store, this.masterCategory, this.archived)
    },
    masterCategoryData () {
      const masterCategoryData = new CategoryData()
      for (const category of this.categories) {
        masterCategoryData.allocated += this.categoryDataList[category.id]?.allocated ?? 0
        masterCategoryData.spent += this.categoryDataList[category.id]?.spent ?? 0
        masterCategoryData.available += this.categoryDataList[category.id]?.available ?? 0
      }
      return masterCategoryData
    }
  },
  methods: {
    updateAllocationOnChange (categoryId: string, value: string) {
      this.$emit('updateAllocation', categoryId, Utils.getCentsAmount(+value))
    },
    getEurosAmount (amount: number): number {
      return Utils.getEurosAmount(amount)
    },
    createCategory () {
      CategoryService.createCategory(newCategoryName, this.masterCategory).then(
        () => {
          StoreHandler.updateCategories(this.$store)
        }
      )
    },
    putFocusOn (categoryId: string) {
      this.focusOn = categoryId
    },
    loosesFocus () {
      this.focusOn = ''
    },
    emptyEnvelope (categoryId: string) {
      this.$emit('emptyCategory', categoryId)
    },
    addSpacesInThousand (number: number): string {
      return Utils.addSpacesInThousand(number)
    },
    entireCalcul (amount: string): number {
      return Calcul.entireCalcul(amount)
    }
  }
})
</script>
