<template>
  <div v-if="this.category.archived" id="operationForm" class="operation">
    <span class="name">{{ this.name }}</span>
    <span class="validation">
      <button class="btn fas fa-level-up-alt" v-on:click="unarchiveCategory"/>
      <button class="btn fas fa-times" v-on:click="this.$emit('loosesFocus')"/>
    </span>
  </div>
  <div v-else id="operationForm" class="operation">
    <span class="name"><input id="newName" class="form-control" v-model="name"></span>
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
  emits: ['loosesFocus'],
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
