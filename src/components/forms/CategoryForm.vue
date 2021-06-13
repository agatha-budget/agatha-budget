<template>
  <div id="operationForm" class="operation">
    <span class="memo"><input id="newName" class="form-control" v-model="name"></span>
    <span class="validation">
      <button class="btn fas fa-check" v-on:click="updateCategory"/>
      <button class="btn fas fa-times" v-on:click="this.$emit('loosesFocus')"/>
      <button class="btn fas fa-archive" v-on:click="archiveCategory"/>
    </span>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import CategoryService from '@/services/CategoryService'
import StoreHandler from '@/store/StoreHandler'

interface CategoryFormData {
  name: string;
}

export default defineComponent({
  name: 'CategoryForm',
  data (): CategoryFormData {
    return {
      name: this.$store.state.categories[this.categoryId].name
    }
  },
  props: {
    categoryId: {
      type: String,
      required: true
    }
  },
  emits: ['loosesFocus'],
  methods: {
    updateCategory () {
      CategoryService.updateCategory(this.categoryId, this.name).then(
        () => {
          StoreHandler.updateCategories(this.$store)
          this.$emit('loosesFocus')
        }
      )
    },
    archiveCategory () {
      CategoryService.archiveCategory(this.categoryId).then(
        () => {
          StoreHandler.updateCategories(this.$store)
          this.$emit('loosesFocus')
        }
      )
    }
  }
})
</script>
