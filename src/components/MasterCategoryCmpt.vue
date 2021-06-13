<template>
  <tr class="masterCategory">
    <th class="col-6 name">
      <div>
        <MasterCategoryForm v-if="focusOn === masterCategoryId" :masterCategoryId="masterCategoryId" @looses-focus="loosesFocus" @create-category="createCategory"/>
        <a v-else v-on:click="this.putFocusOn(masterCategoryId)">{{ masterCategory?.name }}</a>
      </div>
    </th>
    <th class="col-2 allocated">{{ getRoundedAmount(masterCategoryData.allocated)}}</th>
    <th class="col-2 spent">{{ getRoundedAmount(masterCategoryData.spent) }}</th>
    <th class="col-2 available">{{ getRoundedAmount(masterCategoryData.available) }}</th>
  </tr>
  <tbody>
  <tr class="category" v-for="categoryId in this.categoriesId" :key="categoryId">
    <td class="name">
      <div>
        <CategoryForm v-if="focusOn === categoryId" :categoryId="categoryId" @looses-focus="loosesFocus"/>
        <a v-else v-on:click="this.putFocusOn(categoryId)">{{ this.$store.state.categories[categoryId]?.name}}</a>
      </div>
    </td>
    <td class="allocated">
      <input type="number" class="allocationInput"
      :value="this.categoryDataList[categoryId]?.allocated || 0"
      v-on:change="updateAllocationOnChange(categoryId, $event.target.value)"
      >
      </td>
    <td class="spent">{{ getRoundedAmount(this.categoryDataList[categoryId]?.spent || 0) }}</td>
    <td class="available">{{ getRoundedAmount(this.categoryDataList[categoryId]?.available || 0) }}</td>
  </tr>
  </tbody>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { MasterCategory, CategoryDataList, CategoryData } from '@/model/model'
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
    masterCategoryId: {
      type: String,
      required: true
    },
    categoryDataList: {
      type: Object as () => CategoryDataList,
      required: true
    }
  },
  data () {
    return {
      focusOn: ''
    }
  },
  computed: {
    masterCategory (): MasterCategory {
      return this.$store.state.masterCategories[this.masterCategoryId]
    },
    categoriesId (): string[] {
      return this.$store.state.categoriesIdByMasterCategoriesId[this.masterCategoryId]
    },
    masterCategoryData () {
      const masterCategoryData = new CategoryData()
      for (const categoryId in this.categoryDataList) {
        if (this.categoriesId.includes(categoryId)) {
          masterCategoryData.allocated += this.categoryDataList[categoryId].allocated
          masterCategoryData.spent += this.categoryDataList[categoryId].spent
          masterCategoryData.available += this.categoryDataList[categoryId].available
        }
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
    },
    deleteCategory () {
      console.log('rfez')
    }
  }
})
</script>
