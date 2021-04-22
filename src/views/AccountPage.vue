<template >
  <div :class="this.$store.state.css">
    <div class="home row">
      <h1>test - {{ (this.account) ? this.account.name : ''}}</h1>
      <table class="operationTable table" >
          <tr class="">
            <th class="date"><div>{{ $t("DATE") }}</div></th>
            <th class="category">{{ $t("CATEGORY") }}</th>
            <th class="memo">{{ $t("MEMO") }}</th>
            <th class="amount">{{ $t("AMOUNT") }}</th>
          </tr>
          <tbody>
          <tr class="operation" v-for="operation in this.operations" :key="operation">
            <td class="date"><div>{{ $d(this.getDayAsDate(operation.day.comparable), 'day') }}</div></td>
            <td class="category">{{ $t(this.$store.state.categories[operation.categoryId].name) }}</td>
            <td class="memo">{{ $t(operation.memo) }}</td>
            <td class="amount">{{ operation.amount }}</td>
          </tr>
          </tbody>
      </table>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { redirectToLoginPageIfNotLogged } from '@/router'
import { Account } from '@/model/model'
import Time from '@/utils/Time'
import StoreHandler from '@/store/StoreHandler'
import OperationService from '@/services/OperationService'

interface AccountPageData {
    operations: any;
}

export default defineComponent({
  name: 'AccountPage',
  components: {

  },
  created: async function () {
    redirectToLoginPageIfNotLogged(this.$store)
    StoreHandler.initStore(this.$store)
    this.getAccountOperation()
  },
  watch: {
    account: async function () {
      this.getAccountOperation()
    }
  },
  props: ['accountId'],
  data (): AccountPageData {
    return {
      operations: {}
    }
  },
  computed: {
    account (): Account | null {
      return this.$store.state.accounts[this.accountId]
    }
  },
  methods: {
    async getAccountOperation () {
      if (this.account) {
        return OperationService.getOperations(this.account).then(
          (operations) => {
            this.operations = operations
          }
        )
      }
    },
    getDayAsDate (dayAsInt: number): Date {
      return Time.getDayAsDate(dayAsInt)
    }
  }
})
</script>
