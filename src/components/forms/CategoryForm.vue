<template>
  <tr v-if="!this.category.archived">
    <th class="col-6">
      <div class="form-group">
          <input type="textInput" class="form-control" v-model="name">
      </div>
    </th>
    <th class="col-3">
      <button class="illustration btn fas fa-exchange-alt" v-on:click="changeMasterCategory"/>
    </th>
    <th class="col-3">
      <button class="illustration btn fas fa-archive" v-on:click="archiveCategory"/>
    </th>
  </tr>
  <tr v-else>
    <td class="col-6">
      <span class="name">{{ this.name }}</span>
    </td>
    <td class="col-6">
      <button class="illustration btn fas fa-level-up-alt" v-on:click="unarchiveCategory"/>
    </td>
  </tr>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import CategoryService from '@/services/CategoryService'
import StoreHandler from '@/store/StoreHandler'
import { Category } from '@/model/model'

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
  emits: ['loosesFocus', 'emptyEnvelope'],
  methods: {
    updateCategory () {
      CategoryService.updateCategory(this.category.id, this.name).then(
        () => {
          StoreHandler.updateCategories(this.$store)
          this.$emit('loosesFocus')
        }
      )
    },
    archiveCategory () {
      this.$emit('emptyEnvelope', this.category.id)
      CategoryService.archiveCategory(this.category.id).then(
        () => {
          StoreHandler.updateCategories(this.$store)
          this.$emit('loosesFocus')
        }
      )
    },
    unarchiveCategory () {
      CategoryService.unarchiveCategory(this.category.id).then(
        () => {
          StoreHandler.updateCategories(this.$store)
          this.$emit('loosesFocus')
        }
      )
    }
  }
})
</script>
