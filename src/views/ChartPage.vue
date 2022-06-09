<template>
  <div :class="this.$store.state.css">
    <div class="profilePage menuLayout row col-md-4 offset-md-4 col-xl-8 offset-xl-2">
        <div class="header fixed title">
          {{ $t('CHART') }}
        </div>
        <div class="placeholder top">
          {{ $t('CHART') }}
        </div>

        <BarChart/>
        <div class="placeholder bottom">
            <NavMenu :page="'chart'" />
        </div>
        <div class="footer fixed">
            <NavMenu :page="'chart'" />
        </div>
      </div>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import BarChart from '@/components/charts/BarChart.vue'
import NavMenu from '@/components/NavigationMenu.vue'

export default defineComponent({
  name: 'Login',
  components: {
    BarChart,
    NavMenu
  },
  data () {
    return { }
  },
  methods: { }

})
</script>
