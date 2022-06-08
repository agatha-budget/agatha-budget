<template>
  <div class="container header row form">
    <div class="containerCross col-12">
      <span class="cross fas fa-times-circle" v-on:click="closeFilter"/>
    </div>
    <div class ="label col-4 offset-0 col-lg-3 offset-lg-2">{{ $t("CATEGORY") }} :</div>
    <div class="selectAutoComplete col-6 offset-1 col-lg-4 offset-lg-1">
      <Multiselect
        v-model="categoryId"
        :groups="true"
        :searchable="true"
        :options="categories"
        :noResultsText="$t('NO_RESULT_FOUND')"
        :placeholder="$t('SELECT_CATEGORY')"
      />
    </div>
    <btn v-on:click="filter" class="actionButton col-4 offset-4">{{ $t("FILTER") }}</btn>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { Category, MasterCategory, incomeCategoryId, transfertCategoryId, GroupSelectOption, SelectOption } from '@/model/model'
import StoreHandler from '@/store/StoreHandler'
import Multiselect from '@vueform/multiselect'

interface FilterData {
  categoryId: string;
}

export default defineComponent({
  name: 'FilterCmpt',
  components: {
    Multiselect
  },
  props: { },
  computed: {
    categories (): GroupSelectOption[] {
      const optionsList = [
        {
          label: this.$t('DEFAULT'),
          options: [
            { value: incomeCategoryId, label: this.$t('I18N_INCOME') },
            { value: transfertCategoryId, label: this.$t('I18N_TRASNFERT') }
          ]
        }
      ]
      for (const masterCategory of this.$store.state.masterCategories) {
        const categories = this.getCategoriesByMasterCategory(masterCategory)
        if (categories.length > 0) {
          optionsList.push(this.createOptionGroup(masterCategory, categories))
        }
      }
      return optionsList
    }
  },
  data (): FilterData {
    return {
      categoryId: ''
    }
  },
  emits: ['closeFilter', 'filteringCategory'],
  methods: {
    getCategoriesByMasterCategory (masterCategory: MasterCategory): Category[] {
      return StoreHandler.getCategoriesByMasterCategory(this.$store, masterCategory, false)
    },
    createOptionGroup (masterCategory: MasterCategory, categories: Category[]): GroupSelectOption {
      const group: GroupSelectOption = {
        label: masterCategory.name,
        options: []
      }
      for (const category of categories) {
        const option: SelectOption = { value: category.id, label: category.name }
        group.options.push(option)
      }
      return group
    },
    closeFilter () {
      this.$emit('closeFilter')
    },
    filter () {
      this.$emit('filteringCategory', this.categoryId)
    }
  }
})
</script>
