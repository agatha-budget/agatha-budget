<template>
  <tr class="masterCategory">
    <th class="col-6 name"><div>{{ masterCategory?.name }}</div></th>
    <th class="col-2 allocated">{{ masterCategoryData?.allocated }}</th>
    <th class="col-2 spent">{{ 0 }}</th>
    <th class="col-2 available">{{ 0 }}</th>
  </tr>
  <tbody>
  <tr class="category" v-for="categoryId in this.categoriesId" :key="categoryId">
    <td class="name"><div>{{ this.$store.state.categories[categoryId]?.name}}</div></td>
    <td class="allocated">{{ 0 }}</td>
    <td class="spent">{{ 0 }}</td>
    <td class="available">{{ 0 }}</td>
  </tr>
  </tbody>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { MasterCategory, CategoryData } from '@/model/model'

export default defineComponent({
  name: 'MasterCategoryCmpt',
  emits: ['updateAllocation'],
  props: {
    masterCategoryId: {
      type: String,
      required: true
    },
    masterCategoryData: {
      type: Object as () => CategoryData,
      required: true
    }
  },
  computed: {
    masterCategory (): MasterCategory {
      console.log()
      return this.$store.state.masterCategories[this.masterCategoryId]
    },
    categoriesId (): string[] {
      return this.$store.state.categoriesIdByMasterCategoriesId[this.masterCategoryId]
    }
  },
  methods: {}
})
</script>
