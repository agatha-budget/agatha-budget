<template>
  <template v-if="this.categories.length > 0">
    <tr class="masterCategory">
      <th class="col-6 name">
        <div>
          <MasterCategoryForm v-if="focusOn === masterCategory.id" :masterCategory="masterCategory" :archived="archived" @looses-focus="loosesFocus" @create-category="createCategory"/>
          <a v-else v-on:click="this.putFocusOn(masterCategory.id)"><button class="btn fas fa-pen"/>{{ masterCategory?.name }}</a>
        </div>
      </th>
      <th class="col-2 allocated">{{ getRoundedAmount(masterCategoryData.allocated)}}</th>
      <th class="col-2 spent">{{ getRoundedAmount(masterCategoryData.spent) }}</th>
      <th class="col-2 available">
        <span :class="masterCategoryData.available < 0 ? 'negative' : 'positive'">
          {{ getRoundedAmount(masterCategoryData.available) }}
        </span>
      </th>
    </tr>
    <tbody>
    <tr class="category" v-for="category of this.categories" :key="category">
      <td class="name">
        <div>
          <CategoryForm v-if="focusOn === category.id" :category="category" @looses-focus="loosesFocus"/>
          <a v-else v-on:click="this.putFocusOn(category.id)"><button class="btn fas fa-pen"/>{{ category.name}} </a>
        </div>
      </td>
      <td class="allocated">
        <span v-if="archived">{{ this.categoryDataList[category.id]?.allocated ?? "" }}</span>
        <input v-else type="number" class="allocationInput"
        :value="this.categoryDataList[category.id]?.allocated ?? 0"
        v-on:change="updateAllocationOnChange(category.id, $event.target.value)"
        >
        </td>
      <td class="spent">
          {{ getRoundedAmount(this.categoryDataList[category.id]?.spent ?? "") }}
      </td>
      <td class="available">
        <span v-if="this.categoryDataList[category.id] && this.categoryDataList[category.id].available != 0" :class="this.categoryDataList[category.id]?.available < 0 ? 'negative' : 'positive'">
          {{ getRoundedAmount(this.categoryDataList[category.id]?.available) }}
        </span>
      </td>
    </tr>
    </tbody>
  </template>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { MasterCategory, CategoryDataList, CategoryData, Category } from '@/model/model'
import Utils from '@/utils/Utils'
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
  emits: ['updateAllocation'],
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
      this.$emit('updateAllocation', categoryId, +value)
    },
    getRoundedAmount (amount: number): number {
      return Utils.getRoundedAmount(amount)
    },
    createCategory () {
      CategoryService.createCategory('New Category', this.masterCategory).then(
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
    }
  }
})
</script>
