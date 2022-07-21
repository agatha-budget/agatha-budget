<template>
    <div class="row dateNav">
        <div class="col-2 d-flex justify-content-center" ><button type="button" class="btn fas fa-chevron-left" v-on:click="this.goToLastMonth()"/></div>
        <div class="col-8 date-label">
            <p class="title">{{ $d(this.getMonthAsDate(budgetMonth), 'monthString') }} <span v-if="!this.isThisYear"> {{ $d(this.getMonthAsDate(budgetMonth), 'year') }}</span></p>
        </div>
        <div class="col-2 d-flex justify-content-center" ><button type="button" class="btn fas fa-chevron-right" v-on:click="this.goToNextMonth()"/></div>
    </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import Time from '@/utils/Time'

interface DateNavData {
  budgetMonth: number;
}

export default defineComponent({
  name: 'DateNavigation',
  emits: ['changeMonth'],
  data (): DateNavData {
    return {
      budgetMonth: Time.getCurrentMonth()
    }
  },
  computed: {
    isThisYear (): boolean {
      return Time.monthIsThisYear(this.budgetMonth)
    }
  },
  methods: {
    getMonthAsDate (monthAsInt: number): Date {
      return Time.getMonthAsDate(monthAsInt)
    },
    async goToNextMonth () {
      this.$emit('changeMonth', 'next')
      this.budgetMonth = Time.getNextMonth(this.budgetMonth)
    },
    async goToLastMonth () {
      this.$emit('changeMonth', 'last')
      this.budgetMonth = Time.getLastMonth(this.budgetMonth)
    }
  }
})
</script>
