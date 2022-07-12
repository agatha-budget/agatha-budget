<template>
  <tr v-if="!this.category.archived">
    <th class="col-6">
      <div class="form-group">
          <input type="textInput" class="form-control" v-model="name">
      </div>
    </th>
    <th class="col-2">
      <button class="illustration btn fas fa-check" v-on:click="updateCategory"/>
    </th>
      <th class="col-2">
      <button class="illustration btn fas fa-times" v-on:click="this.$emit('loosesFocus')"/>
    </th>
    <th class="col-2">
      <button class="illustration btn fas fa-archive" v-on:click="archiveCategory"/>
    </th>
  </tr>
  <tr v-else>
    <th class="col-6">
      <span class="name">{{ this.name }}</span>
    </th>
    <th class="col-2">
      <button class="illustration btn fas fa-level-up-alt" v-on:click="unarchiveCategory"/>
      </th>
    <th class="col-2">
      <button class="illustration btn fas fa-times" v-on:click="this.$emit('loosesFocus')"/>
    </th>
    <th class="col-2">
    </th>
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
