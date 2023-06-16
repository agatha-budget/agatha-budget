<template>
    <div class="row dateNav">
        <div class="col-2 d-flex justify-content-center" ><button type="button" class="btn fas fa-chevron-left" v-on:click="this.goToLastMonth()"/></div>
        <div class="col-8 date-label" :class="this.toBeBudgetedClass()">
            <p class="title">{{ $d(this.getMonthAsDate(selectedMonth), 'monthString') }} <span v-if="!this.isThisYear"> {{ $d(this.getMonthAsDate(selectedMonth), 'year') }}</span></p>
            <p class="title" v-if="this.money > 0"> {{ centsToEurosDisplay(money) }} € {{$t('TO_BE_BUDGETED')}}</p>
            <p class="title" v-else-if="this.money < 0"> {{ centsToEurosDisplay(-1 * money) }} € {{$t('TO_BE_PULLED_OUT')}}</p>
        </div>
        <div class="col-2 d-flex justify-content-center" ><button type="button" class="btn fas fa-chevron-right" v-on:click="this.goToNextMonth()"/></div>
    </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import Time from '@/utils/Time'
import Utils from '@/utils/Utils'

interface DateNavData {
  selectedMonth: number;
}

export default defineComponent({
  name: 'DateNavigation',
  emits: ['changeMonth'],
  props: {
    fromPage: {
      type: String,
      required: true
    },
    money: {
      type: Number,
      required: false,
      default: 0
    }
  },
  data (): DateNavData {
    return {
      selectedMonth: Time.getCurrentMonth()
    }
  },
  computed: {
    isThisYear (): boolean {
      return Time.monthIsThisYear(this.selectedMonth)
    }
  },
  methods: {
    getMonthAsDate (monthAsInt: number): Date {
      return Time.getMonthAsDate(monthAsInt)
    },
    async goToNextMonth () {
      this.$emit('changeMonth', 'next')
      this.selectedMonth = Time.getNextMonth(this.selectedMonth)
    },
    async goToLastMonth () {
      this.$emit('changeMonth', 'last')
      this.selectedMonth = Time.getLastMonth(this.selectedMonth)
    },
    toBeBudgetedClass (): string {
      if (this.$props.fromPage === 'budget') {
        if (this.money > 0) {
          return 'positive'
        } else if (this.money < 0) {
          return 'negative'
        } else {
          return 'null'
        }
      } else {
        return ''
      }
    },
    centsToEurosDisplay (number: number): string {
      return Utils.centsToEurosDisplay(number)
    }
  }
})
</script>
