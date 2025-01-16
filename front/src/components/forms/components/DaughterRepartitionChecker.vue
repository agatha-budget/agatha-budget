<template>
  <div class="daughterRepartitionChecker">
    <div v-if="done"  class="container all_good">
      {{ $t("GOOD_REPARTITION") }}
    </div>
    <div v-else class="container warning">
      <p>
      <span v-if=tooMuchShared>
        {{ $t("NEED_REMOVE") }}
      </span>
      <span v-else>
        {{ $t("NEED_ADD") }}
      </span>
      &nbsp;<span class="spot" :class="getClassDependingOnAmount(remaining)">
        {{centsToSignedEurosDisplay(remaining)}} €
      </span>
      </p>
      <p>
        <button  class="actionButton add" v-on:click="addDaughter">{{ $t('DO_IT_AUTOMATICALLY') }}</button>
      </p>
      <p class="subtext"> {{ $t("CLICK_VALIDATE_TO_UPDATE") }}</p>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import Utils from '@/utils/Utils'

export default defineComponent({
  name: 'DaughterRepartitionChecker',
  props: {
    toShare: {
      type: Number,
      required: true
    },
    shared: {
      type: Number,
      required: true
    }
  },
  emits: ['addDaughter'],
  computed: {
    done (): boolean {
      return this.toShare === this.shared
    },
    tooMuchShared (): boolean {
      return this.toShare < this.shared
    },
    remaining(): number {
      return this.toShare - this.shared
    }
  },
  methods: {
    centsToEurosDisplay (amount: number): string {
      return Utils.centsToEurosDisplay(amount)
    },
    centsToSignedEurosDisplay(amount: number) : string {
      let amoutString = Utils.centsToEurosDisplay(amount)
      return (amount > 0) ? "+" + amoutString : amoutString
    },
    getClassDependingOnAmount (amount: number): string {
      if (amount > 0) {
        return 'positive'
      } else {
        return 'negative'
      }
    },
    addDaughter(){
      this.$emit('addDaughter', this.remaining)
    }
  }
})
</script>
