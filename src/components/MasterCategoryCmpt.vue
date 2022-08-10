<template>
  <table class="budgetTable table" v-if="this.categories.length > 0">
    <MasterCategoryForm v-if="edit" :masterCategory="masterCategory" :archived="archived" @create-category="createCategory" @empty-master-category="emptyMasterCategory"/>
    <thead v-else class="masterCategory" :style="{'background': color}">
        <tr>
          <th class="col-6 name">
            <span>{{ masterCategory?.name }}</span>
            <span class="action">
            </span>
          </th>
          <th class="col-2">{{ addSpacesInThousand(getEurosAmount(masterCategoryData.allocated))}}</th>
          <th class="col-2 spent">{{ addSpacesInThousand(getEurosAmount(masterCategoryData.spent)) }}</th>
          <th class="col-2"><span :class="masterCategoryData.available < 0 ? 'negative' : ''">
          {{ addSpacesInThousand(getEurosAmount(masterCategoryData.available)) }}
        </span></th>
        </tr>
      </thead>
    <tbody >
      <template v-for="category of this.categories" :key="category">
        <CategoryForm class="categoryBudget" v-if="edit" :category="category" @empty-envelope="emptyEnvelope"/>
        <tr v-else class="categoryBudget">
          <td class="col-6 name">
            <div>
              <span>{{ category.name}}</span>
            </div>
          </td>
          <td class="col-2">
              <span v-if="archived">{{ getEurosAmount(this.categoryDataList[category.id]?.allocated ?? "") }}</span>
              <div v-else class="form-group numberInput">
              <input  type="textInput" class="form-control"
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
      </template>

      <div v-if="!archived">
        <tr>
          <div class="actionLabelIcon">
            <span class="illustration btn fas fa-plus"/>
            <div v-on:click="createCategory" class="text">ajouter une enveloppe</div>
          </div>
        </tr>
      </div>

    </tbody>
  </table>
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
import { navigationColor } from '@/model/colorList'

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
  data () {
    return {
      color: this.masterCategory.color !== 'null' ? this.masterCategory.color : navigationColor
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
    emptyEnvelope (categoryId: string) {
      this.$emit('emptyCategory', categoryId)
    },
    emptyMasterCategory (masterCategoryId: string) {
      this.$emit('emptyMasterCategory', masterCategoryId)
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
