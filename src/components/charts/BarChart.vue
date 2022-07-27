<template>
  <Bar
    :chart-options="chartOptions"
    :chart-data="chartData"
    :chart-id="chartId"
    :dataset-id-key="datasetIdKey"
    :plugins="plugins"
    :css-classes="cssClasses"
    :styles="styles"
    :width="width"
    :height="height"
  />
</template>

<script lang="ts">

import { Bar } from 'vue-chartjs'
import { Chart as ChartJS, Title, Tooltip, Legend, BarElement, CategoryScale, LinearScale } from 'chart.js'
import pattern from 'patternomaly'

interface BarChartData {
    chartDataDefault: Record<string, any>;
    chartOptions: Record<string, any>;
}

ChartJS.register(Title, Tooltip, Legend, BarElement, CategoryScale, LinearScale)

export default {
  name: 'BarChart',
  components: { Bar },
  props: {
    chartData: {
      type: Object
    },
    chartId: {
      type: String,
      default: 'bar-chart'
    },
    datasetIdKey: {
      type: String,
      default: 'label'
    },
    width: {
      type: Number,
      default: 400
    },
    height: {
      type: Number,
      default: 400
    },
    cssClasses: {
      default: '',
      type: String
    },
    styles: {
      type: Object
    },
    plugins: {
      type: Object
    }
  },
  data (): BarChartData {
    return {
      chartDataDefault: {
        labels: [],
        datasets: [
          {
            label: '',
            backgroundColor: '',
            data: []
          }
        ]
      },
      chartOptions: {
        responsive: true
      }
    }
  }
}
</script>
