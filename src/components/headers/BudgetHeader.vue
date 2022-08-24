<template>
    <div id="budgetHeader">
       <!--Calendar-->
      <DateNav :fromPage="'budget'" :money="money" @change-month="changeMonth"/>
      <table  class="budgetTable">
        <!-- Table column label-->
        <thead class="masterCategory collapsed col-6 offset-6">
          <tr>
            <th class="col-6"></th>
            <th class="col-2 amountCol">{{ $t("ALLOCATED") }}</th>
            <th class="col-2 amountCol spent">{{ $t("SPENT") }}</th>
            <th class="col-2 amountCol ">{{ $t("AVAILABLE") }}</th>
          </tr>
        </thead>
        <!-- Total for all table-->
        <thead class="masterCategory collapsed">
          <tr>
            <th>{{ $t("TOTAL") }}</th>
            <th class="amountCol">{{ addSpacesInThousand(totalAllocated) }}</th>
            <th class="amountCol spent">{{ addSpacesInThousand(totalSpent) }}</th>
            <th class="amountCol">{{ addSpacesInThousand(totalAvailable) }}</th>
          </tr>
        </thead>
      </table>
    </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import DateNav from '@/components/inputs/DateNavigation.vue'
import Utils from '@/utils/Utils'

interface BudgetHeaderData {
    budgetMonth: number;
    budgetMoney: number;
}

export default defineComponent({
  name: 'BudgetHeader',
  components: {
    DateNav
  },
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
      budgetMonth: this.$props.month,
      budgetMoney: this.$props.money
    }
  },
  methods: {
    changeMonth (message: string) {
      this.$emit('changeMonth', message)
    },
    addSpacesInThousand (number: number): string {
      return Utils.addSpacesInThousand(number)
    }
  }
})
</script>
