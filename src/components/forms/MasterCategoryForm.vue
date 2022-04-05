<template>
  <div id="operationForm" class="operation">
    <div v-if="this.archived" class="masterCategoryArchived">
      <span class="name">{{ this.name }}</span>
       <span class="validation">
        <button class="btn fas fa-level-up-alt" v-on:click="unarchiveMasterCategory"/>
        <button class="btn fas fa-times" v-on:click="this.$emit('loosesFocus')"/>
      </span>
    </div>
    <div v-else>
      <span class="name"><input id="newName" class="form-control" v-model="name"></span>
      <span class="validation">
        <button class="btn fas fa-check" v-on:click="updateMasterCategory"/>
        <button class="btn fas fa-times" v-on:click="this.$emit('loosesFocus')"/>
        <button class="btn fas fa-archive" v-on:click="archiveMasterCategory"/>
      </span>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import StoreHandler from '@/store/StoreHandler'
import MasterCategoryService from '@/services/MasterCategoryService'
import { MasterCategory } from '@/model/model'

interface CategoryFormData {
  name: string;
}

export default defineComponent({
  name: 'CategoryForm',
  data (): CategoryFormData {
    return {
      name: this.masterCategory.name
    }
  },
  props: {
    masterCategory: {
      type: Object as () => MasterCategory,
      required: true
    },
    archived: {
      type: Object as () => boolean,
      required: false
    }
  },
  emits: ['loosesFocus'],
  methods: {
    updateMasterCategory () {
      MasterCategoryService.renameMasterCategory(this.masterCategory.id, this.name).then(
        () => {
          StoreHandler.updateMasterCategories(this.$store)
          this.$emit('loosesFocus')
        }
      )
    },
    archiveMasterCategory () {
      MasterCategoryService.archiveMasterCategory(this.masterCategory.id).then(
        () => {
          StoreHandler.updateCategories(this.$store)
          this.$emit('loosesFocus')
        }
      )
    },
    unarchiveMasterCategory () {
      MasterCategoryService.unarchiveMasterCategory(this.masterCategory.id).then(
        () => {
          StoreHandler.updateCategories(this.$store)
          this.$emit('loosesFocus')
        }
      )
    }
  }
})
</script>
