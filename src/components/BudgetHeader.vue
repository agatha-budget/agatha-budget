<template>
    <div id="budgetHeader">
        <div class="row date">
            <div class="col-2 d-flex justify-content-center" ><button type="button" class="btn fas fa-chevron-left" v-on:click="this.goToLastMonth()"/></div>
            <div class="col-8 date-label" :class="this.toBeBudgetedClass()">
                <p class="month">{{ $d(this.getMonthAsDate(budgetMonth), 'monthString') }} <span v-if="!this.isThisYear"> {{ $d(this.getMonthAsDate(budgetMonth), 'year') }}</span></p>
                <p class="toBeBudgeted" v-if="this.money > 0"> {{ addSpacesInThousand(money) }} € {{$t('TO_BE_BUDGETED')}}</p>
                <p class="toBePulledOut" v-else-if="this.money < 0"> {{ addSpacesInThousand(-1 * money) }} € {{$t('TO_BE_PULLED_OUT')}}</p>
            </div>
            <div class="col-2 d-flex justify-content-center" ><button type="button" class="btn fas fa-chevron-right" v-on:click="this.goToNextMonth()"/></div>
        </div>
        <table id="totalTable" class="table">
            <tr>
              <th class="col-6 name"></th>
              <th class="col-2 allocated"><div>{{ $t("ALLOCATED") }}</div></th>
              <th class="col-2 spent"><div>{{ $t("SPENT") }}</div></th>
              <th class="col-2 available"><div>{{ $t("AVAILABLE") }}</div></th>
            </tr>
            <tbody>
                <tr>
                    <td class="name"><div>{{ $t("TOTAL") }}</div></td>
                    <td class="allocated">{{ addSpacesInThousand(totalAllocated) }}</td>
                    <td class="spent">{{ addSpacesInThousand(totalSpent) }}</td>
                    <td class="available">{{ addSpacesInThousand(totalAvailable) }}</td>
                </tr>
            </tbody>
        </table>
    </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import Time from '@/utils/Time'
import Utils from '@/utils/Utils'

interface BudgetHeaderData {
    budgetMonth: number;
}

export default defineComponent({
  name: 'BudgetHeader',
  components: {},
  emits: ['changeMonth'],
  props: {
    month: {
      type: Number,
      required: true
    },
    totalAllocated: {
      type: Number,
      required: true
    },
    totalSpent: {
      type: Number,
      required: true
    },
    totalAvailable: {
      type: Number,
      required: true
    },
    money: {
      type: Number,
      required: true
    }
  },
  data (): BudgetHeaderData {
    return {
      budgetMonth: this.$props.month
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
    goToNextMonth () {
      this.$emit('changeMonth', 'next')
      this.budgetMonth = Time.getNextMonth(this.budgetMonth)
    },
    goToLastMonth () {
      this.$emit('changeMonth', 'last')
      this.budgetMonth = Time.getLastMonth(this.budgetMonth)
    },
    getEurosAmount (amount: number): number {
      return Utils.getEurosAmount(amount)
    },
    toBeBudgetedClass (): string {
      if (this.money > 0) {
        return 'positive'
      } else if (this.money < 0) {
        return 'negative'
      } else {
        return 'null'
      }
    },
    addSpacesInThousand (number: number): string {
      return number.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ' ')
    }
  }
})
</script>
