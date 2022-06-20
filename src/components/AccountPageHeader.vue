<template>
    <div id="accountPageHeader">
        <div v-if="this.editingTitle" class="editingNameAccount row">
            <span class="name col-9 offset-0 col-sm-8 offset-sm-1 col-md-6 offset-md-3 col-lg-4 offset-lg-4 col-xl-4 offset-xl-4 col-xxl-4 offset-xxl-4">
                <input id="accountName" class="form-control" :placeholder=this.currentName v-model="name">
            </span>
            <span class="validation col">
              <button class="illustration btn fas fa-check" v-on:click="updateName()"/>
              <button class="illustration btn fas fa-times" v-on:click="this.cancelEditing()"/>
            </span>
          </div>
          <div v-else class="editableNameAccount row">
            <div v-if="this.displayRealAmount" class="col-10 offset-1">
              <h1 class="title">{{ $t("REAL_AMOUNT") }} : {{ realAmountOnAccount }} €</h1>
            </div>
            <div v-else class="col-10 offset-1">
              <span v-on:click="this.displayTitleEditing()" class="row">
                <div class="displayNameAccount col-12 offset-0 col-sm-8 offset-sm-2 col-xxl-6 offset-xxl-3">
                  <h1 class="title" :class="this.getClassDependingOnAmount()">
                    {{ this.account.name }} : {{ totalAccount }} €
                    <button class="btn fas fa-pen" />
                  </h1>
                </div>
              </span>
            </div>
            <div v-if="existingPendingOperation" class="col-1">
              <div v-if="this.displayRealAmount">
                <button v-on:click="displayAmount" class="illustration btn fas fa-calendar-check"/>
              </div>
              <div v-else>
                <button v-on:click="displayAmount" class="illustration btn fas fa-hourglass-half"/>
              </div>
            </div>
          </div>
    </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import AccountService from '@/services/AccountService'
import { Account } from '@/model/model'

interface AccountPageHeaderData {
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
      name: '',
      editingTitle: false,
      displayRealAmount: false
    }
  },
  computed: {
    account (): Account | null {
      for (const account of this.$store.state.accounts) {
        if (account.id === this.accountId) {
          return account
        }
      }
      return null
    },
    currentName (): string {
      return this.account?.name || ''
    }
  },
  emits: [],
  methods: {
    updateName () {
      AccountService.updateAccount(this.$store, this.accountId, this.name)
      this.editingTitle = false
    },
    displayTitleEditing () {
      this.editingTitle = true
    },
    cancelEditing () {
      this.editingTitle = false
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
    }
  }
})
</script>
