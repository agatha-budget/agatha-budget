<template>
    <select v-model="currentGraph" class="selectGraph">
        <option value="pie">{{ $t('PIE_CHART') }}</option>
        <option value="bar">{{ $t('BAR_CHART') }}</option>
    </select>
</template>

<script lang="ts">
import { defineComponent } from 'vue'

interface ChartPageHeaderData {
  currentGraph: string;
}

export default defineComponent({
  name: 'ChartPageHeader',
  watch: {
    currentGraph: function () {
      this.$emit('changeGraph', this.currentGraph)
    }
  },
  data (): ChartPageHeaderData {
    return {
      currentGraph: 'pie'
    }
  }
})
</script>
