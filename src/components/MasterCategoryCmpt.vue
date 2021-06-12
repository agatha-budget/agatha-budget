<template>
  <tr class="masterCategory">
    <th class="col-6 name"><div>{{ masterCategory?.name }}</div></th>
    <th class="col-2 allocated">{{ getRoundedAmount(masterCategoryData.allocated)}}</th>
    <th class="col-2 spent">{{ getRoundedAmount(masterCategoryData.spent) }}</th>
    <th class="col-2 available">{{ getRoundedAmount(masterCategoryData.available) }}</th>
  </tr>
  <tbody>
  <tr class="category" v-for="categoryId in this.categoriesId" :key="categoryId">
    <td class="name"><div>{{ this.$store.state.categories[categoryId]?.name}}</div></td>
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

export default defineComponent({
  name: 'MasterCategoryCmpt',
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
    }
  }
})
</script>
