<template>
  <tr class="masterCategory">
    <th class="col-6 name"><div>{{ masterCategoryData.name }}</div></th>
    <th class="col-2 allocated">{{ masterCategoryData.allocated }}</th>
    <th class="col-2 spent">{{ masterCategoryData.spent }}</th>
    <th class="col-2 available">{{ masterCategoryData.available }}</th>
  </tr>
  <tbody>
  <tr class="category" v-for="category, categoryId in categories" :key="category">
    <td class="name"><div>{{ category.name }}</div></td>
    <td class="allocated">
      <input
        class="form-control"
        v-model.number=category.allocated
        type="number"
        v-on:change="updateAllocation(masterCategoryId, categoryId, category.allocated)"
        :placeholder=category.allocated
      >
    </td>
    <td class="spent">{{ category.spent }}</td>
    <td class="available">{{ category.available }}</td>
  </tr>
  </tbody>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { CategoryData, CategoryDataList } from '@/model/model'

export default defineComponent({
  name: 'BudgetCmpt',
  props: {
    masterCategoryData: {
      type: Object as () => CategoryData
    },
    categories: {
      type: Object as () => CategoryDataList
    }

  },
  computed: {},
  methods: {}
})
</script>
