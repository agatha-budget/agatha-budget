<template>
    <div id="accountPageHeader">
        <div v-if="this.editingTitle" class="editingNameAccount row">
            <span class="name col-9 offset-0 col-sm-8 offset-sm-1 col-md-6 offset-md-3 col-lg-4 offset-lg-4 col-xl-4 offset-xl-4 col-xxl-4 offset-xxl-4">
                <input id="accountName" class="form-control" :placeholder=this.currentName v-model="name">
            </span>
            <span class="validation col">
              <button class="btn fas fa-check" v-on:click="updateName()"/>
              <button class="btn fas fa-times" v-on:click="this.cancelEditing()"/>
            </span>
          </div>
          <div v-else class="editableNameAccount">
            <a v-on:click="this.displayTitleEditing()">
              <h1>
                {{ this.account.name }} : {{ totalAccount }}€
                <button class="btn fas fa-pen" />
              </h1>
            </a>
          </div>
    </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import AccountService from '@/services/AccountService'
import { Account } from '@/model/model'

interface AccountPageHeader {
    name: string;
    editingTitle: boolean;
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
    }
  },
  data (): AccountPageHeader {
    return {
      name: '',
      editingTitle: false
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
    }
  }
})
</script>
