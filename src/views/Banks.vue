<template>
  <div :class="this.$store.state.css">
    <div class="bankPage menuLayout row col-md-8 offset-md-2 col-xxl-6 offset-xxl-3">
        <div class="header fixed title">
          {{ $t('BANKS') }}
        </div>
        <div class="placeholder top">
          {{ $t('BANKS') }}
        </div>

        <div class="banner">
          <div class="title">{{ $t('SYNCHRONISED_ACCOUNTS') }}</div>
        </div>
        <div class="banner">
          <div class="title">{{ $t('ADD_A_BANK') }}</div>
        </div>
        <p>{{ $t('SELECT_BANK_TO_AUTHORIZE_SYNCHRONISATION') }}</p>
        <template v-for="bank in this.availableBanks" :key="bank">
          <btn class="navigationButton bankButton" v-on:click="getBankAuthorization(bank.id)">
            <img class="illustration col-2" alt="banklogo" :src=bank.logo />
            <span class="illustrationLabel col-10">{{ bank.name }}</span>
          </btn>
        </template>

        <div class="placeholder bottom">
          <NavMenu :page="'profile'" />
        </div>
        <div class="footer fixed">
          <NavMenu :page="'profile'" />
        </div>
      </div>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import NavMenu from '@/components/NavigationMenu.vue'
import BankingService from '@/services/BankingService'
import { Bank } from '@/model/model'

interface BanksData {
    availableBanks: Bank[];
}

export default defineComponent({
  name: 'banksPage',
  components: { NavMenu },
  created: async function () {
    this.getAvailableBanks()
  },
  data (): BanksData {
    return {
      availableBanks: []
    }
  },
  methods: {
    async getAvailableBanks () {
      BankingService.getAvailableBanks().then(
        (bankList) => {
          this.availableBanks = bankList
        }
      )
    },
    getBankAuthorization (bankId: string) {
      BankingService.goToBankAgreement(bankId)
    }
  }
})
</script>
