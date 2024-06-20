<template>
    <div id="accountPageHeader">
      <div  class="editableNameAccount row">
        <div>
          <div class="col-md-10 offset-md-1 col-sm-12 offset-sm-0 row">
            <div class="col-md-8 offset-md-2 col-sm-12 offset-sm-0 headerContent" :class="getClassDependingOnAmount()">
              <!-- Normal Title -->
              <div v-if="!editingTitle">
                <h1 class="title clickable breakableRow" v-on:click="displayTitleEditing()">
                  <span class="breakable">{{ name }} : </span>
                  <span class="breakable">{{ amountAsString }} €</span>
                </h1>
                <button v-on:click="displayTitleEditing()" class="illustration btn fas fa-pen" />
              </div>
              <!-- Edit Title -->
              <div v-else class="editingNameAccount">
                <span class="name">
                    <input id="accountName" class="form-control title" :placeholder=name v-model="name">
                </span>
                <span class="validation">
                  <button class="illustration btn fas fa-check" v-on:click="updateName()"/>
                </span>
              </div>
              <!-- Pending -->
              <div v-if="existingPendingOperation" class="subtitle pendingSection breakableRow clickable" v-on:click="displayTitleEditing()">
                <span class="breakable">{{ realAmountAsString }} € {{ $t("ON_ACCOUNT") }}</span>
                <span class="breakable">{{ symbolBeforePendingAmount  }} {{ pendingAmount  }} € {{ $t("PENDING") }} (<span class="illustration fas fa-hourglass-half"/>)</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import AccountService from '@/services/AccountService'
import type { Account } from '@/model/model'
import Utils from '@/utils/Utils'
import { useBudgetStore } from '@/stores/budgetStore'

interface AccountPageHeaderData {
    account: Account | null;
    name: string;
    editingTitle: boolean;
    displayRealAmount: boolean;
}

export default defineComponent({
  name: 'AccountPageHeader',
  components: {},
  watch: {},
  props: {
    accountId: {
      type: String,
      required: true
    },
    totalAccount: {
      type: Number,
      required: true
    },
    existingPendingOperation: {
      type: Boolean,
      required: true
    },
    realAmountOnAccount: {
      type: Number,
      required: true
    }
  },
  data (): AccountPageHeaderData {
    return {
      account: null,
      name: '',
      editingTitle: false,
      displayRealAmount: false
    }
  },
  created: function () {
    this.getAccount()
  },
  computed: {
    amountAsString (): string {
      return Utils.centsToEurosDisplay(this.totalAccount)
    },
    realAmountAsString (): string {
      return Utils.centsToEurosDisplay(this.realAmountOnAccount)
    },
    pendingAmount (): string {
      const pendingCents = Math.abs(this.totalAccount - this.realAmountOnAccount)
      return Utils.centsToEurosDisplay(pendingCents)
    },
    pendingIsNegative (): boolean {
      return (this.totalAccount - this.realAmountOnAccount) < 0
    },
    symbolBeforePendingAmount (): string {
      return (this.pendingIsNegative) ? ' -' : ' +'
    }
  },
  emits: [],
  methods: {
    updateName () {
      AccountService.updateAccount(this.accountId, this.name)
      this.editingTitle = !this.editingTitle
    },
    displayTitleEditing () {
      this.editingTitle = !this.editingTitle
    },
    getClassDependingOnAmount (): string {
      if (this.totalAccount < 0) {
        return 'negative'
      } else {
        return ''
      }
    },
    displayAmount () {
      this.displayRealAmount = !this.displayRealAmount
    },
    getAccount () {
      for (const account of useBudgetStore().accounts) {
        if (account.id === this.accountId) {
          this.name = account.name
          this.account = account
        }
      }
    }
  }
})
</script>
