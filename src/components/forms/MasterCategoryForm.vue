<template>
  <thead v-if="!this.archived" class="container header masterCategory edit">
    <tr>
      <th class="col-6">
        <div class="darkTextInput form-group">
            <input type="text" class="form-control" v-model="name">
        </div>
      </th>
      <th class="col-2">
        <button class="illustration btn fas fa-check" v-on:click="updateMasterCategory"/>
      </th>
       <th class="col-2">
        <button class="illustration btn fas fa-times" v-on:click="this.$emit('loosesFocus')"/>
      </th>
      <th class="col-2">
        <button class="illustration btn fas fa-archive" v-on:click="archiveMasterCategory"/>
      </th>
    </tr>
  </thead>
  <thead v-else class="container header masterCategory edit">
    <tr>
      <th class="col-6">
        <span class="name">{{ this.name }}</span>
      </th>
      <th class="col-2">
        <button class="illustration btn fas fa-level-up-alt" v-on:click="unarchiveMasterCategory"/>
        </th>
      <th class="col-2">
        <button class="illustration btn fas fa-times" v-on:click="this.$emit('loosesFocus')"/>
      </th>
      <th class="col-2">
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
