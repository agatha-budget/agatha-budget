<template>
  <thead v-if="!this.archived" class="masterCategory edit">
    <tr>
      <th class="col-6">
        <div class="darkTextInput form-group">
            <input type="textInput" class="form-control" v-model="name">
        </div>
      </th>
      <th class="col-3">
        <button class="illustration btn fas fa-palette" v-on:click="chooseColor"/>
      </th>
      <th class="col-3">
        <button class="illustration btn fas fa-archive" v-on:click="archiveMasterCategory"/>
      </th>
    </tr>
  </thead>
  <thead v-else class="masterCategory edit">
    <tr>
      <th class="col-6">
        <span class="name">{{ this.name }}</span>
      </th>
      <th class="col-6">
        <button class="illustration btn fas fa-level-up-alt" v-on:click="unarchiveMasterCategory"/>
        </th>
    </tr>
  </thead>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import StoreHandler from '@/store/StoreHandler'
import MasterCategoryService from '@/services/MasterCategoryService'
import { MasterCategory } from '@/model/model'

interface MasterCategoryFormData {
  name: string;
}

export default defineComponent({
  name: 'MasterCategoryForm',
  data (): MasterCategoryFormData {
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
