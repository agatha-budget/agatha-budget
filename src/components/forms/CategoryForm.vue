<template>
  <tr v-if="!category.archived">
    <th class="col-6">
      <div class="form-group">
          <input type="textInput" class="form-control" v-model="name" v-on:change="changeName">
      </div>
    </th>
    <th class="col-3 icon">
      <button class="illustration btn fas fa-archive" v-on:click="archiveCategory"/>
    </th>
    <th class="col-3 text" v-on:click="archiveCategory">
      {{ $t('TO_ARCHIVE') }}
    </th>
  </tr>
  <tr v-else>
    <td class="col-6">
      <span class="name">{{ name }}</span>
    </td>
    <td class="col-6">
      <button class="illustration btn fas fa-level-up-alt" v-on:click="unarchiveCategory"/>
    </td>
  </tr>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import CategoryService from '@/services/CategoryService'
import type { Category } from '@/model/model'
import { useBudgetStore } from '@/stores/budgetStore'

interface CategoryFormData {
  name: string;
}

export default defineComponent({
  name: 'CategoryForm',
  data (): CategoryFormData {
    return {
      name: this.category.name
    }
  },
  props: {
    category: {
      type: Object as () => Category,
      required: true
    }
  },
  emits: ['emptyEnvelope'],
  methods: {
    updateCategory () {
      CategoryService.updateCategory(this.category.id, this.name).then(
        () => {
          useBudgetStore().updateCategories()
        }
      )
    },
    archiveCategory () {
      this.$emit('emptyEnvelope', this.category.id)
      CategoryService.archiveCategory(this.category.id).then(
        () => {
          useBudgetStore().updateCategories()
        }
      )
    },
    unarchiveCategory () {
      CategoryService.unarchiveCategory(this.category.id).then(
        () => {
          useBudgetStore().updateCategories()
        }
      )
    },
    changeName () {
      CategoryService.updateCategory(this.category.id, this.name).then(
        () => {
          useBudgetStore().updateCategories()
        }
      )
    }
  }
})
</script>
