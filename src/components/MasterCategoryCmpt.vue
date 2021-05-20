<template>
  <tr class="masterCategory">
    <th class="col-6 name"><div>{{ masterCategory?.name }}</div></th>
    <th class="col-2 allocated">{{ masterCategoryData.allocated}}</th>
    <th class="col-2 spent">{{ masterCategoryData.spent }}</th>
    <th class="col-2 available">{{ masterCategoryData.available }}</th>
  </tr>
  <tbody>
  <tr class="category" v-for="categoryId in this.categoriesId" :key="categoryId">
    <td class="name"><div>{{ this.$store.state.categories[categoryId]?.name}}</div></td>
    <td class="allocated">{{ this.categoryDataList[categoryId]?.allocated }}</td>
    <td class="spent">{{ this.categoryDataList[categoryId]?.spent }}</td>
    <td class="available">{{ this.categoryDataList[categoryId]?.available }}</td>
  </tr>
  </tbody>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { MasterCategory, CategoryDataList } from '@/model/model'

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
      const masterCategoryData = {
        allocated: 0,
        spent: 0,
        available: 0
      }
      console.log(this.categoryDataList)
      for (const categoryId in this.categoryDataList) {
        console.log(categoryId)
        if (this.categoriesId.includes(categoryId)) {
          console.log('in')
          masterCategoryData.allocated += this.categoryDataList[categoryId].allocated
          masterCategoryData.spent += this.categoryDataList[categoryId].spent
          masterCategoryData.available += this.categoryDataList[categoryId].available
        }
      }
      return masterCategoryData
    }
  },
  methods: {}
})
</script>
