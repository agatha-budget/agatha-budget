<template>
  <thead v-if="!this.archived" class="masterCategory edit" :style=style>
    <tr>
      <th class="col-6">
        <div class="darkTextInput form-group">
            <input type="textInput" class="form-control" v-model="name" v-on:change="changeName" :style="{'background': color}">
        </div>
      </th>
        <th v-if="colorPicker" class="col-3">
          <input type="color" v-model="color"/>
          <button class="ban-color illustration btn fas fa-ban" v-on:click="removeColor"/>
        </th>
        <th v-if="colorPicker" class="col-3">
          <button class="illustration btn fas fa-check" v-on:click="validColor"/>
        </th>
        <th v-if="!colorPicker" class="col-3">
          <button class="illustration btn fas fa-palette" v-on:click="chooseColor"/>
        </th>
        <th v-if="!colorPicker" class="col-3">
          <button class="illustration btn fas fa-archive" v-on:click="archiveMasterCategory"/>
        </th>
    </tr>
  </thead>
  <thead v-else class="masterCategory edit" :style=style>
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
import { navigationColor, Color } from '@/utils/Color'

interface MasterCategoryFormData {
  name: string;
  color: string;
  colorPicker: boolean;
}

export default defineComponent({
  name: 'MasterCategoryForm',
  data (): MasterCategoryFormData {
    return {
      name: this.masterCategory.name,
      color: this.masterCategory.color !== 'null' ? this.masterCategory.color : navigationColor,
      colorPicker: false
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
  emits: ['emptyMasterCategory'],
  computed: {
    style (): string {
      return this.masterCategory.color !== null ? 'background : linear-gradient(to right, ' + Color.shadeColor(this.color, -50) + ',' + this.color + ')' : ''
    }
  },
  methods: {
    updateMasterCategory () {
      MasterCategoryService.renameMasterCategory(this.masterCategory.id, this.name).then(
        () => {
          StoreHandler.updateMasterCategories(this.$store)
        }
      )
    },
    archiveMasterCategory () {
      this.$emit('emptyMasterCategory', this.masterCategory.id)
      MasterCategoryService.archiveMasterCategory(this.masterCategory.id).then(
        () => {
          StoreHandler.updateCategories(this.$store)
        }
      )
    },
    unarchiveMasterCategory () {
      MasterCategoryService.unarchiveMasterCategory(this.masterCategory.id).then(
        () => {
          StoreHandler.updateCategories(this.$store)
        }
      )
    },
    chooseColor () {
      this.colorPicker = !this.colorPicker
    },
    removeColor () {
      MasterCategoryService.updateColorMasterCategory(this.masterCategory.id, 'null').then(
        () => {
          StoreHandler.updateMasterCategories(this.$store)
          this.colorPicker = false
        }
      )
    },
    validColor () {
      MasterCategoryService.updateColorMasterCategory(this.masterCategory.id, this.color).then(
        () => {
          StoreHandler.updateMasterCategories(this.$store)
          this.colorPicker = false
        }
      )
    },
    changeName () {
      MasterCategoryService.renameMasterCategory(this.masterCategory.id, this.name).then(
        () => {
          StoreHandler.updateMasterCategories(this.$store)
        }
      )
    }
  }
})
</script>
