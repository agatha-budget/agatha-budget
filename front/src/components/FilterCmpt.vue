<template>
  <div class="container header row form">
    <div class="containerCross col-12">
      <span class="cross fas fa-times-circle" v-on:click="closeFilter"/>
    </div>
    <div class ="label col-4 offset-0 col-sm-3 offset-sm-1 col-md-1">{{ $t("ENVELOPE") }}</div>
    <div class="selectAutoComplete col-6 offset-1 col-sm-5 col-md-4">
      <Multiselect
        v-model="categoryId"
        :groups="true"
        :searchable="true"
        :strict="false"
        :options="categories"
        :noResultsText="$t('NO_RESULT_FOUND')"
        :placeholder="$t('CHOOSE')"
      />
    </div>
    <button v-on:click="filter" class="actionButton col-6 offset-3 col-sm-4 offset-sm-4 col-md-2 offset-md-1">{{ $t("FILTER") }}</button>
  </div>
</template>

<script lang="ts">
import type { Category, GroupSelectOption, MasterCategory, SelectOption } from '@/model/model'
import { incomeCategoryId, transfertCategoryId } from '@/model/model'
import { useBudgetStore } from '@/stores/budgetStore'
import Multiselect from '@vueform/multiselect'
import { defineComponent } from 'vue'

interface FilterData {
  categoryId: string;
}

export default defineComponent({
  name: 'FilterCmpt',
  components: {
    Multiselect
  },
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
      for (const masterCategory of useBudgetStore().masterCategories) {
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
      return useBudgetStore().getCategoriesByMasterCategory(masterCategory, false)
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
