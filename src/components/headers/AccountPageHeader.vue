<template>
    <div id="accountPageHeader">
      <div  class="editableNameAccount row">
        <div>
          <div class="col-md-10 offset-md-1 col-sm-12 offset-sm-0 row">
            <div class="col-md-8 offset-md-2 col-sm-12 offset-sm-0" :class="this.getClassDependingOnAmount()">
              <!-- Normal Title -->
              <div v-if="!this.editingTitle">
                <h1 class="title clickable row breakableRow" v-on:click="this.displayTitleEditing()">
                  <div class="col-md col-sm-12 left">{{ this.name }} :</div>
                  <div class="col-md col-sm-12 right">{{ amountAsString }} €</div>
                </h1>
              </div>
              <!-- Edit Title -->
              <div v-else class="editingNameAccount row">
                <span class="name">
                    <input id="accountName" class="form-control" :placeholder=this.name v-model="name">
                </span>
                <span class="validation">
                  <button class="illustration btn fas fa-check" v-on:click="updateName()"/>
                </span>
              </div>
              <!-- Pending -->
              <div v-if="existingPendingOperation" class="subtitle pendingSection breakableRow row clickable" v-on:click="this.displayTitleEditing()">
                <div class="col-md col-sm-12 left">{{ realAmountAsString }} € {{ $t("ON_ACCOUNT") }}</div>
                <div class="col-md col-sm-12 right">{{ symbolBeforePendingAmount  }} {{ pendingAmount  }} € {{ $t("PENDING") }} (<span class="illustration fas fa-hourglass-half"/>)</div>
              </div>
            </div>
            <div v-if="!this.editingTitle" class="editTitleBtn col-md col-sm">
              <button v-on:click="this.displayTitleEditing()" class="illustration btn fas fa-pen" />
            </div>
          </div>
        </div>
      </div>
    </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import AccountService from '@/services/AccountService'
import { Account } from '@/model/model'
import Utils from '@/utils/Utils'

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
      return Utils.addSpacesInThousand(this.totalAccount / 100)
    },
    realAmountAsString (): string {
      return Utils.addSpacesInThousand(this.realAmountOnAccount / 100)
    },
    pendingAmount (): string {
      const pendingCents = Math.abs(this.totalAccount - this.realAmountOnAccount)
      return Utils.addSpacesInThousand(pendingCents / 100)
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
      AccountService.updateAccount(this.$store, this.accountId, this.name)
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
      for (const account of this.$store.state.accounts) {
        if (account.id === this.accountId) {
          this.name = account.name
          this.account = account
        }
      }
    }
  }
})
</script>
